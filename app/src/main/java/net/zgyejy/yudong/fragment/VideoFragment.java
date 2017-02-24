package net.zgyejy.yudong.fragment;

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
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import net.zgyejy.yudong.R;
import net.zgyejy.yudong.activity.CourseList51Activity;
import net.zgyejy.yudong.activity.HomeActivity;
import net.zgyejy.yudong.activity.Video51TrialListActivity;
import net.zgyejy.yudong.activity.VideoListActivity;
import net.zgyejy.yudong.activity.VideoPlayActivity;
import net.zgyejy.yudong.adapter.GridViewAdapter_Video;
import net.zgyejy.yudong.adapter.MyPagerAdapter;
import me.yejy.greendao.VideoIntegral;
import net.zgyejy.yudong.modle.parser.ParserCourseInfo;
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
    private boolean isFirstResume = true;
    private Bundle bundle;
    private DbService dbService;//数据库管理工具

    @BindView(R.id.tv_video_51)
    TextView tvVideo51;
    @BindView(R.id.tv_video_quality)
    TextView tvVideoQuality;
    @BindView(R.id.tv_video_free)
    TextView tvVideoFree;
    @BindView(R.id.tv_video_act)
    TextView tvVideoAct;

    @BindView(R.id.vp_home_video)
    ViewPager vpHomeVideo;//左右滑动界面
    //滑动界面适配器
    private MyPagerAdapter viewPagerAdapter;


    //五个一视频
    private LinearLayout ll_volume1, ll_volume2, ll_volume3,
            ll_volume4, ll_volume5, ll_volume6;//1到6册的view

    //精品课程
    private GridView videoRecommend, //推荐课程
            videoCloudMeeting, //云端会议视频
            videoMoreQuality; //更多精品课程
    private GridViewAdapter_Video adapterRecommend,//相应适配器
            adapterCloudMeeting,
            adapterMoreQuality;

    //免费课程
    private LinearLayout ll51T_c1_22,//五个一视频试用版
            ll51T_c3_12,
            ll51T_c5_18;


    //活动视频
    private LinearLayout ll_speech1,//演讲初赛
            ll_speech2,//演讲复赛
            ll_speech3,//演讲决赛
            ll_61Train,//61培训
            ll_moreAct;//更多活动
    private GridView videoMemory, //记忆力小明星
            videoMoreAct; //更多精彩活动
    private GridViewAdapter_Video adapterMemory,//相应适配器
            adapterMoreAct;
    private List<VideoIntegral> listMemory, //相应视频数据集合
            listMoreAct;
    private boolean isMemoryDataLoaded = false,isMoreActDataLoaded = false;


    //界面切换相关
    @BindColor(R.color.white)
    int whiteColor;//白色
    private int themeColor;//主题颜色
    private int topGuideTag = 0;//当前页面标识

    //网络请求相关
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            initView();
            initAdapters();
            //初始化标题栏
            topGuideTag = 0;
            loadDate();
            isFirstResume = false;
        }
    }

    /**
     * 根据传递过来的isTo数据加载数据
     * 五个一数据固定，无需再加载
     */
    private void loadDate() {
        if (!CommonUtil.isNetworkAvailable(getActivity())) {
            ((HomeActivity) getActivity()).showToast("当前无网络连接，请连接网络!");
        } else {
            if (requestQueue == null)
                //实例化一个RequestQueue对象
                requestQueue = VolleySingleton.getVolleySingleton(getContext()).getRequestQueue();
            String isTo = ((HomeActivity) getActivity()).getIsTo();
            if ("VideoQuality".equals(isTo)) {
                vpHomeVideo.setCurrentItem(1);
            } else if ("VideoFree".equals(isTo)) {
                vpHomeVideo.setCurrentItem(2);
            } else if ("VideoAct".equals(isTo)) {
                vpHomeVideo.setCurrentItem(3);
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

        //五个一视频
        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_video_list_51, null);
        ll_volume1 = (LinearLayout) frameLayout.findViewById(R.id.ll_51Volume1);
        ll_volume2 = (LinearLayout) frameLayout.findViewById(R.id.ll_51Volume2);
        ll_volume3 = (LinearLayout) frameLayout.findViewById(R.id.ll_51Volume3);
        ll_volume4 = (LinearLayout) frameLayout.findViewById(R.id.ll_51Volume4);
        ll_volume5 = (LinearLayout) frameLayout.findViewById(R.id.ll_51Volume5);
        ll_volume6 = (LinearLayout) frameLayout.findViewById(R.id.ll_51Volume6);

        ll_volume1.setOnClickListener(onClickListener51);
        ll_volume2.setOnClickListener(onClickListener51);
        ll_volume3.setOnClickListener(onClickListener51);
        ll_volume4.setOnClickListener(onClickListener51);
        ll_volume5.setOnClickListener(onClickListener51);
        ll_volume6.setOnClickListener(onClickListener51);

        viewPagerAdapter.addToAdapterView(frameLayout);

        //精品课程
        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_video_quality, null);
        videoRecommend = (GridView) frameLayout.findViewById(R.id.gv_video_recommend); //推荐课程
        videoCloudMeeting = (GridView) frameLayout.findViewById(R.id.gv_video_cloudMeeting); //云端会议视频
        videoMoreQuality = (GridView) frameLayout.findViewById(R.id.gv_video_moreQuality); //更多精品课程

        //设置ItemClickListener跳转到视频介绍界面
        //设置适配器


        viewPagerAdapter.addToAdapterView(frameLayout);

        //免费课程
        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_video_free, null);
        ll51T_c1_22 = (LinearLayout) frameLayout.findViewById(R.id.ll_51T_c1_22);
        ll51T_c3_12 = (LinearLayout) frameLayout.findViewById(R.id.ll_51T_c3_12);
        ll51T_c5_18 = (LinearLayout) frameLayout.findViewById(R.id.ll_51T_c5_18);
        ll51T_c1_22.setOnClickListener(onClickListener51T);
        ll51T_c3_12.setOnClickListener(onClickListener51T);
        ll51T_c5_18.setOnClickListener(onClickListener51T);

        viewPagerAdapter.addToAdapterView(frameLayout);

        //往期活动视频
        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_video_act, null);
        ll_speech1 = (LinearLayout) frameLayout.findViewById(R.id.ll_free_speech1);
        ll_speech2 = (LinearLayout) frameLayout.findViewById(R.id.ll_free_speech2);
        ll_speech3 = (LinearLayout) frameLayout.findViewById(R.id.ll_free_speech3);
        ll_61Train = (LinearLayout) frameLayout.findViewById(R.id.ll_free_61train);
        ll_moreAct = (LinearLayout) frameLayout.findViewById(R.id.ll_moreAct);
        ll_speech1.setOnClickListener(onClickListener_VideoList);
        ll_speech2.setOnClickListener(onClickListener_VideoList);
        ll_speech3.setOnClickListener(onClickListener_VideoList);
        ll_61Train.setOnClickListener(onClickListener_VideoList);
        ll_moreAct.setOnClickListener(onClickListener_VideoList);

        videoMemory = (GridView) frameLayout.findViewById(R.id.gv_video_memory);
        videoMemory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (bundle == null)
                    bundle = new Bundle();
                VideoIntegral videoIntegral = adapterMemory.getItem(position);
                bundle.putSerializable("videoIntegral", videoIntegral);
                getMyActivity().openActivity(VideoPlayActivity.class, bundle);
            }
        });
        videoMoreAct = (GridView) frameLayout.findViewById(R.id.gv_video_moreAct);
        videoMoreAct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (bundle == null)
                    bundle = new Bundle();
                VideoIntegral videoIntegral = adapterMoreAct.getItem(position);
                bundle.putSerializable("videoIntegral", videoIntegral);
                getMyActivity().openActivity(VideoPlayActivity.class, bundle);
            }
        });

        viewPagerAdapter.addToAdapterView(frameLayout);


        viewPagerAdapter.notifyDataSetChanged();
    }


    /**
     * 五个一视频书页的点击事件
     */
    private View.OnClickListener onClickListener51 = new View.OnClickListener() {
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
            getMyActivity().openActivity(CourseList51Activity.class, bundle);
        }
    };

    /**
     * 五个一视频试用版的点击事件
     */
    private View.OnClickListener onClickListener51T = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (bundle == null)
                bundle = new Bundle();
            switch (v.getId()) {
                case R.id.ll_51T_c1_22:
                    bundle.putString("course", VIDEO_51_BOOK_LIST + 153);
                    break;
                case R.id.ll_51T_c3_12:
                    bundle.putString("course", VIDEO_51_BOOK_LIST + 168);
                    break;
                case R.id.ll_51T_c5_18:
                    bundle.putString("course", VIDEO_51_BOOK_LIST + 195);
                    break;
            }
            getMyActivity().openActivity(Video51TrialListActivity.class, bundle);
        }
    };

    /**
     * 跳转到相应视频列表的点击事件
     */
    private View.OnClickListener onClickListener_VideoList = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (bundle == null)
                bundle = new Bundle();
            switch (v.getId()) {
                case R.id.ll_free_speech1:
                    bundle.putString("url", VIDEO_51_BOOK_LIST + 211);
                    break;
                case R.id.ll_free_speech2:
                    bundle.putString("url", VIDEO_51_BOOK_LIST + 212);
                    break;
                case R.id.ll_free_speech3:
                    bundle.putString("url", VIDEO_51_BOOK_LIST + 213);
                    break;
                case R.id.ll_free_61train:
                    bundle.putString("url", VIDEO_51_BOOK_LIST + 218);
                    break;
                case R.id.ll_moreAct:
                    bundle.putString("url", VIDEO_51_BOOK_LIST + 215);
                    break;
            }
            getMyActivity().openActivity(VideoListActivity.class, bundle);
        }
    };

    /**
     * 获取当前Activity
     *
     * @return
     */
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
    @OnClick({R.id.tv_video_51, R.id.tv_video_quality,
            R.id.tv_video_free, R.id.tv_video_act})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_video_51:
                vpHomeVideo.setCurrentItem(0);
                break;
            case R.id.tv_video_quality:
                vpHomeVideo.setCurrentItem(1);
                break;
            case R.id.tv_video_free:
                vpHomeVideo.setCurrentItem(2);
                break;
            case R.id.tv_video_act:
                vpHomeVideo.setCurrentItem(3);
                break;
        }
    }

    /**
     * 显示精品合成视频数据
     */
    private void showVideoQuality() {

    }

    /**
     * 显示往期活动视频数据
     */
    private void showVideoAct() {
        String urlMemory = VIDEO_51_BOOK_LIST + 214;
        String urlMoreAct = VIDEO_51_BOOK_LIST + 215;
        if (requestQueue == null)
            //实例化一个RequestQueue对象
            requestQueue = VolleySingleton.getVolleySingleton(getContext()).getRequestQueue();
        getMyActivity().showLoadingDialog(getContext(), "数据正在加载...", true);//显示加载动画
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, urlMemory,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listMemory = ParserCourseInfo.getCourseInfo(jsonObject.toString())
                                .getVideo();
                        adapterMemory.appendDataed(listMemory,true);
                        adapterMemory.notifyDataSetChanged();
                        isMemoryDataLoaded = true;
                        if (isMoreActDataLoaded)
                            getMyActivity().cancelDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }
        );
        requestQueue.add(jsonObjectRequest1);

        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, urlMoreAct,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        listMoreAct = ParserCourseInfo.getCourseInfo(jsonObject.toString())
                                .getVideo();
                        if (listMoreAct.size()>3) {
                            adapterMoreAct.appendDataed(listMoreAct.get(listMoreAct.size()-1),true);
                            adapterMoreAct.appendDataed(listMoreAct.get(listMoreAct.size()-2),false);
                            adapterMoreAct.appendDataed(listMoreAct.get(listMoreAct.size()-3),false);
                        }else {
                            adapterMoreAct.appendDataed(listMoreAct,true);
                        }
                        adapterMoreAct.notifyDataSetChanged();
                        isMoreActDataLoaded = true;
                        if (isMemoryDataLoaded)
                            getMyActivity().cancelDialog();
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
    private void initAdapters() {
        /*//精品课程适配
        setAdapter(videoRecommend, adapterRecommend);
        setAdapter(videoCloudMeeting, adapterCloudMeeting);
        setAdapter(videoMoreQuality, adapterMoreQuality);*/

        //往期活动适配
        if (adapterMemory == null)
            adapterMemory = new GridViewAdapter_Video(getContext());
        videoMemory.setAdapter(adapterMemory);

        if (adapterMoreAct == null)
            adapterMoreAct = new GridViewAdapter_Video(getContext());
        videoMoreAct.setAdapter(adapterMoreAct);
    }

    /**
     * 设置上方所有导航标题为未选中状态
     */
    private void setDefaultTopGuide() {
        themeColor = ((HomeActivity) getActivity()).getThemeColor();

        tvVideo51.setBackgroundResource(R.drawable.text_view_border_51);
        tvVideoQuality.setBackgroundResource(R.drawable.text_view_border_free);
        tvVideoFree.setBackgroundResource(R.drawable.text_view_border_free);
        tvVideoAct.setBackgroundResource(R.drawable.text_view_border_vip);

        tvVideo51.setTextColor(whiteColor);
        tvVideoQuality.setTextColor(whiteColor);
        tvVideoFree.setTextColor(whiteColor);
        tvVideoAct.setTextColor(whiteColor);
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
                tvVideoQuality.setBackgroundResource(R.drawable.text_view_back_free);
                tvVideoQuality.setTextColor(themeColor);
                showVideoQuality();
                break;
            case 2:
                tvVideoFree.setBackgroundResource(R.drawable.text_view_back_free);
                tvVideoFree.setTextColor(themeColor);
                break;
            case 3:
                tvVideoAct.setBackgroundResource(R.drawable.text_view_back_vip);
                tvVideoAct.setTextColor(themeColor);
                showVideoAct();
                break;
        }
    }
}
