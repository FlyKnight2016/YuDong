package net.zgyejy.yudong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import net.zgyejy.yudong.R;
import net.zgyejy.yudong.activity.HomeActivity;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoFragment extends Fragment {
    @BindColor(R.color.white) int whiteColor;
    private int themeColor;

    @BindView(R.id.tv_video_51)
    TextView tvVideo51;
    @BindView(R.id.tv_video_free)
    TextView tvVideoFree;
    @BindView(R.id.tv_video_vip)
    TextView tvVideoVip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);
        showVideo51();
        return view;
    }

    @OnClick({R.id.iv_scan, R.id.tv_video_51, R.id.tv_video_free,
            R.id.tv_video_vip, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_scan:
                break;
            case R.id.tv_video_51:
                showVideo51();
                break;
            case R.id.tv_video_free:
                showVideoFree();
                break;
            case R.id.tv_video_vip:
                showVideoVip();
                break;
            case R.id.iv_search:
                break;
        }
    }

    private void showVideoVip() {
        setDeaultBack();
        tvVideoVip.setBackgroundResource(R.drawable.text_view_back_vip);
        tvVideoVip.setTextColor(whiteColor);
    }

    private void showVideoFree() {
        setDeaultBack();
        tvVideoFree.setBackgroundResource(R.drawable.text_view_back_free);
        tvVideoFree.setTextColor(whiteColor);
    }

    private void showVideo51() {
        setDeaultBack();
        tvVideo51.setBackgroundResource(R.drawable.text_view_back_51);
        tvVideo51.setTextColor(whiteColor);
    }

    private void setDeaultBack() {
        themeColor = ((HomeActivity)getActivity()).getThemeColor();

        tvVideo51.setBackgroundResource(R.drawable.text_view_border_51);
        tvVideoFree.setBackgroundResource(R.drawable.text_view_border_free);
        tvVideoVip.setBackgroundResource(R.drawable.text_view_border_vip);

        tvVideo51.setTextColor(themeColor);
        tvVideoFree.setTextColor(themeColor);
        tvVideoVip.setTextColor(themeColor);
    }
}
