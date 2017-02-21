package net.zgyejy.yudong.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jorge.circlelibrary.ImageCycleView;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.live.ui.LiveActivity;
import net.zgyejy.yudong.util.SharedUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.sephiroth.android.library.picasso.Picasso;

public class TableTopActivity extends MyBaseActivity {
    private Bundle bundle;
    private boolean isLogined;

    @BindView(R.id.table_cycleView)
    ImageCycleView imageCycleView;
    @BindView(R.id.iv_tableTop_act)
    ImageView ivTableTopAct;
    @BindView(R.id.tv_tableTop_act_title)
    TextView tvTableTopActTitle;
    @BindView(R.id.tv_tableTop_act_date)
    TextView tvTableTopActDate;
    @BindView(R.id.tv_tableTop_act_content)
    TextView tvTableTopActContent;

    //装载数据的集合 文字描述
    ArrayList<String> imageDescList;
    //装载数据的集合 图片地址
    ArrayList<String> urlList;
    //装载数据的集合 广告链接
    ArrayList<String> advUrlList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_top);
        ButterKnife.bind(this);
        isLogined = SharedUtil.getIsLogined(this);

        loadAdvData();
    }

    /**
     * 请求并添加广告数据
     */
    private void loadAdvData() {
        if (imageDescList == null)
            imageDescList = new ArrayList<>();
        imageDescList.clear();
        if (urlList == null)
            urlList = new ArrayList<>();
        if (advUrlList == null)
            advUrlList = new ArrayList<>();
        urlList.clear();

        /**添加数据*/
        urlList.add("http://www.zgyejy.net/uploads/2016/10/131456398989.jpg");
        urlList.add("http://www.zgyejy.net/uploads/2016/10/241628115842.jpg");
        urlList.add("http://www.zgyejy.net/uploads/2016/04/201619098309.jpg");
        imageDescList.add("广告位招租");
        imageDescList.add("演讲小明星");
        imageDescList.add("诚邀加盟");
        advUrlList.add("http://www.zgyejy.net/zuixinhuodong/4631.html");
        advUrlList.add("http://www.zgyejy.net/tupianzhanshi/9559.html");
        advUrlList.add("http://www.zgyejy.net/tupianzhanshi/5476.html");

        initCarsuelView(imageDescList, urlList);

    }

    /**
     * 初始化轮播图
     *
     * @param imageDescList
     * @param urlList
     */
    private void initCarsuelView(ArrayList<String> imageDescList, ArrayList<String> urlList) {
        LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getScreenHeight(TableTopActivity.this) * 3 / 10);
        imageCycleView.setLayoutParams(cParams);
        ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
            @Override
            public void onImageClick(int position, View imageView) {
                /**实现点击事件*/
                if (bundle == null)
                    bundle = new Bundle();
                bundle.putString("url", advUrlList.get(position));
                openActivity(WebReadActivity.class, bundle);
            }

            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                /**在此方法中，显示图片，可以用自己的图片加载库，也可以用本demo中的（Imageloader）*/
                Picasso.with(getApplicationContext()).load(imageURL).into(imageView);
            }
        };
/**设置数据*/
        imageCycleView.setImageResources(imageDescList, urlList, mAdCycleViewListener);
        imageCycleView.startImageCycle();
    }

    /**
     * 得到屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        if (null == context) {
            return 0;
        }
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    @OnClick({R.id.iv_scan, R.id.iv_tableTop_user, R.id.tv_tableTop_toVideo, R.id.tv_tableTop_to51Video,
            R.id.tv_tableTop_toFreeVideo, R.id.tv_tableTop_toVipVideo, R.id.tv_tableTop_toStudy,
            R.id.tv_tableTop_toPrincipalStudy, R.id.tv_tableTop_toTeacherStudy,
            R.id.tv_tableTop_toParentsStudy, R.id.tv_tableTop_toKidStudy, R.id.tv_tabletop_toAct,
            R.id.tv_tableTop_toShow, R.id.iv_tableTop_toHomeWeb, R.id.rl_tableTop_act, R.id.iv_toDownLoad})
    public void onClick(View view) {
        isLogined = SharedUtil.getIsLogined(getBaseContext());
        if (bundle == null)
            bundle = new Bundle();
        switch (view.getId()) {
            case R.id.iv_scan:
                Intent intent = new Intent(TableTopActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.iv_tableTop_user:
                bundle.putString("isTo", "UserFragment");
                openActivity(HomeActivity.class, bundle);
                break;
            case R.id.tv_tableTop_toVideo:
            case R.id.tv_tableTop_to51Video:
                if (isLogined) {
                    openActivity(HomeActivity.class);
                } else {
                    showToast("还未登录，请先登录！");
                    openActivity(LoginActivity.class);
                }
                break;
            case R.id.tv_tableTop_toFreeVideo:
                if (isLogined) {
                    bundle.putString("isTo", "FreeVideo");
                    openActivity(HomeActivity.class, bundle);
                } else {
                    showToast("还未登录，请先登录！");
                    openActivity(LoginActivity.class);
                }
                break;
            case R.id.tv_tableTop_toVipVideo:
                if (isLogined) {
                    bundle.putString("isTo", "VipVideo");
                    openActivity(HomeActivity.class, bundle);
                } else {
                    showToast("还未登录，请先登录！");
                    openActivity(LoginActivity.class);
                }
                break;
            case R.id.tv_tableTop_toStudy:
                bundle.putString("isTo", "StudyFragment");
                openActivity(HomeActivity.class, bundle);
                break;
            case R.id.tv_tableTop_toPrincipalStudy:
                bundle.putString("isTo", "Principal");
                openActivity(HomeActivity.class, bundle);
                break;
            case R.id.tv_tableTop_toTeacherStudy:
                bundle.putString("isTo", "Teacher");
                openActivity(HomeActivity.class, bundle);
                break;
            case R.id.tv_tableTop_toParentsStudy:
                bundle.putString("isTo", "Parents");
                openActivity(HomeActivity.class, bundle);
                break;
            case R.id.tv_tableTop_toKidStudy:
                bundle.putString("isTo", "Kid");
                openActivity(HomeActivity.class, bundle);
                break;
            case R.id.rl_tableTop_act:
            case R.id.tv_tabletop_toAct:
                if (isLogined) {
                    openActivity(LiveActivity.class);
                } else {
                    showToast("还未登录，请先登录！");
                    openActivity(LoginActivity.class);
                }
                break;
            case R.id.tv_tableTop_toShow:
                bundle.putString("isTo", "Show");
                openActivity(HomeActivity.class, bundle);
                break;
            case R.id.iv_tableTop_toHomeWeb:
                //跳转到幼儿教育网网页
                bundle.putString("url", API.ZgyejyNetIP);
                openActivity(WebReadActivity.class, bundle);
                break;
            case R.id.iv_toDownLoad:

                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            //String result = bundle.getString("result");
            //showToast(result);
            Intent intent = new Intent(TableTopActivity.this, Video51Activity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        //添加退出登录的方法，或者用户选择不自动登录的话，销毁本地缓存的数据
        if (!SharedUtil.getIsAutoLogin(this)) {
            SharedUtil.setIsLogined(this, false);
            SharedUtil.saveToken(this, null);
        }
        super.onDestroy();
    }
}
