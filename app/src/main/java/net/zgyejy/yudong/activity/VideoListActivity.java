package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.ListMp3Adapter;
import net.zgyejy.yudong.adapter.ListViewAdapter_Integral;
import net.zgyejy.yudong.adapter.MyPagerAdapter;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.modle.CourseInfo;
import net.zgyejy.yudong.modle.Mp3Text;
import net.zgyejy.yudong.modle.parser.ParserCourseInfo;
import net.zgyejy.yudong.util.CommonUtil;
import net.zgyejy.yudong.util.VolleySingleton;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yejy.greendao.VideoIntegral;


public class VideoListActivity extends MyBaseActivity {
    private static final String TAG = "VideoListActivity";
    @BindView(R.id.tv_video51_video)
    TextView tvVideo51Video;
    @BindView(R.id.tv_video51_mp3)
    TextView tvVideo51Mp3;
    @BindView(R.id.tv_video51_text)
    TextView tvVideo51Text;
    @BindView(R.id.vp_video51_vmt)
    ViewPager vpVideo51Vmt;
    MyPagerAdapter viewPagerAdapter;
    private int topGuideTag = 0;//当前页面标识
    @BindColor(R.color.themeColor)
    int themeColor;
    @BindColor(R.color.white)
    int whiteColor;

    private String url;
    private RequestQueue requestQueue;
    CourseInfo courseInfo;

    private List<VideoIntegral> videoList;//视频集合
    private List<Mp3Text> mp3TextList;//MP3文档集合
    private ListView lv_video, lv_mp3, lv_text;//视频、mp3、文档列表
    private TextView noVideo, noMp3, noText;//暂无数据
    private ListViewAdapter_Integral adapterVideo;//积分视频列表适配器
    private ListMp3Adapter adapterMp3;//MP3列表适配器
    //文档列表适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video51);
        ButterKnife.bind(this);
        url = getIntent().getExtras().getString("url");
        Log.i(TAG, "onCreate: ++++++++++++++" + url);

        initView();
        loadListData();
    }

    private void loadListData() {
        if (!CommonUtil.isNetworkAvailable(this)) {
            showToast("当前无网络连接，请连接网络!");
        } else {
            if (requestQueue == null)
                //实例化一个RequestQueue对象
                requestQueue = VolleySingleton.getVolleySingleton(this).getRequestQueue();
            showLoadingDialog(this, "数据正在加载...", true);//显示加载动画
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            courseInfo = ParserCourseInfo.getCourseInfo(jsonObject.toString());
                            videoList = courseInfo.getVideo();
                            showVideo();
                            mp3TextList = courseInfo.getMp3text();
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

    /**
     * 初始化界面
     */
    private void initView() {
        viewPagerAdapter = new MyPagerAdapter(this);
        vpVideo51Vmt.setAdapter(viewPagerAdapter);
        vpVideo51Vmt.setOnPageChangeListener(pageChangeListener);

        LinearLayout linearLayout;

        //加载视频列表页面
        linearLayout = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.layout_video51_list, null);
        lv_video = (ListView) linearLayout.findViewById(R.id.lv_video51);
        noVideo = (TextView) linearLayout.findViewById(R.id.tv_noData);
        if (adapterVideo == null)
            adapterVideo = new ListViewAdapter_Integral(this);
        lv_video.setAdapter(adapterVideo);
        lv_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                VideoIntegral videoIntegral = adapterVideo.getItem(position);
                bundle.putSerializable("videoIntegral", videoIntegral);
                openActivity(VideoPlayActivity.class, bundle);
            }
        });
        viewPagerAdapter.addToAdapterView(linearLayout);

        //加载MP3页面
        linearLayout = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.layout_video51_list, null);
        lv_mp3 = (ListView) linearLayout.findViewById(R.id.lv_video51);
        noMp3 = (TextView) linearLayout.findViewById(R.id.tv_noData);
        if (adapterMp3 == null)
            adapterMp3 = new ListMp3Adapter(this);
        lv_mp3.setAdapter(adapterMp3);
        lv_mp3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                Mp3Text mp3Text = adapterMp3.getItem(position);
                bundle.putSerializable("mp3Txt", mp3Text);
                openActivity(Mp3PlayActivity.class, bundle);
            }
        });
        viewPagerAdapter.addToAdapterView(linearLayout);

        //加载文档页面
        linearLayout = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.layout_video51_list, null);
        lv_text = (ListView) linearLayout.findViewById(R.id.lv_video51);
        noText = (TextView) linearLayout.findViewById(R.id.tv_noData);
        viewPagerAdapter.addToAdapterView(linearLayout);

        viewPagerAdapter.notifyDataSetChanged();
    }

    /**
     * ViewPager页面改变的监听
     */
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            topGuideTag = position;
            setTopGuide();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    //设置上标题和对应数据加载
    private void setTopGuide() {
        setDefaultTopGuide();
        switch (topGuideTag) {
            case 0:
                tvVideo51Video.setBackgroundResource(R.drawable.text_view_back_51);
                tvVideo51Video.setTextColor(themeColor);
                showVideo();
                break;
            case 1:
                tvVideo51Mp3.setBackgroundResource(R.drawable.text_view_back_free);
                tvVideo51Mp3.setTextColor(themeColor);
                showMp3();
                break;
            case 2:
                tvVideo51Text.setBackgroundResource(R.drawable.text_view_back_vip);
                tvVideo51Text.setTextColor(themeColor);
                showText();
                break;
        }
    }

    //显示视频列表
    private void showVideo() {
        if (videoList != null && videoList.size() != 0) {
            adapterVideo.appendDataed(videoList, true);
            adapterVideo.updateAdapter();
        } else {
            lv_video.setVisibility(View.GONE);
            noVideo.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示MP3列表
     */
    private void showMp3() {
        if (mp3TextList != null && mp3TextList.size() != 0) {
            adapterMp3.appendDataed(mp3TextList, true);
            adapterMp3.updateAdapter();
        } else {
            lv_mp3.setVisibility(View.GONE);
            noMp3.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示文档
     */
    private void showText() {
        lv_text.setVisibility(View.GONE);
        noText.setVisibility(View.VISIBLE);
    }

    /**
     * 设置上方所有导航标题为未选中状态
     */
    private void setDefaultTopGuide() {

        tvVideo51Video.setBackgroundResource(R.drawable.text_view_border_51);
        tvVideo51Mp3.setBackgroundResource(R.drawable.text_view_border_free);
        tvVideo51Text.setBackgroundResource(R.drawable.text_view_border_vip);

        tvVideo51Video.setTextColor(whiteColor);
        tvVideo51Mp3.setTextColor(whiteColor);
        tvVideo51Text.setTextColor(whiteColor);
    }

    @OnClick({R.id.tv_video51_video, R.id.tv_video51_mp3, R.id.tv_video51_text,R.id.iv_video51_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_video51_video:
                vpVideo51Vmt.setCurrentItem(0);
                break;
            case R.id.tv_video51_mp3:
                vpVideo51Vmt.setCurrentItem(1);
                break;
            case R.id.tv_video51_text:
                vpVideo51Vmt.setCurrentItem(2);
                break;
            case R.id.iv_video51_return:
                finish();
                break;
        }
    }
}
