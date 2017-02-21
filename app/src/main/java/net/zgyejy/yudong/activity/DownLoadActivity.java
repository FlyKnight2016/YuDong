package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownLoadActivity extends MyBaseActivity {

    @BindView(R.id.tv_download_video)
    TextView tvDownloadVideo;
    @BindView(R.id.tv_download_mp3)
    TextView tvDownloadMp3;
    @BindView(R.id.tv_download_image)
    TextView tvDownloadImage;
    @BindView(R.id.tv_download_txt)
    TextView tvDownloadTxt;
    @BindView(R.id.vp_download)
    ViewPager vpDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_download_return, R.id.tv_download_video, R.id.tv_download_mp3, R.id.tv_download_image, R.id.tv_download_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_download_return:
                break;
            case R.id.tv_download_video:
                break;
            case R.id.tv_download_mp3:
                break;
            case R.id.tv_download_image:
                break;
            case R.id.tv_download_txt:
                break;
        }
    }
}
