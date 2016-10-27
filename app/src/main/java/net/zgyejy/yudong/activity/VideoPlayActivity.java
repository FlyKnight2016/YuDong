package net.zgyejy.yudong.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.CommentListAdapter;
import net.zgyejy.yudong.base.MyBaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoPlayActivity extends MyBaseActivity {
    private List<View> otherViews;
    private MediaController mMediaController;
    private boolean fullscreen = false;
    private String path = "http://baobab.wdjcdn.com/145076769089714.mp4";
    private CommentListAdapter commentListAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        addOtherViews();

        matchViewToOrientation();

        mVideoView.setVideoURI(Uri.parse(path));//设置播放地址
        mMediaController = new MediaController(this);//实例化控制器
        mMediaController.show(5000);//控制器显示5s后自动隐藏
        mVideoView.setMediaController(mMediaController);//绑定控制器
        mVideoView.requestFocus();//取得焦点
        mVideoView.start();

        initListData();
    }

    /**
     * 初始化评论列表数据
     */
    private void initListData() {
        if (commentListAdapter == null)
            commentListAdapter = new CommentListAdapter(this);
        lvPlayComments.setAdapter(commentListAdapter);
    }

    /**
     * 刷新评论列表数据
     */
    private void refreshListData() {

    }

    /**
     * 当屏幕方向改变时，设置相应布局
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
    private void addOtherViews() {
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
        for (int i=0; i<3;i++) {
            otherViews.get(i).setVisibility(View.GONE);
        }
    }

    /**
     * 显示所有非播放View
     */
    private void setOtherViewsVisible() {
        for (int i=0; i<3;i++) {
            otherViews.get(i).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 方向改变时，隐藏或显示其他布局，并设置全屏参数
     */
    private void changeVideoView() {
        if(!fullscreen){//设置RelativeLayout的全屏模式
            setOtherViewsGone();
            fullscreen = true;//改变全屏/窗口的标记
        }else{//设置RelativeLayout的窗口模式
            setOtherViewsVisible();
            fullscreen = false;//改变全屏/窗口的标记
        }
    }

    /**
     * 根据当前屏幕的方向确定是否显示其他布局，并设置是否全屏的参数
     */
    private void matchViewToOrientation() {
        Configuration newConfig = getResources().getConfiguration();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            //横屏
            setOtherViewsGone();
            fullscreen = true;

        }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
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
}
