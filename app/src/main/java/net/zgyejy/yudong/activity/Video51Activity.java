package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.ListViewAdapter_51;
import net.zgyejy.yudong.adapter.ListViewAdapter_Free;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.modle.Video51;
import net.zgyejy.yudong.modle.VideoFree;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Video51Activity extends MyBaseActivity {
    private List<Video51> listWeekNums;
    private List<VideoFree> listVideo51;

    @BindView(R.id.lv_video_51_child)
    PullToRefreshListView lvVideo51Child;
    @BindView(R.id.ll_video51_child)
    LinearLayout llVideo51Child;
    @BindView(R.id.lv_video_51)
    PullToRefreshListView lvVideo51;

    private ListViewAdapter_51 adapter_51;
    private ListViewAdapter_Free adapter_51_child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video51);
        ButterKnife.bind(this);

        initView();

        initDataListWeekNum();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        if (adapter_51 == null)
            adapter_51 = new ListViewAdapter_51(this);
        lvVideo51.setAdapter(adapter_51);
        initLvRefresh(lvVideo51);

        if (adapter_51_child == null)
            adapter_51_child = new ListViewAdapter_Free(this);
        lvVideo51Child.setAdapter(adapter_51_child);
        initLvRefresh(lvVideo51Child);

        setListeners();
    }

    /**
     * 设置刷新参数
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
        lvVideo51.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉加载数据
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载数据
            }
        });
        lvVideo51.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lvVideo51.setVisibility(View.GONE);
                llVideo51Child.setVisibility(View.VISIBLE);
                initDataListVideo51();
            }
        });

        lvVideo51Child.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉加载数据
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载数据
            }
        });
        lvVideo51Child.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openActivity(VideoPlayActivity.class);
            }
        });
    }

    /**
     * 导入51视频周列表数据
     */
    private void initDataListWeekNum() {
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
                lvVideo51.setVisibility(View.VISIBLE);
                llVideo51Child.setVisibility(View.GONE);
                break;
        }
    }
}
