package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.ListMp3Adapter;
import net.zgyejy.yudong.adapter.ListViewAdapter_Integral;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.modle.CourseInfo;
import net.zgyejy.yudong.modle.Mp3Text;
import net.zgyejy.yudong.modle.parser.ParserCourseInfo;
import net.zgyejy.yudong.util.CommonUtil;
import net.zgyejy.yudong.util.VolleySingleton;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Mp3TxtActivity extends MyBaseActivity {

    @BindView(R.id.mp3List_title)
    TextView mp3ListTitle;
    @BindView(R.id.mp3List)
    ListView mp3List;

    private String url;
    private ListMp3Adapter adapter;

    private RequestQueue requestQueue;//volley接口对象
    private List<Mp3Text> mp3Texts;//mp3Txt列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3_txt);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");
        mp3ListTitle.setText(bundle.getString("title"));//设置标题
        initView();
        loadListData();
    }

    private void initView() {
        if (adapter == null)
            adapter = new ListMp3Adapter(this);
        mp3List.setAdapter(adapter);
        mp3List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("mp3Txt",mp3Texts.get(position));
                openActivity(Mp3PlayActivity.class,bundle);
            }
        });
    }

    private void loadListData() {
        if (!CommonUtil.isNetworkAvailable(this)) {
            showToast("当前无网络连接，请连接网络!");
        } else {
            if (requestQueue == null)
                //实例化一个RequestQueue对象
                requestQueue = VolleySingleton.getVolleySingleton(this).getRequestQueue();
            showLoadingDialog(this, "数据正在加载...", true);//显示加载动画
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            CourseInfo courseInfo = ParserCourseInfo.getCourseInfo(jsonObject.toString());
                            mp3Texts = courseInfo.getMp3text();
                            adapter.appendDataed(mp3Texts, true);
                            adapter.updateAdapter();
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

    @OnClick(R.id.mp3ListBack)
    public void onClick() {
        finish();
    }
}
