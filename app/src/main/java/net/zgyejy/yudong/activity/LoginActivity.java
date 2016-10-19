package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends MyBaseActivity {

    @BindView(R.id.et_userName)
    TextInputEditText etUserName;
    @BindView(R.id.et_userPsw)
    TextInputEditText etUserPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_login_back, R.id.tv_login_register, R.id.btn_login, R.id.tv_findPsw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_back:
                openActivity(HomeActivity.class);
                finish();
                break;
            case R.id.tv_login_register:
                openActivity(RegisterActivity.class);
                finish();
                break;
            case R.id.btn_login:
                String userName = etUserName.getText().toString();
                String userPsw = etUserPsw.getText().toString();
                break;
            case R.id.tv_findPsw:
                break;
        }
    }
}
