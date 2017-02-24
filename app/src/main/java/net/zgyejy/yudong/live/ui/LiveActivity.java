package net.zgyejy.yudong.live.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import net.zgyejy.yudong.R;
import net.zgyejy.yudong.activity.HomeActivity;
import net.zgyejy.yudong.activity.LoginActivity;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.live.LiveKit;
import net.zgyejy.yudong.live.controller.ChatListAdapter;
import net.zgyejy.yudong.live.controller.RcLog;
import net.zgyejy.yudong.live.ui.message.GiftMessage;
import net.zgyejy.yudong.live.ui.widget.AudioRecordButton;
import net.zgyejy.yudong.live.ui.widget.ChatListView;
import net.zgyejy.yudong.modle.BaseEntity;
import net.zgyejy.yudong.modle.LiveInfo;
import net.zgyejy.yudong.modle.Mp3Text;
import net.zgyejy.yudong.modle.Token;
import net.zgyejy.yudong.modle.parser.ParserBaseEntity;
import net.zgyejy.yudong.modle.parser.ParserCourseInfo;
import net.zgyejy.yudong.modle.parser.ParserLiveInfo;
import net.zgyejy.yudong.util.CommonUtil;
import net.zgyejy.yudong.util.SharedUtil;
import net.zgyejy.yudong.util.VolleySingleton;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import static android.R.attr.button;
import static io.rong.imlib.statistics.UserData.phone;
import static io.vov.vitamio.MediaPlayer.MEDIA_INFO_BUFFERING_END;
import static io.vov.vitamio.MediaPlayer.MEDIA_INFO_BUFFERING_START;
import static io.vov.vitamio.MediaPlayer.VIDEOQUALITY_LOW;
import static net.zgyejy.yudong.modle.parser.ParserBaseEntity.getBaseEntityOfToken;

/**
 * 直播界面
 */
public class LiveActivity extends MyBaseActivity implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        Handler.Callback{
    private static final String TAG = "LiveActivity";
    private String token;//用户令牌
    private String path;//直播地址
    private VideoView mVideoView;//视频播放器
    private MediaController mMediaController;//播放控制器
    private List<View> otherViews;//其他View
    private View rl_title, ll_chatRoom;//标题和聊天
    private boolean fullscreen = false;//是否全屏的标记
    private String title;//直播标题

    //缓冲View
    private View coverView, loadingView;

    //声音和亮度调整界面
    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private AudioManager mAudioManager;

    private PopupWindow popupWindow;//菜单
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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    /**
     * 聊天室部分
     */
    private ChatListView chat_listview;//聊天信息列表
    private ImageButton btn_yuyin, btn_wenzi, btn_zan, btn_jushou;//切换语音，切换文字，点赞，举手
    private EditText et_chatContent;//文本输入框
    private AudioRecordButton btn_sendYuyin;//发送语音（按下录音）
    private Button btn_sendMsg;//发送消息
    private ChatListAdapter chatListAdapter;
    private Handler handler = new Handler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(this);
        setContentView(R.layout.activity_live);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置初始屏幕方向

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        token = SharedUtil.getToken(this);

        LiveKit.addEventHandler(handler);
        initView();

        //设置视频准备完成、播放出错、播放完成的监听
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnCompletionListener(this);

        matchViewToOrientation();//匹配屏幕方向和相应标记

        getPathAndTitle();

        chatServerLogin();//登录聊天室
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mGestureDetector != null && mGestureDetector.onTouchEvent(event))
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

    private void setLivePlay() {
        mMediaController = new MediaController(this);//实例化控制器
        /*mMediaController.setClickIsFullScreenListener(this);*/
        mMediaController.show(3000);//控制器显示3s后自动隐藏
        mVideoView.setMediaController(mMediaController);//绑定控制器
        mVideoView.setVideoURI(Uri.parse(path));//设置播放地址
        mVideoView.setVideoQuality(VIDEOQUALITY_LOW);//设置视频质量
        mVideoView.setBufferSize(512 * 1024);

        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MEDIA_INFO_BUFFERING_START:
                        //开始缓存，暂停播放
                        if (mVideoView.isPlaying()) {
                            mVideoView.pause();
                            needResume = true;
                        }
                        loadingView.setVisibility(View.VISIBLE);
                        break;
                    case MEDIA_INFO_BUFFERING_END:
                        //缓存完成，继续播放
                        if (needResume) {
                            mVideoView.start();
                            needResume = false;
                        }
                        loadingView.setVisibility(View.GONE);
                        break;
                }
                return true;
            }
        });

        mVideoView.requestFocus();//取得焦点

        mGestureDetector = new GestureDetector(this, new MyGestureListener());//设置手势监听，可滑动改变声音大小和亮度
    }

    /**
     * 是否需要自动恢复播放，用于自动暂停，恢复播放
     */
    private boolean needResume;

    /**
     * 初始化各View
     */
    private void initView() {
        mVideoView = (VideoView) findViewById(R.id.liveVideoView);
        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);

        rl_title = findViewById(R.id.rl_title);
        ll_chatRoom = findViewById(R.id.ll_chatRoom);
        addOtherViewsToList();

        coverView = findViewById(R.id.CoverView);
        loadingView = findViewById(R.id.LoadingView);

        //获取系统最大音量
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //聊天系统相关界面
        chat_listview = (ChatListView) findViewById(R.id.chat_listview);
        btn_yuyin = (ImageButton) findViewById(R.id.btn_yuyin);
        btn_wenzi = (ImageButton) findViewById(R.id.btn_wenzi);
        et_chatContent = (EditText) findViewById(R.id.et_chatContent);
        setTextChanged();//输入框内容改变的监听
        btn_sendYuyin = (AudioRecordButton) findViewById(R.id.btn_sendYuyin);
        btn_sendYuyin.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinished(float seconds, String filePath) {
                //录音完毕后...
                Log.i(TAG, "onFinished: 录音完毕，共" + seconds + "秒，存储路径：" + filePath);
            }
        });
        btn_zan = (ImageButton) findViewById(R.id.btn_zan);
        btn_jushou = (ImageButton) findViewById(R.id.btn_jushou);
        btn_sendMsg = (Button) findViewById(R.id.btn_sendMsg);
        chatListAdapter = new ChatListAdapter();
        chat_listview.setAdapter(chatListAdapter);
    }

    @Override
    public boolean handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case LiveKit.MESSAGE_ARRIVED: {
                Log.i(TAG, "handleMessage: ====================收到了消息！");
                MessageContent content = (MessageContent) msg.obj;
                chatListAdapter.addMessage(content);
                break;
            }
            case LiveKit.MESSAGE_SENT: {
                Log.i(TAG, "handleMessage=========================:发送了消息！ ");
                MessageContent content = (MessageContent) msg.obj;
                chatListAdapter.addMessage(content);
                break;
            }
            case LiveKit.MESSAGE_SEND_ERROR: {
                break;
            }
            default:
        }
        chatListAdapter.notifyDataSetChanged();
        return false;
    }

    /**
     * 设置输入框文字内容改变的监听
     */
    private void setTextChanged() {
        et_chatContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0) {
                    btn_sendMsg.setVisibility(View.GONE);
                    btn_zan.setVisibility(View.VISIBLE);
                    btn_jushou.setVisibility(View.VISIBLE);
                }else {
                    btn_sendMsg.setVisibility(View.VISIBLE);
                    btn_zan.setVisibility(View.GONE);
                    btn_jushou.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 添加非播放View到otherViews集合中
     */
    private void addOtherViewsToList() {
        if (otherViews == null)
            otherViews = new ArrayList<>();
        otherViews.clear();
        otherViews.add(rl_title);
        otherViews.add(ll_chatRoom);
    }

    /**
     * 隐藏所有非播放View
     */
    private void setOtherViewsGone() {
        for (int i = 0; i < otherViews.size(); i++) {
            otherViews.get(i).setVisibility(View.GONE);
        }
    }

    /**
     * 显示所有非播放View
     */
    private void setOtherViewsVisible() {
        for (int i = 0; i < otherViews.size(); i++) {
            otherViews.get(i).setVisibility(View.VISIBLE);
        }
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
     * 方向改变时，隐藏或显示其他布局，并设置全屏参数
     */
    private void changeVideoView() {
        if (!fullscreen) {//设置RelativeLayout的全屏模式
            setOtherViewsGone();
            mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
            fullscreen = true;//改变全屏/窗口的标记
        } else {//设置RelativeLayout的窗口模式
            setOtherViewsVisible();
            mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
            fullscreen = false;//改变全屏/窗口的标记
        }
    }

    private RequestQueue requestQueue;//volley接口对象

    /**
     * 得到直播详细信息
     */
    private void getPathAndTitle() {
        if (!CommonUtil.isNetworkAvailable(this)) {
            showToast("当前无网络连接，请连接网络!");
        } else {
            if (requestQueue == null)
                //实例化一个RequestQueue对象
                requestQueue = VolleySingleton.getVolleySingleton(this).getRequestQueue();
            showLoadingDialog(this, "数据正在加载...", true);//显示加载动画
            String urlLive = API.APP_GET_LIVEING;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlLive,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            BaseEntity<LiveInfo> baseEntity = ParserLiveInfo.getBaseEntity(jsonObject.toString());
                            if (baseEntity.getCode() == 300) {
                                showToast("当前无直播！");
                                loadingView.setVisibility(View.GONE);
                            } else if (baseEntity.getCode() == 200) {
                                path = baseEntity.getData().getPull_url();//获取拉流地址
                                setLivePlay();//设置播放数据并开始播放
                            } else {
                                showToast("未知错误！");
                                loadingView.setVisibility(View.GONE);
                            }
                            cancelDialog();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                        }
                    }
            );
            requestQueue.add(jsonObjectRequest);
        }
        /*path = "rtmp://live.hkstv.hk.lxdns.com/live/hks";*/
        /*path = "rtmp://pili-live-rtmp.www.zgyejy.net/yudong/php-sdk-test1483065824";*/
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        showToast("Error");
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setPlaybackSpeed(1.0f);
        coverView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        mVideoView.start();
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Live Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @OnClick({R.id.iv_live_back, R.id.btn_yuyin, R.id.btn_wenzi, R.id.btn_zan, R.id.btn_jushou, R.id.btn_sendMsg})
    public void onClick(View view) {
        LiveKit.setCurrentUser(SharedUtil.getUserInfo(getBaseContext()));//设置用户信息
        switch (view.getId()) {
            case R.id.iv_live_back:
                //关闭当前页
                finish();
                break;
            case R.id.btn_yuyin:
                //切换语音聊天
                btn_yuyin.setVisibility(View.GONE);
                btn_wenzi.setVisibility(View.VISIBLE);
                et_chatContent.setVisibility(View.GONE);
                btn_sendYuyin.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_wenzi:
                //切换文字聊天
                btn_yuyin.setVisibility(View.VISIBLE);
                btn_wenzi.setVisibility(View.GONE);
                et_chatContent.setVisibility(View.VISIBLE);
                btn_sendYuyin.setVisibility(View.GONE);
                break;
            case R.id.btn_zan:
                //点赞
                GiftMessage msg1 = new GiftMessage("1", "为您点赞");//创建点赞消息
                LiveKit.sendMessage(msg1);//发送消息
                break;
            case R.id.btn_jushou:
                //举手
                GiftMessage msg2 = new GiftMessage("2", "举手");//创建举手消息
                LiveKit.sendMessage(msg2);//发送消息
                break;
            case R.id.btn_sendMsg:
                //发送文字消息
                final TextMessage textMsg = TextMessage.obtain(et_chatContent.getText().toString());//创建文字消息
                LiveKit.sendMessage(textMsg);//发送消息
                et_chatContent.getText().clear();
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
                /*mMediaController.setFullScreenButton(R.drawable.esc_full_screen);*/
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                /*mMediaController.setFullScreenButton(R.drawable.full_screen);*/
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
     * 聊天室登录
     */
    private void chatServerLogin() {
        //获取融云token
        if (requestQueue == null)
            requestQueue = VolleySingleton.getVolleySingleton(this).getRequestQueue();
        String getRongyunTokenUrl = API.APP_GET_RONGYUNTOKEN + "token=" + token;
        Log.i(TAG, "chatServerLogin: ++++++++++++++++++" + getRongyunTokenUrl);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getRongyunTokenUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        BaseEntity<Token> baseEntity = ParserBaseEntity.getBaseEntityOfToken(jsonObject.toString());
                        if (baseEntity.getCode()==200) {
                            showToast(baseEntity.getMessage());
                            String rongyunToken = baseEntity.getData().getToken();
                            LiveKit.connect(rongyunToken, new RongIMClient.ConnectCallback() {
                                @Override
                                public void onTokenIncorrect() {
                                    RcLog.d(TAG, "传入token错误");
                                }

                                @Override
                                public void onSuccess(String userId) {
                                    RcLog.d(TAG, "连接融云服务器成功！");
                                    chatRoomLogin();
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    RcLog.d(TAG, "连接融云服务器错误：" + errorCode);
                                    // 根据errorCode 检查原因.
                                }
                            });
                        }else {
                            showToast("连接聊天服务器失败！");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void chatRoomLogin() {
        if (requestQueue == null)
            requestQueue = VolleySingleton.getVolleySingleton(this).getRequestQueue();
        String joinChatRoomUrl = API.APP_GET_JOINCHATROOM + "token=" + token;
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(
                Request.Method.GET,
                joinChatRoomUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        BaseEntity<List> baseEntity = ParserBaseEntity.getBaseEntityList(jsonObject.toString());
                        if (baseEntity.getCode()==200) {
                            showToast(baseEntity.getMessage());
                            Log.i(TAG, "onResponse: 加入聊天时成功！");
                        }else {
                            showToast("加入聊天室失败！");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }
        );
        requestQueue.add(jsonObjectRequest2);
    }

    @Override
    protected void onDestroy() {
        LiveKit.logout();//断开与服务器连接
        LiveKit.removeEventHandler(handler);
        super.onDestroy();
    }
}
