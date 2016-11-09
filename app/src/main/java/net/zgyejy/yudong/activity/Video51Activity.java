package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
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
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.ListViewAdapter_Free;
import net.zgyejy.yudong.adapter.MyPagerAdapter;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.modle.Video;
import net.zgyejy.yudong.modle.parser.ParserVideoList;
import net.zgyejy.yudong.util.CommonUtil;
import net.zgyejy.yudong.util.VolleySingleton;

import org.json.JSONObject;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Video51Activity extends MyBaseActivity {
    PullToRefreshListView lvVideo51;
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

    private ListViewAdapter_Free adapter;//适配器
    private List<Video> list51Video;//一课的视频列表(5个视频)
    private String url51;//5个视频列表的链接
    private RequestQueue requestQueue;//volley接口对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video51);
        ButterKnife.bind(this);

        url51 = getIntent().getStringExtra("result");
        //showToast(url51);

        initView();

        loadListData();
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
        lvVideo51 = (PullToRefreshListView) linearLayout.findViewById(R.id.lv_video51);
        if (adapter == null)
            adapter = new ListViewAdapter_Free(this);
        lvVideo51.setAdapter(adapter);
        initLvRefresh(lvVideo51);//设置下拉上拉刷新参数
        setListeners();//设置各种监听
        viewPagerAdapter.addToAdapterView(linearLayout);

        //加载MP3页面
        linearLayout = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.layout_video51_mp3,null);

        //（加载MP3数据）..................

        viewPagerAdapter.addToAdapterView(linearLayout);

        //加载文档页面
        linearLayout = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.layout_video51_text,null);

        //（加载文档数据）...................

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

    //显示文档
    private void showText() {

    }

    //显示MP3
    private void showMp3() {

    }

    //显示视频列表
    private void showVideo() {

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

    /**
     * 设置刷新参数
     *
     * @param pullToRefresh
     */
    private void initLvRefresh(PullToRefreshListView pullToRefresh) {
        pullToRefresh.setMode(PullToRefreshBase.Mode.BOTH);
        ILoadingLayout startLabels = pullToRefresh
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
    }

    /**
     * 设置监听
     */
    private void setListeners() {
        lvVideo51.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadListData();
            }
        });
        lvVideo51.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                Video video = adapter.getItem(position - 1);
                bundle.putSerializable("video", video);
                openActivity(VideoPlayActivity.class, bundle);
            }
        });
    }

    /**
     * 根据传入的url返回5个1的视频列表
     */
    private void loadListData() {
        if (!CommonUtil.isNetworkAvailable(this)) {
            showToast("当前无网络连接，请连接网络!");
        } else {
            if (requestQueue == null)
                //实例化一个RequestQueue对象
                requestQueue = VolleySingleton.getVolleySingleton(this).getRequestQueue();
            showLoadingDialog(this, "数据正在加载...", true);//显示加载动画
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url51,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            list51Video = ParserVideoList.getVideoList(jsonObject.toString());
                            adapter.appendDataed(list51Video, true);
                            adapter.updateAdapter();
                            lvVideo51.onRefreshComplete();
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

    @OnClick(R.id.iv_video51_return)
    public void onClick() {
        finish();
    }
}
