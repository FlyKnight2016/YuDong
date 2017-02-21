package net.zgyejy.yudong.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import net.zgyejy.yudong.R;
import net.zgyejy.yudong.activity.CourseList51Activity;
import net.zgyejy.yudong.activity.HomeActivity;
import net.zgyejy.yudong.activity.SearchActivity;
import net.zgyejy.yudong.activity.Video51Activity;
import net.zgyejy.yudong.activity.VideoListActivity;
import net.zgyejy.yudong.activity.VideoPlayActivity;
import net.zgyejy.yudong.adapter.HListViewAdapter_Integral;
import net.zgyejy.yudong.adapter.ListCourseAdapter;
import net.zgyejy.yudong.adapter.MyPagerAdapter;
import me.yejy.greendao.VideoIntegral;

import net.zgyejy.yudong.bean.Course;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.modle.CourseInfo;
import net.zgyejy.yudong.modle.parser.ParserCourseInfo;
import net.zgyejy.yudong.modle.parser.ParserCourseList;
import net.zgyejy.yudong.util.CommonUtil;
import net.zgyejy.yudong.util.DbService;
import net.zgyejy.yudong.util.SharedUtil;
import net.zgyejy.yudong.util.VolleySingleton;
import org.json.JSONObject;
import java.util.List;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static net.zgyejy.yudong.gloable.API.VIDEO_51_BOOK_LIST;

public class VideoFragment extends Fragment {
    private static final String TAG = "VideoFragment";
    private boolean isFirstRunning;//是否第一次运行
    private Bundle bundle;
    private DbService dbService;//数据库管理工具

    @BindView(R.id.tv_video_51)
    TextView tvVideo51;
    @BindView(R.id.tv_video_free)
    TextView tvVideoFree;
    @BindView(R.id.tv_video_vip)
    TextView tvVideoVip;

    @BindView(R.id.vp_home_video)
    ViewPager vpHomeVideo;//左右滑动界面

    private MyPagerAdapter viewPagerAdapter;

    private LinearLayout ll_volume1,ll_volume2,ll_volume3,
            ll_volume4,ll_volume5,ll_volume6;//为1到6册的点击事件

    //积分视频页面的控件
    private LinearLayout more;//试用课程
    private RelativeLayout c1_22, c3_12, c5_18, speech1, speech2, speech3,getStudents;
    private GridView gvMemory, gvMore;//记忆力小明星列表,更多视频列表
    private List<VideoIntegral> listMemory, listMore;
    private HListViewAdapter_Integral adapterMemory, adapterMore;//适配器
    private CourseInfo courseInfo;

    private ListView lvVipCourses;//收费视频课程列表
    private ListCourseAdapter vipCoursesAdapter;//课程列表适配器
    private List<Course> listCourses;//课程集合

    @BindColor(R.color.white)
    int whiteColor;//白色
    private int themeColor;//主题颜色

    private int topGuideTag = 0;//当前页面标识

    private RequestQueue requestQueue;//volley接口对象

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (dbService == null)
            dbService = DbService.getInstance(getContext());
        isFirstRunning = SharedUtil.getBoolean(getContext(), "isFirstRunning", true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);
        initView();
        initVideoAdapters();
        loadDate();
        return view;
    }

    /**
     * 加载数据
     */
    private void loadDate() {
        if (!CommonUtil.isNetworkAvailable(getActivity())) {
            ((HomeActivity) getActivity()).showToast("当前无网络连接，请连接网络!");
        } else {
            if (requestQueue == null)
                //实例化一个RequestQueue对象
                requestQueue = VolleySingleton.getVolleySingleton(getContext()).getRequestQueue();
            String isTo = ((HomeActivity) getActivity()).getIsTo();
            if (isTo != null && isTo.equals("51Video")) {
                /*showVideo51();*/
            } else if (isTo.equals("FreeVideo")) {
                vpHomeVideo.setCurrentItem(1);
            } else if (isTo.equals("VipVideo")) {
                vpHomeVideo.setCurrentItem(2);
            }
        }
    }

    /**
     * 界面的初始化
     */
    private void initView() {

        viewPagerAdapter = new MyPagerAdapter(getActivity());
        vpHomeVideo.setAdapter(viewPagerAdapter);
        vpHomeVideo.setOnPageChangeListener(pageChangeListener);

        FrameLayout frameLayout;

        //51视频GridView
        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_video_list_51, null);
        ll_volume1 = (LinearLayout) frameLayout.findViewById(R.id.ll_51Volume1);
        ll_volume2 = (LinearLayout) frameLayout.findViewById(R.id.ll_51Volume2);
        ll_volume3 = (LinearLayout) frameLayout.findViewById(R.id.ll_51Volume3);
        ll_volume4 = (LinearLayout) frameLayout.findViewById(R.id.ll_51Volume4);
        ll_volume5 = (LinearLayout) frameLayout.findViewById(R.id.ll_51Volume5);
        ll_volume6 = (LinearLayout) frameLayout.findViewById(R.id.ll_51Volume6);

        ll_volume1.setOnClickListener(onClickListener2);
        ll_volume2.setOnClickListener(onClickListener2);
        ll_volume3.setOnClickListener(onClickListener2);
        ll_volume4.setOnClickListener(onClickListener2);
        ll_volume5.setOnClickListener(onClickListener2);
        ll_volume6.setOnClickListener(onClickListener2);

        viewPagerAdapter.addToAdapterView(frameLayout);

        //积分视频
        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_video_list_integral, null);
        c1_22 = (RelativeLayout) frameLayout.findViewById(R.id.rl_c1_22);
        c3_12 = (RelativeLayout) frameLayout.findViewById(R.id.rl_c3_12);
        c5_18 = (RelativeLayout) frameLayout.findViewById(R.id.rl_c5_18);

        speech1 = (RelativeLayout) frameLayout.findViewById(R.id.rl_speech1);
        speech2 = (RelativeLayout) frameLayout.findViewById(R.id.rl_speech2);
        speech3 = (RelativeLayout) frameLayout.findViewById(R.id.rl_speech3);

        getStudents = (RelativeLayout) frameLayout.findViewById(R.id.getStudents);

        more = (LinearLayout) frameLayout.findViewById(R.id.ll_more);

        c1_22.setOnClickListener(onClickListener);
        c3_12.setOnClickListener(onClickListener);
        c5_18.setOnClickListener(onClickListener);
        speech1.setOnClickListener(onClickListener);
        speech2.setOnClickListener(onClickListener);
        speech3.setOnClickListener(onClickListener);
        getStudents.setOnClickListener(onClickListener);
        more.setOnClickListener(onClickListener);

        gvMemory = (GridView) frameLayout.findViewById(R.id.gv_memory);
        gvMemory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击跳转到视频播放界面
                if (bundle == null)
                    bundle = new Bundle();
                VideoIntegral videoIntegral = adapterMemory.getItem(position);
                bundle.putSerializable("videoIntegral", videoIntegral);
                ((HomeActivity) getActivity()).openActivity(VideoPlayActivity.class, bundle);
            }
        });

        gvMore = (GridView) frameLayout.findViewById(R.id.gv_more);
        gvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击跳转到视频播放界面
                if (bundle == null)
                    bundle = new Bundle();
                VideoIntegral videoIntegral = adapterMore.getItem(position);
                bundle.putSerializable("videoIntegral", videoIntegral);
                ((HomeActivity) getActivity()).openActivity(VideoPlayActivity.class, bundle);
            }
        });

        viewPagerAdapter.addToAdapterView(frameLayout);

        //收费视频ListView
        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_video_list_vip, null);
        lvVipCourses = (ListView) frameLayout.findViewById(R.id.lv_vipCourses);
        lvVipCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击跳转到收费视频列表界面
                if (bundle == null)
                    bundle = new Bundle();
                bundle.putString("result", API.VIDEO_51_BOOK_LIST +
                        vipCoursesAdapter.getItem(position).getId());
                ((HomeActivity)getActivity()).openActivity(Video51Activity.class, bundle);
            }
        });

        viewPagerAdapter.addToAdapterView(frameLayout);


        viewPagerAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (bundle == null)
                bundle = new Bundle();
            switch (v.getId()) {
                case R.id.rl_c1_22:
                    bundle.putString("url", API.VIDEO_51_BOOK_LIST + 153);
                    break;
                case R.id.rl_c3_12:
                    bundle.putString("url", API.VIDEO_51_BOOK_LIST + 168);
                    break;
                case R.id.rl_c5_18:
                    bundle.putString("url", API.VIDEO_51_BOOK_LIST + 195);
                    break;
                case R.id.rl_speech1:
                    bundle.putString("url", API.VIDEO_51_BOOK_LIST + 211);
                    break;
                case R.id.rl_speech2:
                    bundle.putString("url", API.VIDEO_51_BOOK_LIST + 212);
                    break;
                case R.id.rl_speech3:
                    bundle.putString("url", API.VIDEO_51_BOOK_LIST + 213);
                    break;
                case R.id.ll_more:
                    bundle.putString("url", API.VIDEO_51_BOOK_LIST + 215);
                    break;
                case R.id.getStudents:
                    bundle.putString("url", API.VIDEO_51_BOOK_LIST + 218);
            }
            getMyActivity().openActivity(VideoListActivity.class, bundle);
        }
    };

    private View.OnClickListener onClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (bundle == null)
                bundle = new Bundle();
            switch (v.getId()) {
                case R.id.ll_51Volume1:
                    bundle.putString("courses", VIDEO_51_BOOK_LIST + 131);
                    break;
                case R.id.ll_51Volume2:
                    bundle.putString("courses", VIDEO_51_BOOK_LIST + 65);
                    break;
                case R.id.ll_51Volume3:
                    bundle.putString("courses", VIDEO_51_BOOK_LIST + 156);
                    break;
                case R.id.ll_51Volume4:
                    bundle.putString("courses", VIDEO_51_BOOK_LIST + 86);
                    break;
                case R.id.ll_51Volume5:
                    bundle.putString("courses", VIDEO_51_BOOK_LIST + 177);
                    break;
                case R.id.ll_51Volume6:
                    bundle.putString("courses", VIDEO_51_BOOK_LIST + 109);
                    break;
            }
            openActivity(CourseList51Activity.class, bundle);
        }
    };

    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }

    /**
     * ViewPager页面改变的监听
     */
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            topGuideTag = position;
            setTopGuide();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 普通的点击事件
     *
     * @param view
     */
    @OnClick({R.id.tv_video_51, R.id.tv_video_free,
            R.id.tv_video_vip, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_video_51:
                vpHomeVideo.setCurrentItem(0);
                break;
            case R.id.tv_video_free:
                vpHomeVideo.setCurrentItem(1);
                break;
            case R.id.tv_video_vip:
                vpHomeVideo.setCurrentItem(2);
                break;
            case R.id.iv_search:
                ((HomeActivity) getActivity()).openActivity(SearchActivity.class);
                break;
        }
    }

    /**
     * 跳转Activity
     */
    private void openActivity(Class<?> pClass) {
        ((HomeActivity) getActivity()).openActivity(pClass);
    }

    /**
     * 跳转Activity
     */
    private void openActivity(Class<?> pClass, Bundle bundle) {
        ((HomeActivity) getActivity()).openActivity(pClass, bundle);
    }

    /**
     * 显示vip视频数据
     */
    private void showVideoVip() {
        if (!CommonUtil.isNetworkAvailable(getActivity())) {
            ((HomeActivity) getActivity()).showToast("当前无网络连接，请连接网络!");
        } else {
            String urlCourses = API.VIDEO_GET_COURSES + 222;//所有收费课程列表
            if (requestQueue == null)
                requestQueue = VolleySingleton.getVolleySingleton(getContext()).getRequestQueue();
            ((HomeActivity) getActivity()).showLoadingDialog(getContext(), "数据正在加载...", true);//显示加载动画
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlCourses,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            listCourses = ParserCourseList.getCourseList(jsonObject.toString());
                            vipCoursesAdapter.appendDataed(listCourses, true);
                            vipCoursesAdapter.updateAdapter();
                            ((HomeActivity) getActivity()).cancelDialog();
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
     * 显示积分视频数据
     */
    private void showVideoFree() {
        String urlMemory = API.VIDEO_51_BOOK_LIST + "214";
        ((HomeActivity) getActivity()).showLoadingDialog(getContext(), "数据正在加载...", true);//显示加载动画
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlMemory,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        courseInfo = ParserCourseInfo.getCourseInfo(jsonObject.toString());
                        listMemory = courseInfo.getVideo();
                        adapterMemory.appendDataed(listMemory, true);
                        adapterMemory.updateAdapter();
                        ((HomeActivity) getActivity()).cancelDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);

        String urlMore = API.VIDEO_51_BOOK_LIST + "215";
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, urlMore,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        courseInfo = ParserCourseInfo.getCourseInfo(jsonObject.toString());
                        listMore = courseInfo.getVideo();
                        if (listMore.size() > 3) {
                            adapterMore.appendDataed(listMore.get(listMore.size() - 1), true);
                            adapterMore.appendDataed(listMore.get(listMore.size() - 2), false);
                            adapterMore.appendDataed(listMore.get(listMore.size() - 3), false);
                        } else {
                            adapterMore.appendDataed(listMore, true);
                        }
                        adapterMore.updateAdapter();
                        ((HomeActivity) getActivity()).cancelDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }
        );
        requestQueue.add(jsonObjectRequest2);
    }


    /**
     * 配置各列表适配器
     */
    private void initVideoAdapters() {

        //记忆力小明星适配器
        if (adapterMemory == null)
            adapterMemory = new HListViewAdapter_Integral(getContext());
        gvMemory.setAdapter(adapterMemory);

        //其他积分视频适配器
        if (adapterMore == null)
            adapterMore = new HListViewAdapter_Integral(getContext());
        gvMore.setAdapter(adapterMore);

        if (vipCoursesAdapter == null)
            vipCoursesAdapter = new ListCourseAdapter(getActivity());
        lvVipCourses.setAdapter(vipCoursesAdapter);

        topGuideTag = 0;
        setTopGuide();
    }

    /**
     * 设置上方所有导航标题为未选中状态
     */
    private void setDefaultTopGuide() {
        themeColor = ((HomeActivity) getActivity()).getThemeColor();

        tvVideo51.setBackgroundResource(R.drawable.text_view_border_51);
        tvVideoFree.setBackgroundResource(R.drawable.text_view_border_free);
        tvVideoVip.setBackgroundResource(R.drawable.text_view_border_vip);

        tvVideo51.setTextColor(whiteColor);
        tvVideoFree.setTextColor(whiteColor);
        tvVideoVip.setTextColor(whiteColor);
    }

    //设置上标题和对应数据加载
    private void setTopGuide() {
        setDefaultTopGuide();
        switch (topGuideTag) {
            case 0:
                tvVideo51.setBackgroundResource(R.drawable.text_view_back_51);
                tvVideo51.setTextColor(themeColor);
                break;
            case 1:
                tvVideoFree.setBackgroundResource(R.drawable.text_view_back_free);
                tvVideoFree.setTextColor(themeColor);
                showVideoFree();
                break;
            case 2:
                tvVideoVip.setBackgroundResource(R.drawable.text_view_back_vip);
                tvVideoVip.setTextColor(themeColor);
                showVideoVip();
                break;
        }
    }
}
