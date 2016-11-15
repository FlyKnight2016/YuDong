package net.zgyejy.yudong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.modle.BaseEntity;
import net.zgyejy.yudong.modle.parser.ParserBaseEntity;
import net.zgyejy.yudong.util.CommonUtil;
import net.zgyejy.yudong.util.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class RegisterActivity extends MyBaseActivity {


    private int flag;//判断是注册还是修改密码（0为注册，1为修改密码）
    private RequestQueue requestQueue;//volley接口对象
    private int countDown;//验证码发送倒计时

    private String phone;//手机号
    private String password;//密码
    private String phoneCode;//验证码

    @BindColor(R.color.white)
    int colorWhite;
    @BindColor(R.color.dark)
    int colorDark;

    @BindView(R.id.tv_register_title)
    TextView tvRegisterTitle;
    @BindView(R.id.et_register_phoneNumber)
    TextInputEditText etRegisterPhoneNumber;
    @BindView(R.id.et_register_validateCode)
    TextInputEditText etRegisterValidateCode;
    @BindView(R.id.et_register_userPsw)
    TextInputEditText etRegisterUserPsw;
    @BindView(R.id.et_register_userPsw2)
    TextInputEditText etRegisterUserPsw2;
    @BindView(R.id.btn_register_sendMsg)
    Button btnRegisterSendMsg;
    @BindView(R.id.btn_register)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        flag = getIntent().getIntExtra("rOrF", 0);//得到Login传过来的目的（0为注册，1为找回密码）
        initView();
    }

    private void initView() {
        if (flag == 1) {
            tvRegisterTitle.setText("找回密码");
            btnRegister.setText("修改密码");
        }
    }

    @OnClick({R.id.tv_register_back, R.id.btn_register_sendMsg, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register_back:
                finish();
                break;
            case R.id.btn_register_sendMsg:

                sendTime();//开始下次发送计时

                phone = etRegisterPhoneNumber.getText().toString();//获取填入的电话号码

                if (phone == null || phone.length() == 0) {
                    showToast("手机号码不能为空！");
                } else if (phone.length() != 11) {
                    showToast("请填入正确的手机号码！");
                } else if (!CommonUtil.isNetworkAvailable(this)) {
                    showToast("当前无网络连接，请连接网络!");
                } else {//开始发送网络请求
                    if (requestQueue == null)
                        requestQueue = VolleySingleton.getVolleySingleton(this).getRequestQueue();
                    showLoadingDialog(this, "正在处理数据...", true);//显示加载动画
                    String urlGetCode = API.REGISTER_GET_PHONE_CODE + phone;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlGetCode,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    BaseEntity<List> baseEntity = ParserBaseEntity.
                                            getBaseEntityList(jsonObject.toString());
                                    if (200 == baseEntity.getCode()) {
                                        showToast(baseEntity.getMessage());
                                    } else {
                                        showToast(baseEntity.getMessage() + "，手机号码输入错误，或当日获取验证码次数超过5次");
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
                break;
            case R.id.btn_register:

                //获取输入框中内容
                phone = etRegisterPhoneNumber.getText().toString();
                phoneCode = etRegisterValidateCode.getText().toString();
                password = etRegisterUserPsw.getText().toString();
                String password2 = etRegisterUserPsw2.getText().toString();

                //进行判断
                if (null == phone || phone.length() == 0) {
                    showToast("手机号码不能为空！");
                } else if (phone.length() != 11) {
                    showToast("请填入正确的手机号码！");
                } else if (null == phoneCode || phoneCode.length() == 0) {
                    showToast("验证码不能为空！");
                } else if (phoneCode.length() != 4) {
                    showToast("请输入正确的验证码！");
                } else if (null == password || password.length() == 0) {
                    showToast("密码不能为空！");
                } else if (password.length() < 6 || password.length() > 23) {
                    showToast("密码长度必须在6-23之间");
                } else if (!password.equals(password2)) {
                    showToast("两次输入的密码必须相同！");
                } else {
                    if (requestQueue == null)
                        requestQueue = VolleySingleton.getVolleySingleton(this).getRequestQueue();
                    String urlRegister;
                    if (flag == 0) {
                        urlRegister = API.REGISTER_POST_REGISTER;
                    }else {
                        urlRegister = API.REGISTER_POST_EDITPASSWORD;
                    }
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlRegister,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    BaseEntity<List> baseEntity = ParserBaseEntity.getBaseEntityList(response);
                                    if (baseEntity.getCode() == 200) {
                                        showToast(baseEntity.getMessage());

                                        //将注册或修改成功的用户名和密码传给登录界面
                                        Intent resultIntent = new Intent();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("phone", phone);
                                        bundle.putString("password", password);
                                        resultIntent.putExtras(bundle);
                                        setResult(RESULT_OK, resultIntent);
                                        finish();

                                    } else {
                                        showToast(baseEntity.getMessage());
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            //在这里设置需要post的参数
                            Map<String, String> params = new HashMap();
                            params.put("phone", phone);
                            params.put("password", password);
                            params.put("phoneCode", phoneCode);
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setBtnSendMsg(true);
                        }
                    });
                    timer.cancel();
                } else {
                    countDown--;
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    btnRegisterSendMsg.setText(countDown + "s后\n重新发送");
                                }
                            }
                    );

                }
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }

    /**
     * 设置发送验证码按钮
     *
     * @param isClickable
     */
    private void setBtnSendMsg(boolean isClickable) {
        if (isClickable) {
            btnRegisterSendMsg.setClickable(true);
            btnRegisterSendMsg.setText("点击获取\n验证码");
            btnRegisterSendMsg.setTextColor(colorWhite);
            btnRegisterSendMsg.setBackgroundResource(R.drawable.text_view_back_button);
        } else {
            //倒计时过程
            btnRegisterSendMsg.setClickable(false);
            btnRegisterSendMsg.setText(countDown + "s后\n重新发送");//设置文字显示（根据state的值）
            btnRegisterSendMsg.setTextColor(colorDark);//设置字体为黑色
            btnRegisterSendMsg.setBackgroundResource(R.drawable.text_view_back_button_gray);//设置背景为灰色
        }
    }
}
