package net.zgyejy.yudong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.view.XCRoundRectImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserFragment extends Fragment {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.ll_user_myBalance, R.id.ll_user_myCoupons, R.id.ll_user_myPoints,
            R.id.ll_user_myOrders, R.id.ll_user_myCollect, R.id.ll_user_integralMall,
            R.id.ll_user_actCenter, R.id.ll_user_normalProblem})
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.ll_user_actCenter:
                break;
            case R.id.ll_user_normalProblem:
                break;
        }
    }
}
