package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.ListViewAdapter_Free;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.modle.Video;
import net.zgyejy.yudong.modle.parser.ParserVideoList;
import net.zgyejy.yudong.util.CommonUtil;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Video51Activity extends MyBaseActivity {
    @BindView(R.id.lv_video51)
    PullToRefreshListView lvVideo51;

    private ListViewAdapter_Free adapter;//适配器
    private List<Video> list51Video;//一课的视频列表(5个视频)
    private String url51;//5个视频列表的链接
    private RequestQueue requestQueue;//volley接口对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video51);
        ButterKnife.bind(this);

        initView();

        url51 = getIntent().getStringExtra("result");
        loadListData();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        if (adapter == null)
            adapter = new ListViewAdapter_Free(this);
        lvVideo51.setAdapter(adapter);
        initLvRefresh(lvVideo51);//设置下拉上拉刷新参数
        setListeners();//设置各种监听
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
     * 设置监听
     */
    private void setListeners() {
        lvVideo51.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadListData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                showToast("已无更多数据!");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lvVideo51.onRefreshComplete();
                    }
                });
            }
        });
        lvVideo51.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                Video video = list51Video.get(position);
                bundle.putSerializable("video",video);
                openActivity(VideoPlayActivity.class,bundle);
            }
        });
    }

    /**
     * 根据传入的url返回5个1的视频列表
     */
    private void loadListData() {
        if (!CommonUtil.isNetworkAvailable(this)) {
            showToast("当前无网络连接，请连接网络!");
        } else {
            if (requestQueue == null)
                requestQueue = Volley.newRequestQueue(this);//实例化一个RequestQueue对象
            showLoadingDialog(this, "数据正在加载...", true);//显示加载动画
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url51,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            list51Video = ParserVideoList.getVideoList(jsonObject.toString());
                            adapter.appendDataed(list51Video, true);
                            adapter.updateAdapter();
                            lvVideo51.onRefreshComplete();
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
    }

    @OnClick(R.id.iv_video51_return)
    public void onClick() {
        finish();
    }
}
