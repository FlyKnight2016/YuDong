package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.angle;

public class RegisterActivity extends MyBaseActivity {
    private int countDown;//验证码发送倒计时
    @BindColor(R.color.white)
    int colorWhite;
    @BindColor(R.color.dark)
    int colorDark;

    @BindView(R.id.et_register_phoneNumber)
    TextInputEditText etRegisterPhoneNumber;
    @BindView(R.id.et_register_validateCode)
    TextInputEditText etRegisterValidateCode;
    @BindView(R.id.et_register_userPsw)
    TextInputEditText etRegisterUserPsw;
    @BindView(R.id.btn_register_sendMsg)
    Button btnRegisterSendMsg;

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
                sendTime();


                break;
            case R.id.btn_register:
                String phoneNumber = etRegisterPhoneNumber.getText().toString();
                String validateCode = etRegisterValidateCode.getText().toString();
                String userPsw = etRegisterUserPsw.getText().toString();
                break;
        }
    }

    /**
     * 验证码发送计时器
     */
    private void sendTime() {
        countDown = 60;
        setBtnSendMsg(false);
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (countDown == 0) {
                    setBtnSendMsg(true);
                    timer.cancel();
                } else {
                    countDown--;
                    btnRegisterSendMsg.setText(countDown+"s后\n重新发送");
                }
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }

    /**
     * 设置发送验证码按钮
     * @param isClickable
     */
    private void setBtnSendMsg(boolean isClickable) {
        if (isClickable) {
            btnRegisterSendMsg.setClickable(true);
            btnRegisterSendMsg.setText("点击获取\\n验证码");
            btnRegisterSendMsg.setTextColor(colorWhite);
            btnRegisterSendMsg.setBackgroundResource(R.drawable.text_view_back_button);
        }else {
            //倒计时过程
            btnRegisterSendMsg.setClickable(false);
            btnRegisterSendMsg.setText(countDown+"s后\n重新发送");//设置文字显示（根据state的值）
            btnRegisterSendMsg.setTextColor(colorDark);//设置字体为黑色
            btnRegisterSendMsg.setBackgroundResource(R.drawable.text_view_back_button_gray);//设置背景为灰色
        }
    }
}
