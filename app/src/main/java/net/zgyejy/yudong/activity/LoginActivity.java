package net.zgyejy.yudong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

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
import net.zgyejy.yudong.modle.Token;
import net.zgyejy.yudong.modle.parser.ParserBaseEntity;
import net.zgyejy.yudong.util.SharedUtil;
import net.zgyejy.yudong.util.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends MyBaseActivity {
    private Bundle bundle;
    private RequestQueue requestQueue;//volley接口对象
    private String phone;//手机号码
    private String password;//用户密码
    private String token;//用户令牌

    @BindView(R.id.et_userName)
    TextInputEditText etUserName;//手机号输入框
    @BindView(R.id.et_userPsw)
    TextInputEditText etUserPsw;//密码输入框
    @BindView(R.id.cb_isRememberPsw)
    CheckBox cbIsRememberPsw;//是否记住密码
    @BindView(R.id.cb_isAutoLogin)
    CheckBox cbIsAutoLogin;//是否自动登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        etUserName.setText(SharedUtil.getUserPhone(this));//写入记住的用户手机号
        final Boolean isRememberPsw = SharedUtil.getIsRememberPsw(this);
        cbIsRememberPsw.setChecked(isRememberPsw);//根据缓存信息设置是否记住密码的checkBox
        if (isRememberPsw) {
            etUserPsw.setText(SharedUtil.getUserPsw(this));//若记住密码为true，则填入缓存的用户密码
        }
        cbIsAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !cbIsRememberPsw.isChecked()) {
                    cbIsAutoLogin.setChecked(false);
                    showToast("未记住密码，不能自动登录！");
                }
            }
        });
        Boolean isAutoLogin = SharedUtil.getIsAutoLogin(this);
        cbIsAutoLogin.setChecked(isAutoLogin);//根据缓存信息设置是否自动登录的checkBox
        if (isAutoLogin) {
            showToast("3秒后自动登录");
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(3000);
                        if (cbIsAutoLogin.isChecked()) {
                            login();
                        }else {
                            SharedUtil.saveIsAutoLogin(getBaseContext(),false);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    @OnClick({R.id.tv_login_back, R.id.tv_login_register, R.id.btn_login, R.id.tv_findPsw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_back:
                if (bundle == null)
                    bundle = new Bundle();
                bundle.putString("isTo", "UserFragment");
                openActivity(HomeActivity.class,bundle);
                finish();
                break;
            case R.id.tv_login_register:
                if (bundle == null)
                    bundle = new Bundle();
                bundle.putInt("rOrF",0);//添加目的为注册（0为注册）
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,0,bundle);
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_findPsw:
                if (bundle == null)
                    bundle = new Bundle();
                bundle.putInt("rOrF",1);//添加目的为找回密码（1为找回密码）
                openActivity(RegisterActivity.class,bundle);
                finish();
                break;
        }
    }

    private void login() {
        phone = etUserName.getText().toString();
        password = etUserPsw.getText().toString();
        if (null == phone || phone.length() == 0) {
            showToast("手机号码不能为空！");
        }else if (phone.length()!=11) {
            showToast("请输入正确的手机号码！");
        }else if (null == password || password.length() == 0) {
            showToast("密码不能为空！");
        }else if (password.length()<6||password.length()>23) {
            showToast("密码长度必须在6-23之间！");
        }else {
            //保存用户手机号
            SharedUtil.saveUserPhone(getBaseContext(),phone);
            if (cbIsRememberPsw.isChecked()) {
                //保存是否记住密码的信息，保存密码信息
                SharedUtil.saveIsRememberPsw(getBaseContext(),true);
                SharedUtil.saveUserPsw(getBaseContext(),password);
                //保存是否自动登录的信息
                SharedUtil.saveIsAutoLogin(getBaseContext(),cbIsAutoLogin.isChecked());
            }else {
                //保存是否记住密码的信息，清空密码信息
                SharedUtil.saveIsRememberPsw(getBaseContext(),false);
            }
            sendLogin();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //获取注册页面传输过来的手机号和密码
            Bundle bundle = data.getExtras();
            phone = bundle.getString("phone");
            password = bundle.getString("password");

            //将传过来的手机号和密码自动填入输入框
            etUserName.setText(phone);
            etUserPsw.setText(password);
        }
    }

    /**
     * 发送登录请求
     */
    private void sendLogin() {
        if (requestQueue == null)
            requestQueue = VolleySingleton.getVolleySingleton(this).getRequestQueue();
        String urlLogin = API.LOGIN_POST_LOGIN;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        BaseEntity<Token> baseEntity = ParserBaseEntity.getBaseEntityOfToken(response);
                        if (baseEntity.getCode() == 200) {
                            showToast(baseEntity.getMessage());
                            token = baseEntity.getData().getToken();

                            //将已登录信息和用户令牌存储到本地
                            SharedUtil.setIsLogined(getBaseContext(),true);
                            SharedUtil.saveToken(getBaseContext(),token);

                            Bundle bundle = new Bundle();
                            bundle.putString("isTo", "UserFragment");
                            openActivity(HomeActivity.class,bundle);
                            finish();
                        }else {
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
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
