package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.MyPagerAdapter;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.gloable.Contacts;
import net.zgyejy.yudong.modle.BaseEntity;
import net.zgyejy.yudong.modle.Token;
import net.zgyejy.yudong.modle.Version;
import net.zgyejy.yudong.modle.parser.ParserBaseEntity;
import net.zgyejy.yudong.modle.parser.ParserCourseInfo;
import net.zgyejy.yudong.util.SharedUtil;
import net.zgyejy.yudong.util.UpdateManager;
import net.zgyejy.yudong.util.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LeadActivity extends MyBaseActivity {
    private RequestQueue requestQueue;//volley接口对象
    private boolean isFromAboutUs;//是否是从关于我们界面点进来的
    private ViewPager vpLeadPicture;
    private TextView tvLeadSkip;//点击进入
    private MyPagerAdapter adapter;
    private ImageView[] imageViews = new ImageView[4];
    private boolean isFirstRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //检查更新
        checkUpdate();


    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        if (requestQueue == null)
            requestQueue = VolleySingleton.getVolleySingleton(this).getRequestQueue();
        showToast("正在检查版本，请稍候！");
        String urlUpdate = API.APP_POST_UPDATE;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        BaseEntity<Version> baseEntity = ParserBaseEntity.getBaseEntityOfVersion(response);
                        if (baseEntity.getCode() == 201||baseEntity.getCode() ==200) {//201强制更新，200非强制更新
                            //得到最新下载包链接
                            String url = API.APP_SERVER_IP + baseEntity.getData().getVersionUrl();
                            UpdateManager updateManager = new UpdateManager(getBaseContext(), url);
                            updateManager.checkUpdateInfo(LeadActivity.this);
                        }else {
                            isFirstRunning = SharedUtil.getBoolean(getBaseContext(), "isFirst", true);
                            if (isFirstRunning) {
                                isFirstRunning = false;
                                savePreferences();
                                setContentView(R.layout.activity_lead);
                                initView();
                                init();
                                initData();
                            } else {
                                openActivity(TableTopActivity.class);
                                finish();
                            }
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
                params.put("app", "android");
                params.put("versionCode", Contacts.VER);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        vpLeadPicture = (ViewPager) findViewById(R.id.vp_lead_picture);
        tvLeadSkip = (TextView) findViewById(R.id.tv_lead_skip);

        imageViews[0] = (ImageView) findViewById(R.id.lead_point0);
        imageViews[1] = (ImageView) findViewById(R.id.lead_point1);
        imageViews[2] = (ImageView) findViewById(R.id.lead_point2);
        imageViews[3] = (ImageView) findViewById(R.id.lead_point3);

        imageViews[0].setAlpha(1.0f);

        String from = getIntent().getStringExtra("AboutUsFrom");
        if (from != null && from.equals("AboutUsActivity")) {
            isFromAboutUs = true;
        } else {
            isFromAboutUs = false;
        }
    }

    /**
     * 创建适配器，设置监听
     */
    private void init() {
        adapter = new MyPagerAdapter(this);
        vpLeadPicture.setAdapter(adapter);
        vpLeadPicture.setOnPageChangeListener(listener);
        tvLeadSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFromAboutUs) {
                    finish();
                } else {
                    openActivity(TableTopActivity.class);
                    finish();
                }
            }
        });
    }

    /**
     * 导入数据
     */
    private void initData() {
        ImageView imageView;
        imageView = (ImageView) getLayoutInflater().inflate(
                R.layout.layout_lead_viewpager_item, null);
        imageView.setBackgroundResource(R.drawable.lead1);
        adapter.addToAdapterView(imageView);

        imageView = (ImageView) getLayoutInflater().inflate(
                R.layout.layout_lead_viewpager_item, null);
        imageView.setBackgroundResource(R.drawable.lead2);
        adapter.addToAdapterView(imageView);

        imageView = (ImageView) getLayoutInflater().inflate(
                R.layout.layout_lead_viewpager_item, null);
        imageView.setBackgroundResource(R.drawable.lead3);
        adapter.addToAdapterView(imageView);

        imageView = (ImageView) getLayoutInflater().inflate(
                R.layout.layout_lead_viewpager_item, null);
        imageView.setBackgroundResource(R.drawable.lead4);
        adapter.addToAdapterView(imageView);

        adapter.notifyDataSetChanged();
    }

    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position >= 3) {
                if (isFromAboutUs)
                    tvLeadSkip.setText("点击返回");
                tvLeadSkip.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[i].setAlpha(0.4f);
            }
            imageViews[position].setAlpha(1.0f);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 生成用户配置信息
     */
    private void savePreferences() {
        SharedUtil.putBoolean(this, "isFirst", isFirstRunning);
    }
}
