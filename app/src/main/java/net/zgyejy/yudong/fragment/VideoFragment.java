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
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.activity.CourseList51Activity;
import net.zgyejy.yudong.activity.HomeActivity;
import net.zgyejy.yudong.activity.SearchActivity;
import net.zgyejy.yudong.activity.VideoPlayActivity;
import net.zgyejy.yudong.adapter.GridViewAdapter_51Book;
import net.zgyejy.yudong.adapter.ListViewAdapter_51;
import net.zgyejy.yudong.adapter.ListViewAdapter_Vip;
import net.zgyejy.yudong.adapter.MyPagerAdapter;
import net.zgyejy.yudong.bean.VideoIntegral;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.modle.VideoVip;
import net.zgyejy.yudong.util.CommonUtil;
import net.zgyejy.yudong.util.DbService;
import net.zgyejy.yudong.util.SharedUtil;
import net.zgyejy.yudong.util.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yejy.greendao.Book;

public class VideoFragment extends Fragment {
    private boolean isFirstRunning;//是否第一次运行
    private Bundle bundle;
    private DbService dbService;//数据库管理工具

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
    PullToRefreshListView lvVideoIntegral, lvVideoVip;

    private GridViewAdapter_51Book adapter51Book;//五个一教材列表适配器
    private ListViewAdapter_51 adapterListFree;
    private ListViewAdapter_Vip adapterListVip;

    @BindColor(R.color.white)
    int whiteColor;//白色
    private int themeColor;//主题颜色

    private List<Book> listBook;//五个一教材列表
    private List<VideoIntegral> listVideoIntegralFree;
    private List<VideoVip> listVideoVip;

    private int topGuideTag = 0;//当前页面标识

    private RequestQueue requestQueue;//volley接口对象

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (dbService == null)
            dbService = DbService.getInstance(getContext());
        isFirstRunning = SharedUtil.getBoolean(getContext(), "isFirstRunning", true);
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
            if (requestQueue == null)
                //实例化一个RequestQueue对象
                requestQueue = VolleySingleton.getVolleySingleton(getContext()).getRequestQueue();
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
                bundle.putString("courses",listBook.get(position).getUrl());//将得到课程列表的url传入
                ((HomeActivity) getActivity()).openActivity(CourseList51Activity.class,bundle);
            }
        });
        viewPagerAdapter.addToAdapterView(frameLayout);

        //积分视频ListView
        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_video_list_integral, null);
        lvVideoIntegral = (PullToRefreshListView) frameLayout.findViewById(R.id.lv_video_integral);
        initLvRefresh(lvVideoIntegral);
        lvVideoIntegral.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新数据
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载数据
            }
        });
        lvVideoIntegral.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        VideoIntegral videoIntegral = new VideoIntegral(51,"视频名称","视频链接");
        if (listVideoIntegralFree == null) {
            listVideoIntegralFree = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                listVideoIntegralFree.add(videoIntegral);//测试添加数据
            }
        }
        adapterListFree.appendDataed(listVideoIntegralFree, true);
        adapterListFree.updateAdapter();
    }

    /**
     * 显示五个一视频数据
     */
    private void showVideo51() {
        if (listBook == null)
            listBook = new ArrayList<>();
        if (isFirstRunning) {//若是第一次运行，将数据存储如数据库中
            for (int i=0; i<6; i++) {
                Book book = new Book((long)(i+1));
                book.setBookName("七巧板智力阅读\n第" + (i+1) + "册");
                setBookInfo(book,i);
                dbService.saveBook(book);//存储到数据库中
                listBook.add(book);//添加到当前集合中
            }
            isFirstRunning = false;
            SharedUtil.putBoolean(getContext(), "isFirstRunning", isFirstRunning);
        }else {//否则从数据库中获取数据
            listBook = dbService.loadAllBook();
        }
        adapter51Book.appendDataed(listBook, true);
        adapter51Book.updateAdapter();
    }

    /**
     * 设置每册书的图片和获取课程列表的接口
     * @param i
     */
    private void setBookInfo(Book book,int i) {
        int image;
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
            adapterListFree = new ListViewAdapter_51(getActivity());
        lvVideoIntegral.setAdapter(adapterListFree);

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
