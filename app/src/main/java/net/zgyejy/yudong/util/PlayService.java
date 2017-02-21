package net.zgyejy.yudong.util;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.zgyejy.yudong.activity.Mp3PlayActivity;
import net.zgyejy.yudong.activity.Video51Activity;

import java.io.IOException;
import java.util.Formatter;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static net.zgyejy.yudong.activity.Video51Activity.timeNow;


/**
 * Created by Administrator on 2016/11/25 0025.
 */

public class PlayService extends Service{
    private MusicSercieReceiver receiver;
    public static MediaPlayer mediaPlayer;
    public static boolean isChanging=false;//互斥变量，防止定时器与SeekBar拖动时进度冲突
    private boolean isPlaying;
    private String mp3Url;
    Timer mTimer;
    TimerTask mTimerTask;
    //记录Timer运行状态
    boolean isTimerRunning=false;
    //当前播放状态
    int state=ConstUtil.STATE_NON;
    private ProgressBar mp3Progress;
    private TextView timeAll;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //注册接收器
        if (receiver == null)
            receiver=new MusicSercieReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(ConstUtil.MUSICSERVICE_ACTION);
        registerReceiver(receiver, filter);
        // TODO Auto-generated method stub
        mediaPlayer = new MediaPlayer();

        //为mediaPlayer的完成事件创建监听器
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                if (mTimer!=null) {
                    mTimer.cancel();//取消定时器
                    //发送广播通知前台Activity已播放完毕，更新界面
                    Intent intent=new Intent();
                    intent.setAction(ConstUtil.MUSICBOX_ACTION);
                    intent.putExtra("play",ConstUtil.SERVICE_OVER);
                    sendBroadcast(intent);
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        try {
            mediaPlayer.stop();
        } catch(Exception e) {
            e.printStackTrace();
        }
        unregisterReceiver(receiver);
        stopSelf();
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        int from = intent.getIntExtra("from",0);
        if (from == 0) {
            mp3Progress = Video51Activity.mp3Progress;
            timeAll = Video51Activity.timeAll;
        }else {
            mp3Progress = Mp3PlayActivity.mp3Progress;
            timeAll = Mp3PlayActivity.timeAll;
        }
        mp3Url = intent.getStringExtra("mp3Url");
        //发送广播通知前台服务已准备完毕，可以开始播放
        Intent intent2 = new Intent();
        intent2.setAction(ConstUtil.MUSICBOX_ACTION);
        intent2.putExtra("play",ConstUtil.SERVICE_READY);
        sendBroadcast(intent2);

        prepareAndPlay();
        state=ConstUtil.STATE_PLAY;
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        isPlaying = intent.getBooleanExtra("isPlaying", false);
        if (isPlaying){
            mediaPlayer.start();
        }else {
            mediaPlayer.pause();
        }
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 格式化时间显示
     */
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    /**
     * 装载和播放音乐
     * */
    protected void prepareAndPlay() {
        // TODO Auto-generated method stub

        try {
            //获取assets目录下指定文件的AssetFileDescriptor对象
            mediaPlayer.reset();//初始化mediaPlayer对象
            mediaPlayer.setDataSource(mp3Url);
            //准备播放音乐
            mediaPlayer.prepare();
            //播放音乐
            mediaPlayer.start();
            //getDuration()方法要在prepare()方法之后，否则会出现Attempt to call getDuration without a valid mediaplayer异常
            mp3Progress.setMax(mediaPlayer.getDuration());//设置SeekBar的长度
            timeAll.setText(stringForTime(mediaPlayer.getDuration()));//设置时间
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //----------定时器记录播放进度---------//
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                isTimerRunning=true;
                if(isChanging==true)//当用户正在拖动进度进度条时不处理进度条的的进度
                    return;
                mp3Progress.setProgress(mediaPlayer.getCurrentPosition());
                //发送广播通知前台更新播放进度显示
                if (timeIntent == null){
                    timeIntent = new Intent();
                    timeIntent.setAction(ConstUtil.MUSICBOX_ACTION);
                    timeIntent.putExtra("play",ConstUtil.SERVICE_UPDATE);
                    timeIntent.putExtra("timeNow","00:00");
                }else {
                    timeIntent.putExtra("timeNow",stringForTime(mediaPlayer.getCurrentPosition()));
                }
                sendBroadcast(timeIntent);
            }
        };
        //每隔500毫秒检测一下播放进度
        mTimer.schedule(mTimerTask, 0, 500);

    }

    Intent timeIntent;

    //创建广播接收器用于接收前台Activity发去的广播
    class MusicSercieReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            int control=intent.getIntExtra("control", -1);
            switch (control) {
                case ConstUtil.STATE_PLAY://播放音乐
                    if (state==ConstUtil.STATE_PAUSE) {//如果原来状态是暂停
                        mediaPlayer.start();
                    }else if (state!=ConstUtil.STATE_PLAY) {
                        prepareAndPlay();
                    }
                    state=ConstUtil.STATE_PLAY;
                    break;
                case ConstUtil.STATE_PAUSE://暂停播放
                    if (state==ConstUtil.STATE_PLAY) {
                        mediaPlayer.pause();
                        state=ConstUtil.STATE_PAUSE;
                    }
                    break;
                case ConstUtil.STATE_STOP://停止播放
                    if (state==ConstUtil.STATE_PLAY||state==ConstUtil.STATE_PAUSE) {
                        mediaPlayer.stop();
                        state=ConstUtil.STATE_STOP;
                    }
                    break;
                default:
                    break;
            }
        }

    }
}
