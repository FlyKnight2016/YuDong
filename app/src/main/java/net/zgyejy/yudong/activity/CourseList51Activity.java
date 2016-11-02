package net.zgyejy.yudong.activity;

import android.os.Bundle;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.modle.Course;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 七巧板智力阅读一册课程列表
 */
public class CourseList51Activity extends MyBaseActivity {
    private List<Course> listCourse;//一册的课程列表
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

    }

    private void loadListData() {

    }

    @OnClick(R.id.iv_course_return)
    public void onClick() {
        finish();
    }
}
