package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.MyExpandableListAdapter;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.view.RefreshableView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Video51Activity extends MyBaseActivity {
    private List<String> video51Types;
    private List<List<String>> listVideo51;

    @BindView(R.id.lv_video_51)
    ExpandableListView lvVideo51;
    @BindView(R.id.refresh_Video51)
    RefreshableView refreshVideo51;

    private MyExpandableListAdapter adapterList51;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video51);
        ButterKnife.bind(this);

        initView();

    }

    /*private void initData() {
        //Video51 video51 = new Video51();
        if (video51Types == null) {
            video51Types = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                video51Types.add("第" + i + "周");
            }
        }
        adapterList51.appendGroupData(video51Types,true);
        if (listVideo51 == null) {
            listVideo51 = new ArrayList<>();
            video51Types.clear();
            for (int i = 0; i < 5; i++) {
                video51Types.add("第" + i + "天");
            }
            for (int i = 0; i<20; i++) {
                listVideo51.add(video51Types);
            }
        }
        adapterList51.appendChildData(listVideo51,true);
    }*/

    private void initView() {
        if (video51Types == null) {
            video51Types = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                video51Types.add("第" + (i+1) + "周");
            }
        }
        if (listVideo51 == null) {
            List<String> video51Child = new ArrayList<>();
            listVideo51 = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                video51Child.add("第" + (i+1) + "天");
            }
            for (int i = 0; i<20; i++) {
                listVideo51.add(video51Child);
            }
        }
        if (adapterList51 == null)
            adapterList51 = new MyExpandableListAdapter(this, video51Types, listVideo51);
        lvVideo51.setAdapter(adapterList51);

        lvVideo51.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                openActivity(VideoPlayActivity.class);
                return true;
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
    }

    @OnClick({R.id.iv_video51_return, R.id.tv_video51_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_video51_return:
            case R.id.tv_video51_return:
                openActivity(HomeActivity.class);
                break;
        }
    }
}
