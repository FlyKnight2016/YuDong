package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends MyBaseActivity {

    @BindView(R.id.et_register_phoneNumber)
    TextInputEditText etRegisterPhoneNumber;
    @BindView(R.id.et_register_validateCode)
    TextInputEditText etRegisterValidateCode;
    @BindView(R.id.et_register_userPsw)
    TextInputEditText etRegisterUserPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_register_back, R.id.btn_register_sendMsg, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register_back:
                openActivity(LoginActivity.class);
                finish();
                break;
            case R.id.btn_register_sendMsg:
                break;
            case R.id.btn_register:
                String phoneNumber = etRegisterPhoneNumber.getText().toString();
                String validateCode = etRegisterValidateCode.getText().toString();
                String userPsw = etRegisterUserPsw.getText().toString();
                break;
        }
    }
}
