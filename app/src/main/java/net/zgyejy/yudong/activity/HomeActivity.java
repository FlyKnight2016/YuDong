package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.fragment.ActFragment;
import net.zgyejy.yudong.fragment.ShowFragment;
import net.zgyejy.yudong.fragment.StudyFragment;
import net.zgyejy.yudong.fragment.UserFragment;
import net.zgyejy.yudong.fragment.VideoFragment;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends MyBaseActivity {
    @BindView(R.id.tv_home_show)
    TextView tv_home_show;
    @BindView(R.id.tv_home_study)
    TextView tv_home_study;
    @BindView(R.id.tv_home_video)
    TextView tv_home_video;
    @BindView(R.id.tv_home_act)
    TextView tv_home_act;
    @BindView(R.id.tv_home_user)
    TextView tv_home_user;

    @BindView(R.id.iv_home_show)
    ImageView iv_home_show;
    @BindView(R.id.iv_home_study)
    ImageView iv_home_study;
    @BindView(R.id.iv_home_video)
    ImageView iv_home_video;
    @BindView(R.id.iv_home_act)
    ImageView iv_home_act;
    @BindView(R.id.iv_home_user)
    ImageView iv_home_user;

    @BindColor(R.color.themeColor)
    int themeColor;

    final int defaultColor = 0xFF666666;


    private Fragment actFragment, showFragment, studyFragment,
            userFragment, videoFragment;

    private String isTo;//得到跳转来源想要打开的内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        loadVideoFragment();
        isTo = getIntent().getStringExtra("isTo");
        if (isTo == null) {
            isTo = "51Video";
        }else if (isTo.equals("UserFragment")){
            showUserFragment();
        }else if (isTo.equals("StudyFragment")
                ||isTo.equals("Principal")
                ||isTo.equals("Teacher")
                ||isTo.equals("Parents")
                ||isTo.equals("Kid")) {
            showStudyFragment();
        }else if (isTo.equals("Act")) {
            showActFragment();
        }else if (isTo.equals("Show")) {
            showShowFragment();
        }
    }

    public String getIsTo() {
        return isTo;
    }

    @OnClick({R.id.rl_home_show, R.id.rl_home_study, R.id.rl_home_video,
            R.id.rl_home_act, R.id.rl_home_user})
    void homeOnClick(View view) {
        switch (view.getId()) {
            case R.id.rl_home_show:
                showShowFragment();
                break;
            case R.id.rl_home_study:
                showStudyFragment();
                break;
            case R.id.rl_home_video:
                showVideoFragment();
                break;
            case R.id.rl_home_act:
                showActFragment();
                break;
            case R.id.rl_home_user:
                showUserFragment();
                break;
            default:
                break;
        }
    }

    public int getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(int themeColor) {
        this.themeColor = themeColor;
    }

    public int getDefaultColor() {
        return defaultColor;
    }

    private void loadVideoFragment() {
        videoFragment = new VideoFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.rl_container,
                videoFragment).commit();
        setDefaultColor();
        iv_home_video.setBackgroundResource(R.drawable.video_selected);
        tv_home_video.setTextColor(themeColor);
    }

    private void showShowFragment() {
        if (showFragment == null)
            showFragment = new ShowFragment();
        getSupportFragmentManager().beginTransaction().replace
                (R.id.rl_container, showFragment).commit();
        setDefaultColor();
        iv_home_show.setBackgroundResource(R.drawable.show_selected);
        tv_home_show.setTextColor(themeColor);
    }

    private void showStudyFragment() {
        if (studyFragment == null)
            studyFragment = new StudyFragment();
        getSupportFragmentManager().beginTransaction().replace
                (R.id.rl_container, studyFragment).commit();
        setDefaultColor();
        iv_home_study.setBackgroundResource(R.drawable.study_selected);
        tv_home_study.setTextColor(themeColor);
    }

    private void showVideoFragment() {
        if (videoFragment == null)
            videoFragment = new VideoFragment();
        getSupportFragmentManager().beginTransaction().replace
                (R.id.rl_container, videoFragment).commit();
        setDefaultColor();
        iv_home_video.setBackgroundResource(R.drawable.video_selected);
        tv_home_video.setTextColor(themeColor);
    }

    private void showActFragment() {
        if (actFragment == null)
            actFragment = new ActFragment();
        getSupportFragmentManager().beginTransaction().replace
                (R.id.rl_container, actFragment).commit();
        setDefaultColor();
        iv_home_act.setBackgroundResource(R.drawable.act_selected);
        tv_home_act.setTextColor(themeColor);
    }

    private void showUserFragment() {
        if (userFragment == null)
            userFragment = new UserFragment();
        getSupportFragmentManager().beginTransaction().replace
                (R.id.rl_container, userFragment).commit();
        setDefaultColor();
        iv_home_user.setBackgroundResource(R.drawable.user_selected);
        tv_home_user.setTextColor(themeColor);
    }

    private void setDefaultColor() {
        iv_home_show.setBackgroundResource(R.drawable.show_normal);
        tv_home_show.setTextColor(defaultColor);
        iv_home_study.setBackgroundResource(R.drawable.study_normal);
        tv_home_study.setTextColor(defaultColor);
        iv_home_video.setBackgroundResource(R.drawable.video_normal);
        tv_home_video.setTextColor(defaultColor);
        iv_home_act.setBackgroundResource(R.drawable.act_normal);
        tv_home_act.setTextColor(defaultColor);
        iv_home_user.setBackgroundResource(R.drawable.user_normal);
        tv_home_user.setTextColor(defaultColor);
    }

}
