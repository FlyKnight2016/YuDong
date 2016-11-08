package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.util.SharedUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AboutUsActivity extends MyBaseActivity {
    Handler handler;

    @BindView(R.id.tv_copyRight)
    TextView tvCopyRight;
    @BindView(R.id.iv_open_copyRight)
    ImageView ivOpenCopyRight;
    @BindView(R.id.sv_about)
    ScrollView svAbout;
    private int copyRightTag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_about_back, R.id.rl_about_toLead, R.id.rl_about_copyright})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_about_back:
                finish();
                break;
            case R.id.rl_about_toLead:
                SharedUtil.putBoolean(this, "isFirst", true);
                Bundle bundle = new Bundle();
                bundle.putString("AboutUsFrom", "AboutUsActivity");
                openActivity(LeadActivity.class, bundle);
                break;
            case R.id.rl_about_copyright:
                switch (copyRightTag) {
                    case 0:
                        ivOpenCopyRight.setBackgroundResource(R.drawable.useropen2);
                        tvCopyRight.setVisibility(View.VISIBLE);//显示版权信息
                        if (handler == null)
                            handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                svAbout.fullScroll(View.FOCUS_DOWN);//自动滚动到底部
                            }
                        });
                        copyRightTag = 1;
                        break;
                    case 1:
                        ivOpenCopyRight.setBackgroundResource(R.drawable.useropen);
                        tvCopyRight.setVisibility(View.GONE);
                        copyRightTag = 0;
                        break;
                }
                break;
        }
    }
}
