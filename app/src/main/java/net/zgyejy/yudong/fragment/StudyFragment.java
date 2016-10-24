package net.zgyejy.yudong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.activity.HomeActivity;
import net.zgyejy.yudong.adapter.ArticleListAdapter;
import net.zgyejy.yudong.adapter.MyPagerAdapter;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.modle.Article;
import net.zgyejy.yudong.modle.parser.ParserArticleLists;
import net.zgyejy.yudong.util.CommonUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class StudyFragment extends Fragment {
    private static final String TAG = "StudyFragment";

    @BindView(R.id.tv_study_principal)
    TextView tvStudyPrincipal;
    @BindView(R.id.tv_study_teacher)
    TextView tvStudyTeacher;
    @BindView(R.id.tv_study_parents)
    TextView tvStudyParents;
    @BindView(R.id.tv_study_kid)
    TextView tvStudyKid;
    @BindView(R.id.vp_study_list)
    ViewPager vpStudyList;

    private MyPagerAdapter viewPagerAdapter;//ViewPager适配器

    TextView tvStudyChange;//改变父母学习列表界面显示内容

    private PullToRefreshListView lvStudyPrincipal, lvStudyTeacher,
            lvStudyParents, lvStudyKid;//各布局对应的列表

    private ArticleListAdapter lvPrincipalAdapter, lvTeacherAdapter,
            lvParentsAdapter, lvSPAdapter;
    private List<Article> principalArticles, teacherArticles,
            parentsArticles, spArticles;
    private RequestQueue requestQueue;//volley接口对象
    private int PageNumPrincipal, PageNumTeacher, PageNumParents, PageNumSp, PageNumKid;//当前列表加载页数

    //private KidListAdapter lvKidAdapter;
    //private List<KidVideo> kidVideos;

    @BindColor(R.color.white)
    int whiteColor;//白色
    private int themeColor;//主题颜色

    private int topGuidTag = 0;//当前页面标识
    private int parentsTag = 0;//父母学习列表显示界面标识

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study, container, false);
        ButterKnife.bind(this, view);
        initView();
        initVideoAdapters();
        loadDate();
        return view;
    }

    /**
     * 所有界面的初始化
     */
    private void initView() {

        viewPagerAdapter = new MyPagerAdapter(getActivity());
        vpStudyList.setAdapter(viewPagerAdapter);
        vpStudyList.setOnPageChangeListener(pageChangeListener);

        FrameLayout frameLayout;

        //园长学习列表界面的初始化
        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_study_principal, null);
        lvStudyPrincipal = (PullToRefreshListView) frameLayout.findViewById(R.id.lv_study_principal);
        initLvRefresh(lvStudyPrincipal);
        lvStudyPrincipal.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PageNumPrincipal = 1;
                showStudyPrincipal();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PageNumPrincipal++;
                showStudyPrincipal();
            }
        });
        lvStudyPrincipal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //打开条目对应网页的方法
            }
        });
        viewPagerAdapter.addToAdapterView(frameLayout);

        //幼师学习列表界面的初始化
        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_study_teacher, null);
        lvStudyTeacher = (PullToRefreshListView) frameLayout.findViewById(R.id.lv_study_teacher);
        initLvRefresh(lvStudyTeacher);
        lvStudyTeacher.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        lvStudyTeacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //打开条目对应网页的方法
            }
        });
        viewPagerAdapter.addToAdapterView(frameLayout);

        //父母学习列表界面的初始化
        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_study_parents, null);
        lvStudyParents = (PullToRefreshListView) frameLayout.findViewById(R.id.lv_study_parents);
        initLvRefresh(lvStudyParents);
        lvStudyParents.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        lvStudyParents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //打开条目对应网页的方法
            }
        });
        tvStudyChange = (TextView) frameLayout.findViewById(R.id.tv_study_change);
        tvStudyChange.setOnClickListener(new View.OnClickListener() {
            //根据设定的标识改变父母学习页面的显示内容
            @Override
            public void onClick(View v) {
                switch (parentsTag) {
                    case 0:
                        //适配列表内容为父母学堂
                        parentsTag = 1;
                        break;
                    case 1:
                        //适配列表内容为家园共育
                        parentsTag = 0;
                        break;
                }
            }
        });
        viewPagerAdapter.addToAdapterView(frameLayout);

        //孩子学习列表界面的初始化
        frameLayout = (FrameLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.layout_study_kid, null);
        lvStudyKid = (PullToRefreshListView) frameLayout.findViewById(R.id.lv_study_kid);
        initLvRefresh(lvStudyKid);
        lvStudyKid.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        lvStudyKid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //打开条目对应网页的方法
            }
        });
        viewPagerAdapter.addToAdapterView(frameLayout);

        viewPagerAdapter.notifyDataSetChanged();
    }

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
     * 配置各列表适配器，初始化51视频界面
     */
    private void initVideoAdapters() {

        //配置园长管理列表适配器
        if (lvPrincipalAdapter == null)
            lvPrincipalAdapter = new ArticleListAdapter(getContext());
        lvStudyPrincipal.setAdapter(lvPrincipalAdapter);
    }

    /**
     * 初始化列表中的数据
     */
    private void loadDate() {
        if (!CommonUtil.isNetworkAvailable(getActivity())) {
            ((HomeActivity) getActivity()).showToast("当前无网络连接，请连接网络!");
        } else {
            requestQueue = Volley.newRequestQueue(getContext());//实例化一个RequestQueue对象
            PageNumPrincipal = 1;
            showStudyPrincipal();
        }
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
            topGuidTag = position;
            setTopGuide();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    //设置上标题和对应数据加载
    private void setTopGuide() {
        setDefaultTopGuid();
        switch (topGuidTag) {
            case 0:
                tvStudyPrincipal.setBackgroundResource(R.drawable.text_view_back_51);
                tvStudyPrincipal.setTextColor(whiteColor);
                showStudyPrincipal();
                break;
            case 1:
                tvStudyTeacher.setBackgroundResource(R.drawable.text_view_back_free);
                tvStudyTeacher.setTextColor(whiteColor);
                showStudyTeacher();
                break;
            case 2:
                tvStudyParents.setBackgroundResource(R.drawable.text_view_back_free);
                tvStudyParents.setTextColor(whiteColor);
                showStudyParents();
                break;
            case 3:
                tvStudyKid.setBackgroundResource(R.drawable.text_view_back_vip);
                tvStudyKid.setTextColor(whiteColor);
                showStudyKid();
                break;
        }
    }

    /**
     * 请求、添加园长学习列表的数据
     */
    private void showStudyPrincipal() {
        String url = API.STUDY_ARTICLE_PRINCIPAL + "&page=" + PageNumPrincipal;

        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray> () {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.i(TAG, "onResponse: ---------------" + response);
                    List<Article> list = ParserArticleLists.getArticleList(response.toString());
                    if (principalArticles == null)
                        principalArticles = new ArrayList<>();
                    if (PageNumPrincipal == 1)
                        principalArticles.clear();
                    principalArticles.addAll(list);
                    lvPrincipalAdapter.appendDataed(principalArticles, true);
                    lvPrincipalAdapter.updateAdapter();
                    lvStudyPrincipal.onRefreshComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });


        /*JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );*/
        requestQueue.add(req);
    }

    /**
     * 请求、添加幼师学习列表的数据
     */
    private void showStudyTeacher() {

    }

    /**
     * 请求、添加父母学习列表的数据
     */
    private void showStudyParents() {

    }

    /**
     * 请求、添加幼儿学习列表的数据
     */
    private void showStudyKid() {

    }

    /**
     * 设置上方所有导航标题为未选中状态
     */
    private void setDefaultTopGuid() {
        themeColor = ((HomeActivity) getActivity()).getThemeColor();

        tvStudyPrincipal.setBackgroundResource(R.drawable.text_view_border_51);
        tvStudyTeacher.setBackgroundResource(R.drawable.text_view_border_free);
        tvStudyParents.setBackgroundResource(R.drawable.text_view_border_free);
        tvStudyKid.setBackgroundResource(R.drawable.text_view_border_vip);

        tvStudyPrincipal.setTextColor(themeColor);
        tvStudyTeacher.setTextColor(themeColor);
        tvStudyParents.setTextColor(themeColor);
        tvStudyKid.setTextColor(themeColor);
    }

    /**
     * 根据点击的按钮，切换相应的ViewPager
     *
     * @param view
     */
    @OnClick({R.id.tv_study_principal, R.id.tv_study_teacher, R.id.tv_study_parents, R.id.tv_study_kid})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_study_principal:
                vpStudyList.setCurrentItem(0);
                break;
            case R.id.tv_study_teacher:
                vpStudyList.setCurrentItem(1);
                break;
            case R.id.tv_study_parents:
                vpStudyList.setCurrentItem(2);
                break;
            case R.id.tv_study_kid:
                vpStudyList.setCurrentItem(3);
                break;
        }
    }
}
