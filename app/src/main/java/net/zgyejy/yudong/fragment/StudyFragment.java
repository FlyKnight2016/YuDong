package net.zgyejy.yudong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import net.zgyejy.yudong.activity.WebReadActivity;
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
    Bundle bundle;

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

    TextView tvStudySelectParents, tvStudySelectSp;//改变父母学习列表界面显示内容

    private PullToRefreshListView lvStudyPrincipal, lvStudyTeacher,
            lvStudyParents, lvStudyKid;//各布局对应的列表

    private ArticleListAdapter lvPrincipalAdapter, lvTeacherAdapter,
            lvParentsAdapter;
    private List<Article> principalArticles, teacherArticles,
            parentsArticles, spArticles;
    private RequestQueue requestQueue;//volley接口对象
    private int PageNumPrincipal = 1, PageNumTeacher = 1, PageNumParents = 1,
            PageNumSp = 1, PageNumKid = 1;//当前列表加载页数(默认为1)

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
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                //打开条目对应网页的方法
                if (bundle == null)
                    bundle = new Bundle();
                Article article = principalArticles.get(i);
                bundle.putString("url",API.ZgyejyNetIP + article.getUrl());
                ((HomeActivity) getActivity()).openActivity(WebReadActivity.class, bundle);
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
                PageNumTeacher = 1;
                showStudyTeacher();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PageNumTeacher++;
                showStudyTeacher();
            }
        });
        lvStudyTeacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                //打开对应的网页
                if (bundle == null)
                    bundle = new Bundle();
                Article article = teacherArticles.get(i);
                bundle.putString("url", API.ZgyejyNetIP + article.getUrl());
                ((HomeActivity) getActivity()).openActivity(WebReadActivity.class, bundle);
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
                switch (parentsTag) {
                    case 0:
                        PageNumParents = 1;
                        break;
                    case 1:
                        PageNumSp = 1;
                        break;
                }
                showStudyParents();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                switch (parentsTag) {
                    case 0:
                        PageNumParents++;
                        break;
                    case 1:
                        PageNumSp++;
                        break;
                }
                showStudyParents();
            }
        });
        lvStudyParents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                //打开条目对应网页的方法
                if (bundle == null)
                    bundle = new Bundle();
                Article article;
                if (parentsTag == 0) {
                    article = parentsArticles.get(i);
                }else {
                    article = spArticles.get(i);
                }
                bundle.putString("url", API.ZgyejyNetIP + article.getUrl());
                ((HomeActivity) getActivity()).openActivity(WebReadActivity.class, bundle);
            }
        });
        tvStudySelectParents = (TextView) frameLayout.findViewById(R.id.tv_study_select_parents);
        tvStudySelectSp = (TextView) frameLayout.findViewById(R.id.tv_study_select_sp);
        tvStudySelectParents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvStudySelectParents.setBackgroundResource(R.color.white);
                tvStudySelectSp.setBackgroundResource(R.color.lightGray);
                parentsTag = 0;
                showStudyParents();
            }
        });
        tvStudySelectSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvStudySelectSp.setBackgroundResource(R.color.white);
                tvStudySelectParents.setBackgroundResource(R.color.lightGray);
                parentsTag = 1;
                showStudyParents();
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

    /**
     * 设置刷新参数
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
     * 配置各列表适配器，初始化51视频界面
     */
    private void initVideoAdapters() {

        //配置园长管理列表适配器
        if (lvPrincipalAdapter == null)
            lvPrincipalAdapter = new ArticleListAdapter(getContext());
        lvStudyPrincipal.setAdapter(lvPrincipalAdapter);

        //配置幼师之路列表适配器
        if (lvTeacherAdapter == null)
            lvTeacherAdapter = new ArticleListAdapter(getContext());
        lvStudyTeacher.setAdapter(lvTeacherAdapter);

        //配置父母学堂和家园共育列表适配器
        if (lvParentsAdapter == null)
            lvParentsAdapter = new ArticleListAdapter(getContext());
        lvStudyParents.setAdapter(lvParentsAdapter);
    }

    /**
     * 初始化列表中的数据
     */
    private void loadDate() {
        if (!CommonUtil.isNetworkAvailable(getActivity())) {
            ((HomeActivity) getActivity()).showToast("当前无网络连接，请连接网络!");
        } else {
            requestQueue = Volley.newRequestQueue(getContext());//实例化一个RequestQueue对象
            String isTo = ((HomeActivity)getActivity()).getIsTo();
            if (isTo.equals("StudyFragment")||isTo.equals("Principal")){
                showStudyPrincipal();
            }else if (isTo.equals("Teacher")) {
                vpStudyList.setCurrentItem(1);
            }else if (isTo.equals("Parents")) {
                vpStudyList.setCurrentItem(2);
            }else if (isTo.equals("Kid")) {
                vpStudyList.setCurrentItem(3);
            }else {
                showStudyPrincipal();
            }
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
        setDefaultTopGuide();
        switch (topGuidTag) {
            case 0:
                tvStudyPrincipal.setBackgroundResource(R.drawable.text_view_back_51);
                tvStudyPrincipal.setTextColor(themeColor);
                showStudyPrincipal();
                break;
            case 1:
                tvStudyTeacher.setBackgroundResource(R.drawable.text_view_back_free);
                tvStudyTeacher.setTextColor(themeColor);
                showStudyTeacher();
                break;
            case 2:
                tvStudyParents.setBackgroundResource(R.drawable.text_view_back_free);
                tvStudyParents.setTextColor(themeColor);
                showStudyParents();
                break;
            case 3:
                tvStudyKid.setBackgroundResource(R.drawable.text_view_back_vip);
                tvStudyKid.setTextColor(themeColor);
                showStudyKid();
                break;
        }
    }

    /**
     * 显示加载动画
     */
    private void showLoadingDialog() {
        ((HomeActivity) getActivity()).showLoadingDialog(getContext(), "数据正在加载...", true);
    }

    /**
     * 取消加载动画
     */
    private void cancelDialog() {
        ((HomeActivity) getActivity()).cancelDialog();
    }

    /**
     * 请求、添加园长学习列表的数据
     */
    private void showStudyPrincipal() {
        showLoadingDialog();
        String url = API.STUDY_ARTICLE_PRINCIPAL + "&page=" + PageNumPrincipal;

        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    List<Article> list = ParserArticleLists.getArticleList(response.toString());
                    if (principalArticles == null)
                        principalArticles = new ArrayList<>();
                    if (PageNumPrincipal == 1)
                        principalArticles.clear();
                    principalArticles.addAll(list);
                    lvPrincipalAdapter.appendDataed(principalArticles, true);
                    lvPrincipalAdapter.updateAdapter();
                    lvStudyPrincipal.onRefreshComplete();
                    cancelDialog();
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
        requestQueue.add(req);
    }

    /**
     * 请求、添加幼师学习列表的数据
     */
    private void showStudyTeacher() {
        showLoadingDialog();
        String url = API.STUDY_ARTICLE_TEACHER + "&page=" + PageNumTeacher;

        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    List<Article> list = ParserArticleLists.getArticleList(response.toString());
                    if (teacherArticles == null)
                        teacherArticles = new ArrayList<>();
                    if (PageNumTeacher == 1)
                        teacherArticles.clear();
                    teacherArticles.addAll(list);
                    lvTeacherAdapter.appendDataed(teacherArticles, true);
                    lvTeacherAdapter.updateAdapter();
                    lvStudyTeacher.onRefreshComplete();
                    cancelDialog();
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
        requestQueue.add(req);
    }

    /**
     * 请求、添加父母学习列表的数据
     */
    private void showStudyParents() {
        showLoadingDialog();
        switch (parentsTag) {
            //Tag为0，显示父母学堂的列表内容
            case 0:
                String url = API.STUDY_ARTICLE_PARENTS + "&page=" + PageNumParents;

                JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<Article> list = ParserArticleLists.getArticleList(response.toString());
                            if (parentsArticles == null)
                                parentsArticles = new ArrayList<>();
                            if (PageNumParents == 1)
                                parentsArticles.clear();
                            parentsArticles.addAll(list);
                            lvParentsAdapter.appendDataed(parentsArticles, true);
                            lvParentsAdapter.updateAdapter();
                            lvStudyParents.onRefreshComplete();
                            cancelDialog();
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
                requestQueue.add(req);
                break;
            //Tag为1，显示家园共育的列表内容
            case 1:
                String url2 = API.STUDY_ARTICLE_SP + "&page=" + PageNumSp;

                JsonArrayRequest req2 = new JsonArrayRequest(url2, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<Article> list = ParserArticleLists.getArticleList(response.toString());
                            if (spArticles == null)
                                spArticles = new ArrayList<>();
                            if (PageNumSp == 1)
                                spArticles.clear();
                            spArticles.addAll(list);
                            lvParentsAdapter.appendDataed(spArticles, true);
                            lvParentsAdapter.updateAdapter();
                            lvStudyParents.onRefreshComplete();
                            cancelDialog();
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
                requestQueue.add(req2);
                break;
        }
    }

    /**
     * 请求、添加幼儿学习列表的数据
     */
    private void showStudyKid() {
        //showLoadingDialog();
        //cancelDialog();
    }

    /**
     * 设置上方所有导航标题为未选中状态
     */
    private void setDefaultTopGuide() {
        themeColor = ((HomeActivity) getActivity()).getThemeColor();

        tvStudyPrincipal.setBackgroundResource(R.drawable.text_view_border_51);
        tvStudyTeacher.setBackgroundResource(R.drawable.text_view_border_free);
        tvStudyParents.setBackgroundResource(R.drawable.text_view_border_free);
        tvStudyKid.setBackgroundResource(R.drawable.text_view_border_vip);

        tvStudyPrincipal.setTextColor(whiteColor);
        tvStudyTeacher.setTextColor(whiteColor);
        tvStudyParents.setTextColor(whiteColor);
        tvStudyKid.setTextColor(whiteColor);
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
