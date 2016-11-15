package net.zgyejy.yudong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.activity.AboutUsActivity;
import net.zgyejy.yudong.activity.HomeActivity;
import net.zgyejy.yudong.activity.LoginActivity;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.gloable.Contacts;
import net.zgyejy.yudong.modle.Coupon;
import net.zgyejy.yudong.modle.UserBaseInfo;
import net.zgyejy.yudong.modle.parser.ParserUserBaseInfo;
import net.zgyejy.yudong.util.CommonUtil;
import net.zgyejy.yudong.util.SharedUtil;
import net.zgyejy.yudong.util.VolleySingleton;
import net.zgyejy.yudong.view.XCRoundRectImageView;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.sephiroth.android.library.picasso.Picasso;


public class UserFragment extends Fragment {
    private String token;
    private boolean isLogined;
    private RequestQueue requestQueue;//volley接口对象
    private UserBaseInfo userBaseInfo;

    private String userName;//用户昵称
    private String userPortrait;//用户头像
    private String userPoints;//用户积分
    private int[] payVideos;//已付费视频id
    private int[] collectVideos;//已收藏视频id
    private List<Coupon> coupons;//优惠券

    @BindView(R.id.iv_userPortrait)
    XCRoundRectImageView ivUserPortrait;
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.tv_user_myBalance)
    TextView tvUserMyBalance;
    @BindView(R.id.tv_user_myCoupons)
    TextView tvUserMyCoupons;
    @BindView(R.id.tv_user_myPoints)
    TextView tvUserMyPoints;
    @BindView(R.id.tv_version_code)
    TextView tvVersionCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLogined = SharedUtil.getIsLogined(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        tvVersionCode.setText(Contacts.VER);
        if (isLogined) {
            token = SharedUtil.getToken(getContext());
            getUserBaseInfo();
        }
        return view;
    }

    @OnClick({R.id.iv_userPortrait, R.id.tv_userName, R.id.ll_user_myBalance, R.id.ll_user_myCoupons, R.id.ll_user_myPoints,
            R.id.ll_user_myOrders, R.id.ll_user_myCollect, R.id.ll_user_integralMall,
            R.id.ll_user_normalProblem, R.id.ll_user_aboutUs})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_userPortrait:
            case R.id.tv_userName:
                //跳转到登录界面
                ((HomeActivity) getActivity()).openActivity(LoginActivity.class);
                getActivity().finish();
                break;
            case R.id.ll_user_myBalance:
                break;
            case R.id.ll_user_myCoupons:
                break;
            case R.id.ll_user_myPoints:
                break;
            case R.id.ll_user_myOrders:
                break;
            case R.id.ll_user_myCollect:
                break;
            case R.id.ll_user_integralMall:
                break;
            case R.id.ll_user_normalProblem:
                break;
            case R.id.ll_user_aboutUs:
                ((HomeActivity) getActivity()).openActivity(AboutUsActivity.class);
                break;
        }
    }

    /**
     * 发送网络请求，得到用户基本信息
     */
    public void getUserBaseInfo() {
        if (!CommonUtil.isNetworkAvailable(getActivity())) {
            ((HomeActivity) getActivity()).showToast("当前无网络连接，请连接网络!");
        } else {
            if (requestQueue == null)
                //实例化一个RequestQueue对象
                requestQueue = VolleySingleton.getVolleySingleton(getContext()).getRequestQueue();
            String urlUser = API.USER_GET_USERINFO + "token=" + token;
            ((HomeActivity) getActivity()).showLoadingDialog(getContext(), "数据正在加载...", true);//显示加载动画
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlUser,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            //解析用户基本信息
                            userBaseInfo = ParserUserBaseInfo.getUserBaseInfo(jsonObject.toString());

                            userName = userBaseInfo.getUsername();
                            userPortrait = userBaseInfo.getImage();
                            userPoints = userBaseInfo.getIntegralcount();
                            payVideos = userBaseInfo.getPayVideo();
                            collectVideos = userBaseInfo.getCollectVideo();
                            coupons = userBaseInfo.getCoupon();

                            //设置用户名
                            if (userName != null) {
                                tvUserName.setText(userName);
                            } else {
                                String phone = SharedUtil.getUserPhone(getContext());
                                tvUserName.setText(phone.substring(0, 3) + "****" + phone.substring(7));
                            }

                            //使用Picasso加载头像
                            Picasso.with(getContext())
                                    .load(userPortrait)//加载地址
                                    .placeholder(R.drawable.loadingphoto)//占位图（加载中）
                                    .error(R.drawable.user_selected)//加载失败
                                    .into(ivUserPortrait);//加载到的ImageView

                            //设置积分
                            if (userPoints!=null) {
                                tvUserMyPoints.setText(userPoints);
                            }

                            //设置优惠券数目
                            if (coupons!=null) {
                                tvUserMyCoupons.setText(coupons.size()+"");
                            }

                            ((HomeActivity) getActivity()).cancelDialog();
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
