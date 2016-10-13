package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.fragment.ActFragment;
import net.zgyejy.yudong.fragment.ShowFragment;
import net.zgyejy.yudong.fragment.StudyFragment;
import net.zgyejy.yudong.fragment.UserFragment;
import net.zgyejy.yudong.fragment.VideoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.tv_home_show) TextView tv_home_show;
    @BindView(R.id.tv_home_study) TextView tv_home_study;
    @BindView(R.id.tv_home_video) TextView tv_home_video;
    @BindView(R.id.tv_home_act) TextView tv_home_act;
    @BindView(R.id.tv_home_user) TextView tv_home_user;
    private Fragment actFragment,showFragment,studyFragment,
            userFragment,videoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        loadVideoFragment();
    }

    @OnClick({R.id.tv_home_show,R.id.tv_home_study,R.id.tv_home_video,
            R.id.tv_home_act,R.id.tv_home_user})
    void homeOnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home_show:
                showShowFragment();
                break;
            case R.id.tv_home_study:
                showStudyFragment();
                break;
            case R.id.tv_home_video:
                showVideoFragment();
                break;
            case R.id.tv_home_act:
                showActFragment();
                break;
            case R.id.tv_home_user:
                showUserFragment();
                break;
            default:
                break;
        }
    }



    private void loadVideoFragment (){
        videoFragment = new VideoFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.rl_container,videoFragment).commit();
    }

    private void showShowFragment() {
        if (showFragment == null)
            showFragment = new ShowFragment();
        getSupportFragmentManager().beginTransaction().replace
                (R.id.rl_container,showFragment).commit();
    }

    private void showStudyFragment() {
        if (studyFragment == null)
            studyFragment = new StudyFragment();
        getSupportFragmentManager().beginTransaction().replace
                (R.id.rl_container,studyFragment).commit();
    }

    private void showVideoFragment() {
        if (videoFragment == null)
            videoFragment = new VideoFragment();
        getSupportFragmentManager().beginTransaction().replace
                (R.id.rl_container,videoFragment).commit();
    }

    private void showActFragment() {
        if (actFragment == null)
            actFragment = new ActFragment();
        getSupportFragmentManager().beginTransaction().replace
                (R.id.rl_container,actFragment).commit();
    }

    private void showUserFragment() {
        if (userFragment == null)
            userFragment = new UserFragment();
        getSupportFragmentManager().beginTransaction().replace
                (R.id.rl_container,userFragment).commit();
    }

}
