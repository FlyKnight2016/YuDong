package net.zgyejy.yudong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.activity.CourseList51Activity;
import net.zgyejy.yudong.activity.HomeActivity;
import net.zgyejy.yudong.activity.SearchActivity;
import net.zgyejy.yudong.activity.VideoPlayActivity;
import net.zgyejy.yudong.adapter.GridViewAdapter_51Book;
import net.zgyejy.yudong.adapter.ListViewAdapter_Free;
import net.zgyejy.yudong.adapter.ListViewAdapter_Vip;
import net.zgyejy.yudong.adapter.MyPagerAdapter;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.modle.Book;
import net.zgyejy.yudong.modle.Video;
import net.zgyejy.yudong.modle.VideoVip;
import net.zgyejy.yudong.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoFragment extends Fragment {
    Bundle bundle;

    @BindView(R.id.tv_video_51)
    TextView tvVideo51;
    @BindView(R.id.tv_video_free)
    TextView tvVideoFree;
    @BindView(R.id.tv_video_vip)
    TextView tvVideoVip;

    @BindView(R.id.vp_home_video)
    ViewPager vpHomeVideo;//左右滑动界面

    private MyPagerAdapter viewPagerAdapter;

    GridView gvVideo51;//五个一教材列表
    PullToRefreshListView lvVideoFree, lvVideoVip;

    private GridViewAdapter_51Book adapter51Book;//五个一教材列表适配器
    private ListViewAdapter_Free adapterListFree;
    private ListViewAdapter_Vip adapterListVip;

    @BindColor(R.color.white)
    int whiteColor;//白色
    private int themeColor;//主题颜色

    private List<Book> listBook;//五个一教材列表
    private List<Video> listVideoFree;
    private List<VideoVip> listVideoVip;

    private int topGuideTag = 0;//当前页面标识

    private RequestQueue requestQueue;//volley接口对象

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);
        initView();
        initVideoAdapters();
        loadDate();
        return view;
    }

    /**
     * 加载数据
     */
    private void loadDate() {
        if (!CommonUtil.isNetworkAvailable(getActivity())) {
            ((HomeActivity) getActivity()).showToast("当前无网络连接，请连接网络!");
        } else {
            requestQueue = Volley.newRequestQueue(getContext());//实例化一个RequestQueue对象
            String isTo = ((HomeActivity) getActivity()).getIsTo();
            if (isTo != null && isTo.equals("51Video")) {
                showVideo51();
            } else if (isTo.equals("FreeVideo")) {
                vpHomeVideo.setCurrentItem(1);
            } else if (isTo.equals("VipVideo")) {
                vpHomeVideo.setCurrentItem(2);
            }
        }
    }

    /**
     * 界面的初始化
     */
    private void initView() {

        viewPagerAdapter = new MyPagerAdapter(getActivity());
        vpHomeVideo.setAdapter(viewPagerAdapter);
        vpHomeVideo.setOnPageChangeListener(pageChangeListener);

        FrameLayout frameLayout;

        //51视频GridView
        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_video_list_51, null);
        gvVideo51 = (GridView) frameLayout.findViewById(R.id.gv_video_51);
        gvVideo51.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //打开课程列表界面
                if (bundle == null)
                    bundle = new Bundle();
                bundle.putString("courses",listBook.get(position).getUrl());
                ((HomeActivity) getActivity()).openActivity(CourseList51Activity.class,bundle);
            }
        });
        viewPagerAdapter.addToAdapterView(frameLayout);

        //免费视频ListView
        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_video_list_free, null);
        lvVideoFree = (PullToRefreshListView) frameLayout.findViewById(R.id.lv_video_free);
        initLvRefresh(lvVideoFree);
        lvVideoFree.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新数据
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载数据
            }
        });
        lvVideoFree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((HomeActivity) getActivity()).openActivity(VideoPlayActivity.class);
            }
        });
        viewPagerAdapter.addToAdapterView(frameLayout);

        //收费视频ListView
        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_video_list_vip, null);
        lvVideoVip = (PullToRefreshListView) frameLayout.findViewById(R.id.lv_video_vip);
        initLvRefresh(lvVideoVip);
        lvVideoVip.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新数据
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载数据
            }
        });
        lvVideoVip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((HomeActivity) getActivity()).openActivity(VideoPlayActivity.class);
            }
        });
        viewPagerAdapter.addToAdapterView(frameLayout);

        viewPagerAdapter.notifyDataSetChanged();
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

    /**
     * 普通的点击事件
     *
     * @param view
     */
    @OnClick({R.id.tv_video_51, R.id.tv_video_free,
            R.id.tv_video_vip, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_video_51:
                vpHomeVideo.setCurrentItem(0);
                break;
            case R.id.tv_video_free:
                vpHomeVideo.setCurrentItem(1);
                break;
            case R.id.tv_video_vip:
                vpHomeVideo.setCurrentItem(2);
                break;
            case R.id.iv_search:
                ((HomeActivity) getActivity()).openActivity(SearchActivity.class);
                break;
        }
    }

    /**
     * 显示vip视频数据
     */
    private void showVideoVip() {
        VideoVip videoVip = new VideoVip();
        if (listVideoVip == null) {
            listVideoVip = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                listVideoVip.add(videoVip);//测试添加数据
            }
        }
        adapterListVip.appendDataed(listVideoVip, true);
        adapterListVip.updateAdapter();
    }

    /**
     * 显示免费视频数据
     */
    private void showVideoFree() {
        Video video = new Video(51,"视频名称","视频链接");
        if (listVideoFree == null) {
            listVideoFree = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                listVideoFree.add(video);//测试添加数据
            }
        }
        adapterListFree.appendDataed(listVideoFree, true);
        adapterListFree.updateAdapter();
    }

    /**
     * 显示五个一视频数据
     */
    private void showVideo51() {
        if (listBook == null)
            listBook = new ArrayList<>();
        listBook.clear();
        for (int i = 0; i < 6; i++) {
            listBook.add(new Book("七巧板智力阅读\n第" + (i+1) + "册"));
            setBook(i);
        }
        adapter51Book.appendDataed(listBook, true);
        adapter51Book.updateAdapter();
    }

    private void setBook(int i) {
        int image;
        Book book = listBook.get(i);
        switch (i) {
            case 0:
                image = R.drawable.volume1;
                book.setUrl(API.VIDEO_51_BOOK_LIST+131);
                break;
            case 1:
                image = R.drawable.volume2;
                book.setUrl(API.VIDEO_51_BOOK_LIST+65);
                break;
            case 2:
                image = R.drawable.volume3;
                book.setUrl(API.VIDEO_51_BOOK_LIST+156);
                break;
            case 3:
                image = R.drawable.volume4;
                book.setUrl(API.VIDEO_51_BOOK_LIST+86);
                break;
            case 4:
                image = R.drawable.volume5;
                book.setUrl(API.VIDEO_51_BOOK_LIST+177);
                break;
            default:
                image = R.drawable.volume6;
                book.setUrl(API.VIDEO_51_BOOK_LIST+109);
                break;
        }
        book.setBookImage(image);
    }

    /**
     * 配置各列表适配器，初始化51视频界面
     */
    private void initVideoAdapters() {

        if (adapter51Book == null)
            adapter51Book = new GridViewAdapter_51Book(getActivity());
        gvVideo51.setAdapter(adapter51Book);

        if (adapterListFree == null)
            adapterListFree = new ListViewAdapter_Free(getActivity());
        lvVideoFree.setAdapter(adapterListFree);

        if (adapterListVip == null)
            adapterListVip = new ListViewAdapter_Vip(getActivity());
        lvVideoVip.setAdapter(adapterListVip);

        topGuideTag = 0;
        setTopGuide();
    }

    /**
     * 设置上方所有导航标题为未选中状态
     */
    private void setDefaultTopGuide() {
        themeColor = ((HomeActivity) getActivity()).getThemeColor();

        tvVideo51.setBackgroundResource(R.drawable.text_view_border_51);
        tvVideoFree.setBackgroundResource(R.drawable.text_view_border_free);
        tvVideoVip.setBackgroundResource(R.drawable.text_view_border_vip);

        tvVideo51.setTextColor(whiteColor);
        tvVideoFree.setTextColor(whiteColor);
        tvVideoVip.setTextColor(whiteColor);
    }

    //设置上标题和对应数据加载
    private void setTopGuide() {
        setDefaultTopGuide();
        switch (topGuideTag) {
            case 0:
                tvVideo51.setBackgroundResource(R.drawable.text_view_back_51);
                tvVideo51.setTextColor(themeColor);
                showVideo51();
                break;
            case 1:
                tvVideoFree.setBackgroundResource(R.drawable.text_view_back_free);
                tvVideoFree.setTextColor(themeColor);
                showVideoFree();
                break;
            case 2:
                tvVideoVip.setBackgroundResource(R.drawable.text_view_back_vip);
                tvVideoVip.setTextColor(themeColor);
                showVideoVip();
                break;
        }
    }
}
