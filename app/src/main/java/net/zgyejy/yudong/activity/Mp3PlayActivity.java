package net.zgyejy.yudong.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.modle.Mp3Text;
import net.zgyejy.yudong.util.ConstUtil;
import net.zgyejy.yudong.util.PlayService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import static net.zgyejy.yudong.util.PlayService.mediaPlayer;

public class Mp3PlayActivity extends MyBaseActivity {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    private Mp3Text mp3Text;//mp3文档信息
    public static TextView mp3Name, timeNow, timeAll;
    private ImageView cd, needle, play, back,front, downLoad,share;
    public static SeekBar mp3Progress;
    boolean isPlaying = false;//是否正在播放
    private MusicBoxReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_video51_mp3);
        ShareSDK.initSDK(this);
        ButterKnife.bind(this);
        rlBack.setVisibility(View.VISIBLE);
        mp3Text = (Mp3Text) getIntent().getExtras().getSerializable("mp3Txt");
        initView();
        playMp3();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PlayService.class);
        this.stopService(intent);
        if (receiver != null) {
            try {
                unregisterReceiver(receiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    private void playMp3() {
        if (mp3Text != null) {
            mp3Name.setText(mp3Text.getMusic_name());
            //注册接收器
            showToast("mp3加载中\n请稍候...");
            if (receiver == null)
                receiver = new MusicBoxReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConstUtil.MUSICBOX_ACTION);
            registerReceiver(receiver, filter);

            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //启动后台Service
                                Intent intent = new Intent(Mp3PlayActivity.this, PlayService.class);
                                intent.putExtra("mp3Url", API.APP_SERVER_IP + mp3Text.getMusic());//传递过去MP3资源地址
                                intent.putExtra("from", 1);
                                startService(intent);
                                showToast("mp3缓冲完毕，开始播放！");
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private void initView() {
        //初始化mp3界面
        mp3Name = (TextView) findViewById(R.id.tv_mp3_name);
        timeNow = (TextView) findViewById(R.id.tv_mp3_timeNow);
        timeAll = (TextView) findViewById(R.id.tv_mp3_timeAll);
        cd = (ImageView) findViewById(R.id.iv_mp3_cd);
        needle = (ImageView) findViewById(R.id.iv_mp3_needle);
        play = (ImageView) findViewById(R.id.iv_mp3_play);
        back = (ImageView) findViewById(R.id.iv_mp3_back);
        front = (ImageView) findViewById(R.id.iv_mp3_front);
        downLoad = (ImageView) findViewById(R.id.iv_mp3_downLoad);
        mp3Progress = (SeekBar) findViewById(R.id.sb_mp3_progress);
        share = (ImageView) findViewById(R.id.iv_mp3_share);

        //（加载MP3数据）..................
        mp3Progress.setOnSeekBarChangeListener(sChangeListener);
        play.setOnClickListener(listener);
        needle.setOnClickListener(listener);
        share.setOnClickListener(listener);
    }

    /**
     * SeekBar进度改变事件
     */
    SeekBar.OnSeekBarChangeListener sChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            //当拖动停止后，控制mediaPlayer播放指定位置的音乐
            mediaPlayer.seekTo(seekBar.getProgress());
            PlayService.isChanging = false;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            PlayService.isChanging = true;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // TODO Auto-generated method stub

        }
    };

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            switch (v.getId()) {
                case R.id.iv_mp3_needle:
                case R.id.iv_mp3_play://播放或暂停
                    if (!isPlaying) {
                        play.setImageResource(R.drawable.mp3pause);
                        sendBroadcastToService(ConstUtil.STATE_PLAY);
                        turnCD();
                        needlePlay();
                        isPlaying = true;
                    } else {
                        play.setImageResource(R.drawable.mp3play2);
                        sendBroadcastToService(ConstUtil.STATE_PAUSE);
                        cd.clearAnimation();
                        needlePause();
                        isPlaying = false;
                    }
                    break;
                case R.id.iv_mp3_share:
                    showShare();
                    break;
                default:
                    break;
            }
        }
    };

    @OnClick(R.id.mp3PlayBack)
    public void onClick() {
        finish();
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(mp3Text.getMusic_name());
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(API.APP_SERVER_IP + mp3Text.getMusic());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mp3Text.getMusic_name());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("给大家分享个音乐！");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.zgyejy.net/");

        // 启动分享GUI
        oks.show(this);
    }


    //创建一个广播接收器用于接收后台Service发出的广播
    class MusicBoxReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            int play = intent.getIntExtra("play", -1);
            switch (play) {
                case ConstUtil.SERVICE_READY://后台服务准备完毕
                    playPrepared();
                    break;
                case ConstUtil.SERVICE_OVER://音乐播放完毕
                    playComplete();
                    break;
                case ConstUtil.SERVICE_UPDATE://更新进度
                    timeNow.setText(intent.getStringExtra("timeNow"));
                    break;
                default:
                    break;
            }
        }
    }

    private void playPrepared() {
        play.setImageResource(R.drawable.mp3pause);
        /*sendBroadcastToService(ConstUtil.STATE_PLAY);*/
        turnCD();
        isPlaying = true;
    }

    private void playComplete() {
        showToast("音乐播放完了！");
        play.setImageResource(R.drawable.mp3play2);
        sendBroadcastToService(ConstUtil.STATE_PAUSE);
        cd.clearAnimation();
        isPlaying = false;
    }

    /**
     * 转动cd
     */
    private void turnCD() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.cd_animation);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        cd.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 向后台Service发送控制广播
     *
     * @param state int state 控制状态码
     */
    protected void sendBroadcastToService(int state) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setAction(ConstUtil.MUSICSERVICE_ACTION);
        intent.putExtra("control", state);
        //向后台Service发送播放控制的广播
        sendBroadcast(intent);
    }

    private void needlePause() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.needle_animation_pause);
        needle.startAnimation(animation);
    }

    private void needlePlay() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.needle_animation_play);
        needle.startAnimation(animation);
    }
}
