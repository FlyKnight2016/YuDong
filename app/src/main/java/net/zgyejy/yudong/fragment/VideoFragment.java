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

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.activity.HomeActivity;
import net.zgyejy.yudong.activity.Video51Activity;
import net.zgyejy.yudong.activity.VideoPlayActivity;
import net.zgyejy.yudong.adapter.GridViewAdapter_51Book;
import net.zgyejy.yudong.adapter.ListViewAdapter_Free;
import net.zgyejy.yudong.adapter.ListViewAdapter_Vip;
import net.zgyejy.yudong.adapter.MyPagerAdapter;
import net.zgyejy.yudong.modle.Book;
import net.zgyejy.yudong.modle.VideoFree;
import net.zgyejy.yudong.modle.VideoVip;
import net.zgyejy.yudong.view.RefreshableView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoFragment extends Fragment {
    @BindView(R.id.tv_video_51)
    TextView tvVideo51;
    @BindView(R.id.tv_video_free)
    TextView tvVideoFree;
    @BindView(R.id.tv_video_vip)
    TextView tvVideoVip;

    @BindView(R.id.vp_home_video)
    ViewPager vpHomeVideo;//左右滑动界面

    private MyPagerAdapter viewPagerAdapter;

    RefreshableView refreshableViewFree, refreshableViewVip;//下拉刷新控件

    GridView gvVideo51;//五个一教材列表
    ListView lvVideoFree, lvVideoVip;

    private GridViewAdapter_51Book adapter51Book;//五个一教材列表适配器
    private ListViewAdapter_Free adapterListFree;
    private ListViewAdapter_Vip adapterListVip;

    @BindColor(R.color.white)
    int whiteColor;//白色
    private int themeColor;//主题颜色

    private List<Book> listBook;//五个一教材列表
    private List<VideoFree> listVideoFree;
    private List<VideoVip> listVideoVip;

    private int topGuidTag = 0;//当前页面标识


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
        return view;
    }

    /**
     * 界面的初始化
     */
    private void initView() {

        viewPagerAdapter = new MyPagerAdapter(getActivity());
        vpHomeVideo.setAdapter(viewPagerAdapter);
        vpHomeVideo.setOnPageChangeListener(pageChangeListener);

        FrameLayout frameLayout;

        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_video_list_51, null);
        gvVideo51 = (GridView) frameLayout.findViewById(R.id.gv_video_51);
        gvVideo51.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((HomeActivity) getActivity()).openActivity(Video51Activity.class);
            }
        });
        viewPagerAdapter.addToAdapterView(frameLayout);

        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_video_list_free, null);
        lvVideoFree = (ListView) frameLayout.findViewById(R.id.lv_video_free);
        lvVideoFree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((HomeActivity) getActivity()).openActivity(VideoPlayActivity.class);
            }
        });
        refreshableViewFree = (RefreshableView) frameLayout.findViewById
                (R.id.refresh_VideoFree);
        viewPagerAdapter.addToAdapterView(frameLayout);

        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_video_list_vip, null);
        lvVideoVip = (ListView) frameLayout.findViewById(R.id.lv_video_vip);
        lvVideoVip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((HomeActivity) getActivity()).openActivity(VideoPlayActivity.class);
            }
        });
        refreshableViewVip = (RefreshableView) frameLayout.findViewById
                (R.id.refresh_VideoVip);
        viewPagerAdapter.addToAdapterView(frameLayout);

        setRefreshListener();

        viewPagerAdapter.notifyDataSetChanged();
    }

    /**
     * 设置下拉刷新控件的监听
     */
    private void setRefreshListener() {

        refreshableViewFree.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshableViewFree.finishRefreshing();
            }
        }, 0);

        refreshableViewVip.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshableViewVip.finishRefreshing();
            }
        }, 0);
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
            topGuidTag = position;
            setTopGuid();
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
    @OnClick({R.id.iv_scan, R.id.tv_video_51, R.id.tv_video_free,
            R.id.tv_video_vip, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_scan:
                break;
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
        VideoFree videoFree = new VideoFree();
        if (listVideoFree == null) {
            listVideoFree = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                listVideoFree.add(videoFree);//测试添加数据
            }
        }
        adapterListFree.appendDataed(listVideoFree, true);
        adapterListFree.updateAdapter();
    }

    /**
     * 显示五个一视频数据
     */
    private void showVideo51() {
        String[] str = {"一", "二", "三", "四", "五", "六"};
        if (listBook == null)
            listBook = new ArrayList<>();
        listBook.clear();
        for (int i = 0; i < 6; i++) {
            listBook.add(new Book("七巧板智力阅读\n第" + str[i] + "册"));
        }
        adapter51Book.appendDataed(listBook, true);
        adapter51Book.updateAdapter();
    }

    /**
     * 配置各列表适配器，初始化51视频界面
     */
    private void initVideoAdapters() {

        if (adapter51Book == null)
            adapter51Book = new GridViewAdapter_51Book(getContext());
        gvVideo51.setAdapter(adapter51Book);

        if (adapterListFree == null)
            adapterListFree = new ListViewAdapter_Free(getContext());
        lvVideoFree.setAdapter(adapterListFree);

        if (adapterListVip == null)
            adapterListVip = new ListViewAdapter_Vip(getContext());
        lvVideoVip.setAdapter(adapterListVip);

        topGuidTag = 0;
        setTopGuid();
    }

    /**
     * 设置上方所有导航标题为未选中状态
     */
    private void setDefaultTopGuid() {
        themeColor = ((HomeActivity) getActivity()).getThemeColor();

        tvVideo51.setBackgroundResource(R.drawable.text_view_border_51);
        tvVideoFree.setBackgroundResource(R.drawable.text_view_border_free);
        tvVideoVip.setBackgroundResource(R.drawable.text_view_border_vip);

        tvVideo51.setTextColor(themeColor);
        tvVideoFree.setTextColor(themeColor);
        tvVideoVip.setTextColor(themeColor);
    }

    //设置上标题和对应数据加载
    private void setTopGuid() {
        setDefaultTopGuid();
        switch (topGuidTag) {
            case 0:
                tvVideo51.setBackgroundResource(R.drawable.text_view_back_51);
                tvVideo51.setTextColor(whiteColor);
                showVideo51();
                break;
            case 1:
                tvVideoFree.setBackgroundResource(R.drawable.text_view_back_free);
                tvVideoFree.setTextColor(whiteColor);
                showVideoFree();
                break;
            case 2:
                tvVideoVip.setBackgroundResource(R.drawable.text_view_back_vip);
                tvVideoVip.setTextColor(whiteColor);
                showVideoVip();
                break;
        }
    }
}
