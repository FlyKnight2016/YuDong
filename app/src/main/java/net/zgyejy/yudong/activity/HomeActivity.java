package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import net.zgyejy.yudong.R;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.tv_home_show) TextView tv_home_show;
    @BindView(R.id.tv_home_study) TextView tv_home_study;
    @BindView(R.id.tv_home_video) TextView tv_home_video;
    @BindView(R.id.tv_home_act) TextView tv_home_act;
    @BindView(R.id.tv_home_user) TextView tv_home_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @OnClick({R.id.tv_home_show,R.id.tv_home_study,R.id.tv_home_video,
            R.id.tv_home_act,R.id.tv_home_user})
    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home_show:
                break;
            case R.id.tv_home_study:
                break;
            case R.id.tv_home_video:
                break;
            case R.id.tv_home_act:
                break;
            case R.id.tv_home_user:
                break;
            default:
                break;
        }
    }
}
