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
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.ListCourseAdapter;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.bean.Course;
import net.zgyejy.yudong.modle.parser.ParserCourseList;
import net.zgyejy.yudong.util.CommonUtil;
import net.zgyejy.yudong.util.VolleySingleton;

import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
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
        initLvRefresh(lvCourse);//设置下拉上拉刷新参数
        lvCourse.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadListData();
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

    private void loadListData() {
        if (!CommonUtil.isNetworkAvailable(this)) {
            showToast("当前无网络连接，请连接网络!");
        } else {
            if (requestQueue == null)
                requestQueue = VolleySingleton.getVolleySingleton(this).getRequestQueue();
            showLoadingDialog(this, "数据正在加载...", true);//显示加载动画
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlCourses,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            listCourse = ParserCourseList.getCourseList(jsonObject.toString());

                            //对课程列表进行排序整理的方法
                            sort(listCourse);

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

    /**
     * 整理排序课程列表
     * @param courses
     */
    private void sort(List<Course> courses) {
        Collections.sort(courses, new Comparator<Course>() {
            /**
             * 返回负数表示o1小于o2
             * 返回0表示o1等于o2
             * 返回正数表示o1大于o2
             * @param o1
             * @param o2
             * @return
             */
            @Override
            public int compare(Course o1, Course o2) {
                String str1 = o1.getName();
                String str2 = o2.getName();

                String num1 = str1.substring(str1.indexOf("册 第")+1,str1.indexOf("课"));
                String num2 = str2.substring(str1.indexOf("册 第")+1,str1.indexOf("课"));

                return num1.compareTo(num2);
            }
        });
    }

    @OnClick(R.id.iv_course_return)
    public void onClick() {
        finish();
    }
}
