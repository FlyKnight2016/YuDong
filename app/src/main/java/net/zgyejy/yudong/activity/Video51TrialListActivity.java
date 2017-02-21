package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.ListViewAdapter_51;
import net.zgyejy.yudong.base.MyBaseActivity;
import me.yejy.greendao.VideoIntegral;
import net.zgyejy.yudong.modle.CourseInfo;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Video51TrialListActivity extends MyBaseActivity {
    List<VideoIntegral> list;
    private ListViewAdapter_51 adapter;//51视频适配器

    @BindView(R.id.tv_videoList_title)
    TextView tvVideoListTitle;
    @BindView(R.id.lv_videoList)
    ListView lvVideoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video51_trial_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        showLoadingDialog(this,"正在加载数据...",true);
        Bundle bundle = getIntent().getExtras();
        tvVideoListTitle.setText(bundle.getString("title"));
        list = ((CourseInfo)bundle.getSerializable("courseInfo")).getVideo();
        if (adapter == null)
            adapter = new ListViewAdapter_51(this);
        adapter.appendDataed(list,true);
        lvVideoList.setAdapter(adapter);
        lvVideoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoIntegral videoIntegral = adapter.getItem(position);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("videoIntegral",videoIntegral);
                openActivity(Video51TrialPlayActivity.class,bundle1);
            }
        });
        cancelDialog();
    }

    @OnClick(R.id.iv_videoList_back)
    public void onClick() {
        finish();
    }
}
