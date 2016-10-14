package net.zgyejy.yudong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.activity.HomeActivity;
import net.zgyejy.yudong.adapter.GridViewAdapter_51Book;
import net.zgyejy.yudong.adapter.MyPagerAdapter;
import net.zgyejy.yudong.modle.Book;

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

    GridView gvVideo51;//五个一教材列表
    ListView lvVideo51,lvVideoFree,lvVideoVip;

    @BindView(R.id.vp_home_video)
    ViewPager vpHomeVideo;//左右滑动界面
    private MyPagerAdapter viewPagerAdapter;

    private GridViewAdapter_51Book adapter51Book;//五个一教材列表适配器

    @BindColor(R.color.white)
    int whiteColor;//白色
    private int themeColor;//主题颜色

    private List<Book> listBook;//五个一教材列表

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
        initVideo51();
        return view;
    }

    private void initView() {

        viewPagerAdapter = new MyPagerAdapter(getActivity());
        vpHomeVideo.setAdapter(viewPagerAdapter);
        vpHomeVideo.setOnPageChangeListener(pageChangeListener);

        FrameLayout frameLayout;

        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_video_list_51,null);
        gvVideo51 = (GridView) frameLayout.findViewById(R.id.gv_video_51);
        lvVideoFree = (ListView) frameLayout.findViewById(R.id.lv_video_51);
        viewPagerAdapter.addToAdapterView(frameLayout);

        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_video_list_free,null);
        lvVideoFree = (ListView) frameLayout.findViewById(R.id.lv_video_free);
        viewPagerAdapter.addToAdapterView(frameLayout);

        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_video_list_vip,null);
        lvVideoFree = (ListView) frameLayout.findViewById(R.id.lv_video_vip);
        viewPagerAdapter.addToAdapterView(frameLayout);

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
            topGuidTag = position;
            setTopGuid();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

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

    private void showVideoVip() {
        topGuidTag = 2;
        setTopGuid();
    }

    private void showVideoFree() {
        topGuidTag = 1;
        setTopGuid();
    }

    //初始化五个一视频界面
    private void initVideo51() {
        topGuidTag = 0;
        setTopGuid();
        if (adapter51Book == null)
            adapter51Book = new GridViewAdapter_51Book(getContext());
        gvVideo51.setAdapter(adapter51Book);
        initDateVideo51();
    }

    private void showVideo51() {
        topGuidTag = 0;
        setTopGuid();
        gvVideo51.setVisibility(View.VISIBLE);
        initDateVideo51();
    }

    private void initDateVideo51() {
        String[] str = {"一", "二", "三", "四", "五", "六"};
        if (listBook == null) {
            listBook = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                listBook.add(new Book("七巧板智力阅读\n第" + str[i] + "册"));
            }
        }
        adapter51Book.appendDataed(listBook, true);
        adapter51Book.updateAdapter();
    }

    private void setDefaultTopGuid() {
        themeColor = ((HomeActivity) getActivity()).getThemeColor();

        tvVideo51.setBackgroundResource(R.drawable.text_view_border_51);
        tvVideoFree.setBackgroundResource(R.drawable.text_view_border_free);
        tvVideoVip.setBackgroundResource(R.drawable.text_view_border_vip);

        tvVideo51.setTextColor(themeColor);
        tvVideoFree.setTextColor(themeColor);
        tvVideoVip.setTextColor(themeColor);
    }

    private void setTopGuid() {
        setDefaultTopGuid();
        switch (topGuidTag) {
            case 0:
                tvVideo51.setBackgroundResource(R.drawable.text_view_back_51);
                tvVideo51.setTextColor(whiteColor);
                break;
            case 1:
                tvVideoFree.setBackgroundResource(R.drawable.text_view_back_free);
                tvVideoFree.setTextColor(whiteColor);
                break;
            case 2:
                tvVideoVip.setBackgroundResource(R.drawable.text_view_back_vip);
                tvVideoVip.setTextColor(whiteColor);
                break;
        }
    }
}
