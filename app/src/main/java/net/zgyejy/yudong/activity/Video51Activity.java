package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.ListViewAdapter_51;
import net.zgyejy.yudong.adapter.ListViewAdapter_Free;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.modle.Video51;
import net.zgyejy.yudong.modle.VideoFree;
import net.zgyejy.yudong.view.RefreshableView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Video51Activity extends MyBaseActivity {
    private List<Video51> listWeekNums;
    private List<VideoFree> listVideo51;

    @BindView(R.id.lv_video_51_child)
    ListView lvVideo51Child;
    @BindView(R.id.refresh_Video51_child)
    RefreshableView refreshVideo51Child;
    @BindView(R.id.ll_video51_child)
    LinearLayout llVideo51Child;
    @BindView(R.id.lv_video_51)
    ListView lvVideo51;
    @BindView(R.id.refresh_Video51)
    RefreshableView refreshVideo51;

    private ListViewAdapter_51 adapter_51;
    private ListViewAdapter_Free adapter_51_child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video51);
        ButterKnife.bind(this);

        initView();

        initDataListWeekNums();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        if (adapter_51 == null)
            adapter_51 = new ListViewAdapter_51(this);
        lvVideo51.setAdapter(adapter_51);

        if (adapter_51_child == null)
            adapter_51_child = new ListViewAdapter_Free(this);
        lvVideo51Child.setAdapter(adapter_51_child);

        setListeners();
    }

    /**
     * 设置监听
     */
    private void setListeners() {
        lvVideo51.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                refreshVideo51.setVisibility(View.GONE);
                llVideo51Child.setVisibility(View.VISIBLE);
                initDataListVideo51();
            }
        });

        lvVideo51Child.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openActivity(VideoPlayActivity.class);
            }
        });

        refreshVideo51.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshVideo51.finishRefreshing();
            }
        }, 0);
        refreshVideo51Child.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshVideo51Child.finishRefreshing();
            }
        }, 0);
    }

    /**
     * 导入51视频周列表数据
     */
    private void initDataListWeekNums() {
        Video51 video51 = new Video51();
        if (listWeekNums == null) {
            listWeekNums = new ArrayList<>();
            for (int i =0 ; i<20; i++) {
                listWeekNums.add(video51);
            }
        }
        adapter_51.appendDataed(listWeekNums,true);
        adapter_51.updateAdapter();
    }

    /**
     * 导入一周的51视频列表数据
     */
    private void initDataListVideo51() {
        VideoFree videoFree = new VideoFree();
        if (listVideo51 == null) {
            listVideo51 = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                listVideo51.add(videoFree);//测试添加数据
            }
        }
        adapter_51_child.appendDataed(listVideo51, true);
        adapter_51_child.updateAdapter();
    }

    @OnClick({R.id.iv_video51_return, R.id.tv_video51_return,R.id.tv_video51_backParent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_video51_return:
            case R.id.tv_video51_return:
                openActivity(HomeActivity.class);
                finish();
                break;
            case R.id.tv_video51_backParent:
                refreshVideo51.setVisibility(View.VISIBLE);
                llVideo51Child.setVisibility(View.GONE);
                break;
        }
    }
}
