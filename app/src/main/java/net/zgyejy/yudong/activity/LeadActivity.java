package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.MyPagerAdapter;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.util.SharedUtil;

public class LeadActivity extends MyBaseActivity {
    boolean isFromUserFragment;
    ViewPager vpLeadPicture;
    TextView tvLeadSkip;//点击进入
    private MyPagerAdapter adapter;
    private ImageView[] imageViews = new ImageView[4];
    private boolean isFirstRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstRunning = SharedUtil.getBoolean(this, "isFirst", true);
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
        if (from != null && from.equals("UserFragment")) {
            isFromUserFragment = true;
        } else {
            isFromUserFragment = false;
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

                if (isFromUserFragment) {
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
                if (isFromUserFragment)
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
