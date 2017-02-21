package net.zgyejy.yudong.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import net.zgyejy.yudong.activity.EditUserInfoActivity;
import net.zgyejy.yudong.activity.HomeActivity;
import net.zgyejy.yudong.activity.LoginActivity;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.gloable.Contacts;
import net.zgyejy.yudong.modle.BaseEntity;
import net.zgyejy.yudong.modle.Coupon;
import net.zgyejy.yudong.modle.UserBaseInfo;
import net.zgyejy.yudong.modle.parser.ParserBaseEntity;
import net.zgyejy.yudong.modle.parser.ParserUserBaseInfo;
import net.zgyejy.yudong.util.CommonUtil;
import net.zgyejy.yudong.util.DbService;
import net.zgyejy.yudong.util.SharedUtil;
import net.zgyejy.yudong.util.VolleySingleton;
import net.zgyejy.yudong.view.XCRoundRectImageView;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.sephiroth.android.library.picasso.Picasso;


public class UserFragment extends Fragment {
    final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
    private String token;
    private boolean isLogined;
    private RequestQueue requestQueue;//volley接口对象
    private UserBaseInfo userBaseInfo;

    private DbService dbService;
    private String userName;//用户昵称
    private String userPortrait;//用户头像
    private String userPoints;//用户积分
    private long[] payVideos;//已付费视频id
    private long[] collectVideos;//已收藏视频id
    private List<Coupon> coupons;//优惠券

    @BindView(R.id.iv_userPortrait)
    XCRoundRectImageView ivUserPortrait;
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.tv_user_signIn)
    TextView tvUserSignIn;
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
        if (dbService == null)
            dbService = DbService.getInstance(getContext());
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
            if (isSignInedToday()) {//若当前已登录过
                tvUserSignIn.setBackgroundResource(R.drawable.signined);
            }
        }
        return view;
    }

    @OnClick({R.id.iv_userPortrait, R.id.tv_userName, R.id.ll_user_myBalance, R.id.ll_user_myCoupons, R.id.ll_user_myPoints,
            R.id.ll_user_myOrders, R.id.ll_user_myCollect, R.id.ll_user_integralMall,
            R.id.ll_user_normalProblem, R.id.ll_user_aboutUs, R.id.tv_user_signIn, R.id.tv_editData})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_userPortrait:
            case R.id.tv_userName:
                if (isLogined) {
                    quiteDialog();
                } else {
                    //跳转到登录界面
                    ((HomeActivity) getActivity()).openActivity(LoginActivity.class);
                    getActivity().finish();
                }
                break;
            case R.id.tv_user_signIn:
                if (isLogined) {
                    signIn();
                } else {
                    showToast("还未登录，请先登录！");
                    getMyActivity().openActivity(LoginActivity.class);
                    getActivity().finish();
                }
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
            case R.id.tv_editData:
                //编辑个人资料
                ((HomeActivity) getActivity()).openActivity(EditUserInfoActivity.class);
                break;
        }
    }

    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }

    private static final String TAG = "UserFragment";

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
            Log.i(TAG, "getUserBaseInfo: +++++++++++++++++++++++++++++++" + urlUser);
            ((HomeActivity) getActivity()).showLoadingDialog(getContext(), "数据正在加载...", true);//显示加载动画
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlUser,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {

                            Log.i(TAG, "onResponse: ===========" + jsonObject.toString());

                            //解析用户基本信息
                            userBaseInfo = ParserUserBaseInfo.getUserBaseInfo(jsonObject.toString());

                            Log.i(TAG, "onResponse: +++++++++++" + userBaseInfo);
                            userName = userBaseInfo.getUsername();
                            userPortrait = userBaseInfo.getImage();
                            userPoints = userBaseInfo.getIntegralcount();
                            payVideos = userBaseInfo.getPayVideo();
                            dbService.saveAllPayVideo(payVideos);
                            collectVideos = userBaseInfo.getCollectVideo();
                            dbService.saveAllCollectVideo(collectVideos);
                            coupons = userBaseInfo.getCoupon();

                            //设置用户名
                            if (userName != null) {
                                tvUserName.setText(userName);
                                SharedUtil.saveUserName(getContext(),userName);//保存用户名
                            } else {
                                String phone = SharedUtil.getUserPhone(getContext());
                                tvUserName.setText(phone.substring(0, 3) + "****" + phone.substring(7));
                            }

                            Log.i(TAG, "onResponse: ----------" + userPortrait);

                            //使用Picasso加载头像
                            Picasso.with(getContext())
                                    .load(userPortrait)//加载地址
                                    .placeholder(R.drawable.loadingphoto)//占位图（加载中）
                                    .error(R.drawable.user_selected)//加载失败
                                    .into(ivUserPortrait);//加载到的ImageView

                            //设置积分
                            if (userPoints != null) {
                                tvUserMyPoints.setText(userPoints);
                            }

                            //设置优惠券数目
                            if (coupons != null) {
                                tvUserMyCoupons.setText(coupons.size() + "");
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

    /**
     * 弹出是否退出登录的提示
     */
    private void quiteDialog() {
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        SharedUtil.clearAllInfo(getContext());//清空所有用户信息
                        getActivity().finish();//关闭当前界面
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        ((HomeActivity) getActivity()).showToast("取消退出！");
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确认退出?"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确认", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.create().show();
    }

    /**
     * 判断当日是否已经签到过了，若没有，存储当前签到时间
     *
     * @return
     */
    private boolean isSignInedToday() {
        String now = df.format(new Date());//当前时间，格式：2016-11-17 下午4:35:55
        String lastSignInDate = SharedUtil.getSignInDate(getContext());//获取上次存储的签到时间
        if (lastSignInDate == null//之前未记录过或不是同一天
                || !now.substring(0, 10).equals(lastSignInDate.substring(0, 10))) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 签到增加积分，并存储签到时间
     */
    private void signIn() {
        if (requestQueue == null)
            requestQueue = VolleySingleton.getVolleySingleton(getContext()).getRequestQueue();
        showLoadingDialog("正在增加积分...");//显示加载动画
        String urlGetIntegral = API.USER_GET_INTEGRAL + "flag=1" + "&token=" + token;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlGetIntegral,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        BaseEntity<List> baseEntity = ParserBaseEntity.
                                getBaseEntityList(jsonObject.toString());
                        if (200 == baseEntity.getCode()) {
                            showToast(baseEntity.getMessage());
                        } else {
                            showToast("已签到过啦！");
                        }
                        SharedUtil.saveSignInDate(getContext(), df.format(new Date()));//存储当前签到时间
                        tvUserSignIn.setBackgroundResource(R.drawable.signined);
                        cancelDialog();
                        getUserBaseInfo();//刷新数据
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

    private void showLoadingDialog(String msg) {
        ((HomeActivity) getActivity()).showLoadingDialog(getContext(), msg, true);
    }

    private void cancelDialog() {
        ((HomeActivity) getActivity()).cancelDialog();
    }

    private void showToast(String msg) {
        ((HomeActivity) getActivity()).showToast(msg);
    }
}
