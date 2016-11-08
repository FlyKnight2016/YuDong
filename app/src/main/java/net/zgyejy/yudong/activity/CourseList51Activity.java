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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.ListCourseAdapter;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.modle.Course;
import net.zgyejy.yudong.modle.parser.ParserCourseList;
import net.zgyejy.yudong.util.CommonUtil;
import org.json.JSONObject;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 七巧板智力阅读一册课程列表
 */
public class CourseList51Activity extends MyBaseActivity {

    private Bundle bundle;
    private RequestQueue requestQueue;//volley接口对象
    private List<Course> listCourse;//一册的课程列表
    private ListCourseAdapter adapter;//课程列表适配器
    private String urlCourses;//课程列表的链接

    @BindView(R.id.lv_course)
    PullToRefreshListView lvCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list51);
        ButterKnife.bind(this);

        initView();

        urlCourses = getIntent().getStringExtra("courses");
        loadListData();
    }

    /**
     * 初始化界面，设置监听
     */
    private void initView() {
        if (adapter == null)
            adapter = new ListCourseAdapter(this);
        lvCourse.setAdapter(adapter);
        lvCourse.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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
                        lvCourse.onRefreshComplete();
                    }
                });
            }
        });
        lvCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (bundle == null)
                    bundle = new Bundle();
                bundle.putString("result",API.VIDEO_51_BOOK_LIST + adapter.getItem(position-1).getId());
                openActivity(Video51Activity.class,bundle);
            }
        });
    }

    private void loadListData() {
        if (!CommonUtil.isNetworkAvailable(this)) {
            showToast("当前无网络连接，请连接网络!");
        } else {
            if (requestQueue == null)
                requestQueue = Volley.newRequestQueue(this);//实例化一个RequestQueue对象
            showLoadingDialog(this, "数据正在加载...", true);//显示加载动画
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlCourses,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            listCourse = ParserCourseList.getCourseList(jsonObject.toString());
                            adapter.appendDataed(listCourse, true);
                            adapter.updateAdapter();
                            lvCourse.onRefreshComplete();
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

    @OnClick(R.id.iv_course_return)
    public void onClick() {
        finish();
    }
}
