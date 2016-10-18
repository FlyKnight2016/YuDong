package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.ListViewAdapter_51;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.modle.Video51;
import net.zgyejy.yudong.view.RefreshableView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Video51Activity extends MyBaseActivity {
    private List<Video51> listVideo51;

    @BindView(R.id.lv_video_51)
    ListView lvVideo51;
    @BindView(R.id.refresh_Video51)
    RefreshableView refreshVideo51;

    private ListViewAdapter_51 adapterList51;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video51);
        ButterKnife.bind(this);
        
        initView();
        
        initData();

    }

    private void initData() {
        Video51 video51 = new Video51();
        if (listVideo51 == null) {
            listVideo51 = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                listVideo51.add(video51);//测试添加数据
            }
        }
        adapterList51.appendDataed(listVideo51, true);
        adapterList51.updateAdapter();
    }

    private void initView() {
        
        if (adapterList51 == null)
            adapterList51 = new ListViewAdapter_51(this);
        lvVideo51.setAdapter(adapterList51);

        lvVideo51.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
