package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.zxing.common.StringUtils;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.ListViewAdapter_51;
import net.zgyejy.yudong.base.MyBaseActivity;
import me.yejy.greendao.VideoIntegral;
import net.zgyejy.yudong.modle.CourseInfo;
import net.zgyejy.yudong.modle.parser.ParserCourseInfo;
import net.zgyejy.yudong.util.VolleySingleton;

import org.json.JSONObject;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Video51TrialListActivity extends MyBaseActivity {
    private boolean isFirstResume = true;
    private List<VideoIntegral> list;
    private ListViewAdapter_51 adapter;//51视频适配器
    //网络请求相关
    private RequestQueue requestQueue;//volley接口对象

    @BindView(R.id.tv_videoList_title)
    TextView tvVideoListTitle;
    @BindView(R.id.lv_videoList)
    ListView lvVideoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video51_trial_list);
        ButterKnife.bind(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstResume) {
            initView();
            loadData();
            isFirstResume = false;
        }
    }

    /**
     * 加载数据
     */
    private void loadData() {
        showLoadingDialog(this,"正在加载数据...",true);
        String url = getIntent().getExtras().getString("course");
        if (requestQueue == null)
            requestQueue = VolleySingleton.getVolleySingleton(this).getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        CourseInfo courseInfo = ParserCourseInfo.getCourseInfo(jsonObject.toString());
                        list = courseInfo.getVideo();
                        String title = list.get(0).getVideo_name();
                        title = title.substring(title.indexOf("课 ")+1,title.lastIndexOf(" 第"));
                        tvVideoListTitle.setText(title);
                        adapter.appendDataed(list,true);
                        adapter.notifyDataSetChanged();
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

    private void initView() {
        if (adapter == null)
            adapter = new ListViewAdapter_51(this);
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
    }

    @OnClick(R.id.iv_videoList_back)
    public void onClick() {
        finish();
    }
}
