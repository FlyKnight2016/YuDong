package net.zgyejy.yudong.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.bean.VideoIntegral;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.util.MediaController;
import net.zgyejy.yudong.view.VideoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Video51TrialPlayActivity extends MyBaseActivity implements
        MediaController.onClickIsFullScreenListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    private VideoIntegral videoIntegral;
    private List<View> otherViews;
    private MediaController mMediaController;
    private boolean fullscreen = false;
    private String path;//视频网络地址
    private String title;//视频标题
    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private AudioManager mAudioManager;
    /**
     * 最大声音
     */
    private int mMaxVolume;
    /**
     * 当前声音
     */
    private int mVolume = -1;
    /**
     * 当前亮度
     */
    private float mBrightness = -1f;
    /**
     * 当前缩放模式
     */
    //private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
    private GestureDetector mGestureDetector;

    @BindView(R.id.tv_play_trial_title)
    TextView tvPlayTitle;
    @BindView(R.id.ll_play_trial_title)
    RelativeLayout llPlayTitle;
    @BindView(R.id.videoViewTrial)
    VideoView mVideoView;
    @BindView(R.id.sv_videoTrial_describe)
    ScrollView svVideoTrialDescribe;
    @BindView(R.id.btn_askFor_trial)
    TextView btnAskForTrial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video51_trial_play);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        getPathAndTitle();

        showLoadingDialog(this, "视频正在准备中\n请稍等...", true);//显示加载动画
        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness_trial);
        mOperationBg = (ImageView) findViewById(R.id.operation_bg_trial);
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent_trial);

        //获取系统最大音量
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //设置视频准备完成、播放出错、播放完成的监听
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnCompletionListener(this);

        addOtherViewsToList();

        matchViewToOrientation();

        mMediaController = new MediaController(this);//实例化控制器
        mMediaController.setClickIsFullScreenListener(this);
        mMediaController.show(5000);//控制器显示5s后自动隐藏

        mVideoView.setMediaController(mMediaController);//绑定控制器
        mVideoView.setVideoURI(Uri.parse(path));//设置播放地址
        mVideoView.requestFocus();//取得焦点

        mGestureDetector = new GestureDetector(this, new MyGestureListener());
    }

    /**
     * 得到视频资源地址
     */
    private void getPathAndTitle() {
        Bundle bundle = getIntent().getExtras();
        videoIntegral = (VideoIntegral) bundle.getSerializable("videoIntegral");
        if (videoIntegral != null) {
            path = API.APP_SERVER_IP + videoIntegral.getVideo_url();
            String video_name = videoIntegral.getVideo_name();
            title = video_name.substring(video_name.indexOf("课 ") + 1, video_name.length());
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mGestureDetector.onTouchEvent(event))
            return true;

        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 手势结束
     */
    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;

        // 隐藏
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    /**
     * 定时隐藏
     */
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mVolumeBrightnessLayout.setVisibility(View.GONE);
        }
    };

    @OnClick({R.id.iv_play_trial_back, R.id.btn_askFor_trial})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_play_trial_back:
                finish();
                break;
            case R.id.btn_askFor_trial:
                String url = "http://m1.rabbitpre.com/m/jamaqyd?lc=2&sui=ZPjIthQ2#from=share";
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                openActivity(WebReadActivity.class, bundle);
                break;
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        /**
         * 双击
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (!fullscreen) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mMediaController.setFullScreenButton(R.drawable.esc_full_screen);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mMediaController.setFullScreenButton(R.drawable.full_screen);
            }
            return true;
        }

        /**
         * 滑动
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            Display disp = getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();

            if (mOldX > windowWidth * 4.0 / 5)// 右边滑动
                onVolumeSlide((mOldY - y) / windowHeight);
            else if (mOldX < windowWidth / 5.0)// 左边滑动
                onBrightnessSlide((mOldY - y) / windowHeight);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            // 显示
            mOperationBg.setImageResource(R.drawable.video_volume_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        // 变更进度条
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width
                * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            // 显示
            mOperationBg.setImageResource(R.drawable.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);

        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
        mOperationPercent.setLayoutParams(lp);
    }

    /**
     * 当屏幕方向改变时，设置相应布局
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        changeVideoView();
    }

    /**
     * 添加非播放View到otherViews集合中
     */
    private void addOtherViewsToList() {
        if (otherViews == null)
            otherViews = new ArrayList<>();
        otherViews.clear();
        otherViews.add(llPlayTitle);
        otherViews.add(svVideoTrialDescribe);
        otherViews.add(btnAskForTrial);
    }

    /**
     * 隐藏所有非播放View
     */
    private void setOtherViewsGone() {
        for (int i = 0; i < 3; i++) {
            otherViews.get(i).setVisibility(View.GONE);
        }
    }

    /**
     * 显示所有非播放View
     */
    private void setOtherViewsVisible() {
        for (int i = 0; i < 3; i++) {
            otherViews.get(i).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 方向改变时，隐藏或显示其他布局，并设置全屏参数
     */
    private void changeVideoView() {
        if (!fullscreen) {//设置RelativeLayout的全屏模式
            setOtherViewsGone();
            fullscreen = true;//改变全屏/窗口的标记
        } else {//设置RelativeLayout的窗口模式
            setOtherViewsVisible();
            fullscreen = false;//改变全屏/窗口的标记
        }
    }

    /**
     * 根据当前屏幕的方向确定是否显示其他布局，并设置是否全屏的参数
     */
    private void matchViewToOrientation() {
        Configuration newConfig = getResources().getConfiguration();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            setOtherViewsGone();
            fullscreen = true;

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            setOtherViewsVisible();
            fullscreen = false;
        }
    }

    @Override
    public void setOnClickIsFullScreen() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mMediaController.setFullScreenButton(R.drawable.esc_full_screen);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mMediaController.setFullScreenButton(R.drawable.full_screen);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        showToast("视频播放完成");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        showToast("Error");
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        cancelDialog();
        showToast("视频准备好了");
        tvPlayTitle.setText(title);
        mVideoView.start();
    }
}
