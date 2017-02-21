package net.zgyejy.yudong.live.ui.widget;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import net.zgyejy.yudong.R;
import net.zgyejy.yudong.live.controller.AudioManager;
import net.zgyejy.yudong.live.controller.DialogManager;
import java.io.File;


/**
 * 录音按钮
 * Created by Administrator on 2017/1/19 0019.
 */

public class AudioRecordButton extends Button implements AudioManager.AudioStateListener {
    private static final String TAG = "AudioRecordButton";
    
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;

    private static final int DISTANCE_Y_CANCEL = 50;

    private int mCurrentState = STATE_NORMAL;
    // 已经开始录音
    private boolean isRecording = false;

    private DialogManager mDialogManager;

    private AudioManager mAudioManager;

    private float mTime = 0;
    // 是否触发了onlongclick，准备好了
    private boolean mReady;

    /**
     * 先实现两个参数的构造方法，布局会默认引用这个构造方法， 用一个 构造参数的构造方法来引用这个方法 * @param context
     */

    public AudioRecordButton(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public AudioRecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDialogManager = new DialogManager(getContext());

        // 这里没有判断储存卡是否存在，有空要判断
        /*String dir = getExternalStorageDirectory()
                + "/nickming_recorder_audios";*/
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "nickming_recorder_audios";
        Log.i(TAG, "AudioRecordButton:  dir = " + dir);
        
        mAudioManager = AudioManager.getInstance(dir);
        mAudioManager.setOnAudioStateListener(this);

        setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method
                mReady = true;
                mAudioManager.prepareAudio();
                return false;
            }
        });
        // TODO Auto-generated constructor stub
    }

    /**
     * 录音完成后的回调，回调给activiy，可以获得mtime和文件的路径
     * @author nickming
     *
     */
    public interface AudioFinishRecorderListener{
        void onFinished(float seconds,String filePath);
    }

    private AudioFinishRecorderListener mListener;

    public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener)
    {
        mListener=listener;
    }

    // 获取音量大小的runnable
    private Runnable mGetVoiceLevelRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mhandler.sendEmptyMessage(MSG_VOICE_CHANGE);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    // 准备三个常量
    private static final int MSG_AUDIO_PREPARED = 0X110;
    private static final int MSG_VOICE_CHANGE = 0X111;
    private static final int MSG_DIALOG_DIMISS = 0X112;

    private Handler mhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    // 显示应该是在audio end prepare之后回调
                    mDialogManager.showRecordingDialog();
                    isRecording = true;
                    new Thread(mGetVoiceLevelRunnable).start();

                    // 需要开启一个线程来变换音量
                    break;
                case MSG_VOICE_CHANGE:
                    mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));

                    break;
                case MSG_DIALOG_DIMISS:

                    break;

            }
        };
    };

    // 在这里面发送一个handler的消息
    @Override
    public void wellPrepared() {
        // TODO Auto-generated method stub
        mhandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    /**
     * 直接复写这个监听函数
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:

                if (isRecording) {

                    // 根据x，y来判断用户是否想要取消
                    if (wantToCancel(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                    } else {
                        changeState(STATE_RECORDING);
                    }

                }

                break;
            case MotionEvent.ACTION_UP:
                // 首先判断是否有触发onlongclick事件，没有的话直接返回reset
                if (!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }
                // 如果按的时间太短，还没准备好或者时间录制太短，就离开了，则显示这个dialog
                if (!isRecording || mTime < 0.6f) {
                    mDialogManager.tooShort();
                    mAudioManager.cancel();
                    mhandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);// 持续1.3s
                } else if (mCurrentState == STATE_RECORDING) {//正常录制结束

                    mDialogManager.dimissDialog();

                    mAudioManager.release();// release释放一个mediarecorder

                    if (mListener!=null) {// 并且callbackActivity，保存录音

                        mListener.onFinished(mTime, mAudioManager.getCurrentFilePath());
                    }





                } else if (mCurrentState == STATE_WANT_TO_CANCEL) {
                    // cancel
                    mAudioManager.cancel();
                    mDialogManager.dimissDialog();
                }
                reset();// 恢复标志位

                break;

        }

        return super.onTouchEvent(event);
    }

    /**
     * 回复标志位以及状态
     */
    private void reset() {
        // TODO Auto-generated method stub
        isRecording = false;
        changeState(STATE_NORMAL);
        mReady = false;
        mTime = 0;
    }

    private boolean wantToCancel(int x, int y) {
        // TODO Auto-generated method stub

        if (x < 0 || x > getWidth()) {// 判断是否在左边，右边，上边，下边
            return true;
        }
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
            return true;
        }

        return false;
    }

    /**
     * 改变录音按钮状态
     * @param state
     */
    private void changeState(int state) {
        // TODO Auto-generated method stub
        if (mCurrentState != state) {
            mCurrentState = state;
            switch (mCurrentState) {
                case STATE_NORMAL://正常状态
                    setBackgroundResource(R.drawable.button_recordnormal);
                    setText(R.string.normal);

                    break;
                case STATE_RECORDING://录音状态
                    setBackgroundResource(R.drawable.button_recording);
                    setText(R.string.recording);
                    if (isRecording) {
                        mDialogManager.recording();
                        // 复写dialog.recording();
                    }
                    break;

                case STATE_WANT_TO_CANCEL://取消状态
                    setBackgroundResource(R.drawable.button_recording);
                    setText(R.string.want_to_cancle);
                    // dialog want to cancel
                    mDialogManager.wantToCancel();
                    break;

            }
        }

    }

    @Override
    public boolean onPreDraw() {
        // TODO Auto-generated method stub
        return false;
    }
}
