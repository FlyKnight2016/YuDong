package net.zgyejy.yudong.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.CommentListAdapter;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.modle.Comment;
import net.zgyejy.yudong.modle.Video;
import net.zgyejy.yudong.util.MediaController;
import net.zgyejy.yudong.view.VideoView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class VideoPlayActivity extends MyBaseActivity implements
        net.zgyejy.yudong.util.MediaController.onClickIsFullScreenListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener{
    private List<View> otherViews;
    private MediaController mMediaController;
    private boolean fullscreen = false;
    private String path;
    private CommentListAdapter commentListAdapter;
    private Video video;
    private List<Comment> comments;

    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private AudioManager mAudioManager;
    /** 最大声音 */
    private int mMaxVolume;
    /** 当前声音 */
    private int mVolume = -1;
    /** 当前亮度 */
    private float mBrightness = -1f;
    /** 当前缩放模式 */
    //private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
    private GestureDetector mGestureDetector;

    @BindView(R.id.tv_play_title)
    TextView tvPlayTitle;
    @BindView(R.id.ll_play_title)
    RelativeLayout llPlayTitle;
    @BindView(R.id.videoView)
    VideoView mVideoView;
    @BindView(R.id.tv_play_browse)
    TextView tvPlayBrowse;
    @BindView(R.id.iv_play_praise)
    ImageView ivPlayPraise;
    @BindView(R.id.tv_play_praiseNum)
    TextView tvPlayPraiseNum;
    @BindView(R.id.iv_play_collect)
    ImageView ivPlayCollect;
    @BindView(R.id.tv_play_collectNum)
    TextView tvPlayCollectNum;
    @BindView(R.id.ll_play_videoAbout)
    LinearLayout llPlayVideoAbout;
    @BindView(R.id.iv_play_userPortrait)
    ImageView ivPlayUserPortrait;
    @BindView(R.id.et_play_commentContent)
    EditText etPlayCommentContent;
    @BindView(R.id.lv_play_comments)
    ListView lvPlayComments;
    @BindView(R.id.ll_play_commentAbout)
    LinearLayout llPlayCommentAbout;
    @BindView(R.id.tv_play_noComment)
    TextView tvPlayNoComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        getPath();

        addOtherViewsToList();

        matchViewToOrientation();

        mMediaController = new MediaController(this);//实例化控制器
        mMediaController.setClickIsFullScreenListener(this);
        mMediaController.show(5000);//控制器显示5s后自动隐藏

        mVideoView.setMediaController(mMediaController);//绑定控制器
        mVideoView.setVideoURI(Uri.parse(path));//设置播放地址
        mVideoView.requestFocus();//取得焦点


        //加载评论列表数据
        initListData();
    }

    /**
     * 得到视频资源地址
     */
    private void getPath() {
        Bundle bundle = getIntent().getExtras();
        video = (Video)bundle.getSerializable("video");
        if (video!=null)
            path = API.APP_SERVER_IP + video.getVideo_url();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //全屏时滑动屏幕调节亮度和音量
        if (fullscreen) {

        }
        return super.onTouchEvent(event);
    }

    /**
     * 初始化评论列表数据
     */
    private void initListData() {
        if (commentListAdapter == null)
            commentListAdapter = new CommentListAdapter(this);
        lvPlayComments.setAdapter(commentListAdapter);

        //加载数据
        refreshListData();
    }

    /**
     * 根据是否有评论，显示相应提示
     */
    private void showHint() {
        if (comments == null||comments.size() == 0) {
            lvPlayComments.setVisibility(View.GONE);
            tvPlayNoComment.setVisibility(View.VISIBLE);
        }else {
            lvPlayComments.setVisibility(View.VISIBLE);
            tvPlayNoComment.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新评论列表数据
     */
    private void refreshListData() {
        //发送请求，得到返回数据

        commentListAdapter.appendDataed(comments,true);
        commentListAdapter.updateAdapter();

        showHint();
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
        otherViews.add(llPlayVideoAbout);
        otherViews.add(llPlayCommentAbout);
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

    @OnClick({R.id.iv_play_back, R.id.iv_play_praise, R.id.tv_play_praiseNum,
            R.id.iv_play_collect, R.id.tv_play_collectNum, R.id.iv_play_menu,
            R.id.btn_play_sendComment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_play_back:
                //关闭当前播放页面
                finish();
                break;
            case R.id.iv_play_praise:
            case R.id.tv_play_praiseNum:
                //赞和取消赞的方法
                break;
            case R.id.iv_play_collect:
            case R.id.tv_play_collectNum:
                //收藏和取消收藏的方法
                break;
            case R.id.iv_play_menu:
                //点击弹出菜单的方法
                break;
            case R.id.btn_play_sendComment:
                //获取品论内容，发送评论的方法
                refreshListData();
                break;
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
        showToast("准备好了");
        mVideoView.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        showToast("Error");
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        showToast("播放完成");
    }
}
