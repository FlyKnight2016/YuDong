package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TableTopActivity extends MyBaseActivity {

    @BindView(R.id.vp_tableTop_recommend)
    ViewPager vpTableTopRecommend;
    @BindView(R.id.iv_tableTop_act)
    ImageView ivTableTopAct;
    @BindView(R.id.tv_tableTop_act_title)
    TextView tvTableTopActTitle;
    @BindView(R.id.tv_tableTop_act_date)
    TextView tvTableTopActDate;
    @BindView(R.id.tv_tableTop_act_content)
    TextView tvTableTopActContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_top);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_tableTop_user, R.id.tv_tableTop_toVideo, R.id.tv_tableTop_to51Video,
            R.id.tv_tableTop_toFreeVideo, R.id.tv_tableTop_toVipVideo, R.id.tv_tableTop_toStudy,
            R.id.tv_tableTop_toPrincipalStudy, R.id.tv_tableTop_toTeacherStudy,
            R.id.tv_tableTop_toParentsStudy, R.id.tv_tableTop_toKidStudy, R.id.tv_tabletop_toAct,
            R.id.tv_tableTop_toShow,R.id.iv_tableTop_toHomeWeb})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.iv_tableTop_user:
                bundle.putString("isTo","UserFragment");
                openActivity(HomeActivity.class,bundle);
                break;
            case R.id.tv_tableTop_toVideo:
            case R.id.tv_tableTop_to51Video:
                openActivity(HomeActivity.class);
                break;
            case R.id.tv_tableTop_toFreeVideo:
                bundle.putString("isTo","FreeVideo");
                openActivity(HomeActivity.class,bundle);
                break;
            case R.id.tv_tableTop_toVipVideo:
                bundle.putString("isTo","VipVideo");
                openActivity(HomeActivity.class,bundle);
                break;
            case R.id.tv_tableTop_toStudy:
                bundle.putString("isTo","StudyFragment");
                openActivity(HomeActivity.class,bundle);
                break;
            case R.id.tv_tableTop_toPrincipalStudy:
                bundle.putString("isTo","Principal");
                openActivity(HomeActivity.class,bundle);
                break;
            case R.id.tv_tableTop_toTeacherStudy:
                bundle.putString("isTo","Teacher");
                openActivity(HomeActivity.class,bundle);
                break;
            case R.id.tv_tableTop_toParentsStudy:
                bundle.putString("isTo","Parents");
                openActivity(HomeActivity.class,bundle);
                break;
            case R.id.tv_tableTop_toKidStudy:
                bundle.putString("isTo","Kid");
                openActivity(HomeActivity.class,bundle);
                break;
            case R.id.tv_tabletop_toAct:
                bundle.putString("isTo","Act");
                openActivity(HomeActivity.class,bundle);
                break;
            case R.id.tv_tableTop_toShow:
                bundle.putString("isTo","Show");
                openActivity(HomeActivity.class,bundle);
                break;
            case R.id.iv_tableTop_toHomeWeb:
                //跳转到幼儿教育网网页
                bundle.putString("isTo","YejyNet");
                openActivity(WebReadActivity.class,bundle);
                break;
        }
    }
}
