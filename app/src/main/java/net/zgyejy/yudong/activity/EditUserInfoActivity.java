package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.GridViewAdapter_portraitList;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.modle.BaseEntity;
import net.zgyejy.yudong.modle.Portrait;
import net.zgyejy.yudong.modle.PortraitAndInfos;
import net.zgyejy.yudong.modle.Token;
import net.zgyejy.yudong.modle.UserInfoMore;
import net.zgyejy.yudong.modle.parser.ParserBaseEntity;
import net.zgyejy.yudong.modle.parser.ParserUserBaseInfo;
import net.zgyejy.yudong.util.CommonUtil;
import net.zgyejy.yudong.util.SharedUtil;
import net.zgyejy.yudong.util.VolleySingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static io.rong.imlib.statistics.UserData.phone;


public class EditUserInfoActivity extends MyBaseActivity {
    @BindView(R.id.cb_isMan)
    CheckBox cbIsMan;
    @BindView(R.id.cb_isWomen)
    CheckBox cbIsWomen;
    @BindView(R.id.tv_userIdentity)
    TextView tvUserIdentity;
    @BindView(R.id.et_kindergarten)
    EditText etKindergarten;
    @BindView(R.id.et_qqNumber)
    EditText etQqNumber;
    @BindView(R.id.et_weichatNumber)
    EditText etWeichatNumber;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_signature)
    EditText etSignature;
    private RequestQueue requestQueue;//volley接口对象

    @BindView(R.id.et_newName)
    EditText etNewName;
    @BindView(R.id.ll_editPortrait)
    LinearLayout ll_editPortrait;
    @BindView(R.id.iv_newPortrait)
    ImageView ivNewPortrait;

    private String nickname;
    private int iid = 1;

    private static final String TAG = "EditUserInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        ButterKnife.bind(this);
        initView();
        getPortraitAndInfos();
        showPopupWindow();
    }

    /**
     * 设置监听
     */
    private void initView() {
        cbIsMan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbIsWomen.setChecked(false);
                    gender = 1;
                }
            }
        });
        cbIsWomen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbIsMan.setChecked(false);
                    gender = 2;
                }
            }
        });
    }

    @OnClick({R.id.iv_edit_back, R.id.iv_newPortrait, R.id.tv_newPortrait,
            R.id.tv_editConfirm, R.id.tv_editCancel, R.id.tv_userIdentity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_edit_back:
                finish();
                break;
            case R.id.iv_newPortrait:
            case R.id.tv_newPortrait:
                //弹出头像列表，选择编辑头像
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else if (popupWindow != null) {
                    popupWindow.showAsDropDown(ll_editPortrait, 0, 5);
                }
                break;
            case R.id.tv_editConfirm:
                nickname = etNewName.getText().toString();
                //上传修改信息，确认返回结果
                conFirmEdit();
                //储存本地用户基本信息
                SharedUtil.saveUserName(this, nickname);
                break;
            case R.id.tv_editCancel:
                finish();
                break;
            case R.id.tv_userIdentity:
                if (popupWindow2.isShowing()) {
                    popupWindow2.dismiss();
                } else if (popupWindow2 != null) {
                    popupWindow2.showAsDropDown(tvUserIdentity, 0, 2);
                }
                break;
        }
    }

    private int gender = 1;//性别数字标识
    private int identity = 1;//身份数字标识

    /**
     * 确认修改
     */
    private void conFirmEdit() {
        if (etNewName.getText().toString().length()==0) {
            showToast("昵称不能为空!");
        }else {
            if (userInfoMore == null) {
                userInfoMore = new UserInfoMore();
            }
            userInfoMore.setNickname(etNewName.getText().toString());
            userInfoMore.setGender(gender + "");
            userInfoMore.setIdentity(identity + "");
            userInfoMore.setQq(etQqNumber.getText().toString());
            userInfoMore.setWeichat(etWeichatNumber.getText().toString());
            userInfoMore.setKindergarten(etKindergarten.getText().toString());
            userInfoMore.setEmail(etEmail.getText().toString());
            userInfoMore.setSignature(etSignature.getText().toString());
            userInfoMore.setImage(portraitImage);
            userInfoMore.setIid(iid);
            sendEditChange();
        }
    }

    /**
     * 发送请求，修改资料
     */
    private void sendEditChange() {
        if (requestQueue == null)
            requestQueue = VolleySingleton.getVolleySingleton(this).getRequestQueue();
        showLoadingDialog(this, "数据正在加载...", true);//显示加载动画
        String urlEditUserinfo = API.APP_EDIT_USERINFOS + "token=" + SharedUtil.getToken(getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlEditUserinfo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //解析返回消息
                        BaseEntity<Object> baseEntity = ParserBaseEntity.getBaseEntityObject(response);
                        if (baseEntity.getCode() == 200) {
                            showToast(baseEntity.getMessage());
                            SharedUtil.saveUserInfoMore(getBaseContext(),userInfoMore);
                            finish();
                        }else {
                            showToast(baseEntity.getMessage());
                        }
                        cancelDialog();
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
                params.put("nickname", userInfoMore.getNickname());
                params.put("gender", userInfoMore.getGender());
                params.put("identity", userInfoMore.getIdentity());
                params.put("qq", userInfoMore.getQq());
                params.put("weichat", userInfoMore.getWeichat());
                params.put("kindergarten", userInfoMore.getKindergarten());
                params.put("email", userInfoMore.getEmail());
                params.put("signature", userInfoMore.getSignature());
                params.put("iid", userInfoMore.getIid()+"");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private PopupWindow popupWindow,popupWindow2;//头像选择菜单
    private List<Portrait> portraits;//头像集合
    private GridView portraitList;//头像列表
    private GridViewAdapter_portraitList adapter_portraitList;

    /**
     * ts
     * 弹出头像列表
     */
    private void showPopupWindow() {
        View view = getLayoutInflater().inflate(R.layout.layout_portrait_select, null);
        portraitList = (GridView) view.findViewById(R.id.gv_portrait);
        adapter_portraitList = new GridViewAdapter_portraitList(getBaseContext());
        portraitList.setAdapter(adapter_portraitList);
        updatePortraits();
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        portraitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ivNewPortrait.setImageResource(portraits.get(position).getPortraitCode());
                portraitImage = portraits.get(position).getPortraitUri();
                iid = position +1;
                popupWindow.dismiss();
            }
        });

        View view2 = getLayoutInflater().inflate(R.layout.layout_identitys,null);
        TextView identity1 = (TextView) view2.findViewById(R.id.tv_identity1);
        TextView identity2 = (TextView) view2.findViewById(R.id.tv_identity2);
        TextView identity3 = (TextView) view2.findViewById(R.id.tv_identity3);
        popupWindow2 = new PopupWindow(view2, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_identity1:
                        identity = 1;
                        tvUserIdentity.setText("园长▼");
                        break;
                    case R.id.tv_identity2:
                        identity = 2;
                        tvUserIdentity.setText("老师▼");
                        break;
                    case R.id.tv_identity3:
                        identity = 3;
                        tvUserIdentity.setText("家长▼");
                        break;
                }
                popupWindow2.dismiss();
            }
        };
        identity1.setOnClickListener(listener);
        identity2.setOnClickListener(listener);
        identity3.setOnClickListener(listener);
    }

    private void updatePortraits() {
        if (portraits == null)
            portraits = new ArrayList<>();
        portraits.clear();
        for (int i = 1; i <= 24; i++) {
            portraits.add(new Portrait(i));
        }
        adapter_portraitList.appendDataed(portraits, true);
        adapter_portraitList.updateAdapter();
    }

    private String portraitImage;//用户头像
    private UserInfoMore userInfoMore;//其他信息

    /**
     * 获取用户头像和其他信息并存到本地
     */
    private void getPortraitAndInfos() {
        if (!CommonUtil.isNetworkAvailable(this)) {
            showToast("当前无网络连接，请连接网络!");
        } else {
            if (requestQueue == null)
                requestQueue = VolleySingleton.getVolleySingleton(this).getRequestQueue();
            showLoadingDialog(this, "数据正在加载...", true);//显示加载动画
            String getInfoUri = API.APP_GET_PORTRAITANDINFOS + "token=" + SharedUtil.getToken(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getInfoUri,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            PortraitAndInfos portraitAndInfos =
                                    ParserUserBaseInfo.getPortraitAndInfos(jsonObject.toString());
                            if (portraitAndInfos != null) {
                                portraitImage = portraitAndInfos.getIconUrl();
                                if (portraitImage != null) {
                                    SharedUtil.saveUserIcon(getBaseContext(), portraitImage);//保存用户头像地址
                                }
                                if (portraitAndInfos.getUserinfo().size() != 0) {
                                    userInfoMore = portraitAndInfos.getUserinfo().get(0);
                                    SharedUtil.saveUserName(getBaseContext(), userInfoMore.getNickname());//保存用户名
                                    SharedUtil.saveUserInfoMore(getBaseContext(),userInfoMore);
                                    etNewName.setText(userInfoMore.getNickname());
                                    if ("2".equals(userInfoMore.getGender())) {
                                        cbIsWomen.setChecked(true);
                                    }
                                    ivNewPortrait.setImageResource(new Portrait(userInfoMore.getIid()).getPortraitCode());
                                    String identity = "园长";
                                    if ("2".equals(userInfoMore.getIdentity())) {
                                        identity = "老师";
                                    }else if ("3".equals(userInfoMore.getIdentity())) {
                                        identity = "家长";
                                    }
                                    tvUserIdentity.setText(identity + "▼");
                                    etKindergarten.setText(userInfoMore.getKindergarten());
                                    etQqNumber.setText(userInfoMore.getQq());
                                    etWeichatNumber.setText(userInfoMore.getWeichat());
                                    etEmail.setText(userInfoMore.getEmail());
                                    etSignature.setText(userInfoMore.getSignature());
                                    iid = userInfoMore.getIid();
                                    SharedUtil.saveUserInfoMore(getBaseContext(),userInfoMore);
                                }
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
    }
}
