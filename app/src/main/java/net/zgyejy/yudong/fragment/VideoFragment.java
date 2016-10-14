package net.zgyejy.yudong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.activity.HomeActivity;
import net.zgyejy.yudong.adapter.GridViewAdapter_51Book;
import net.zgyejy.yudong.modle.Book;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoFragment extends Fragment {
    @BindView(R.id.tv_video_51)
    TextView tvVideo51;
    @BindView(R.id.tv_video_free)
    TextView tvVideoFree;
    @BindView(R.id.tv_video_vip)
    TextView tvVideoVip;

    @BindView(R.id.gv_video_51)
    GridView gvVideo51;//五个一教材列表
    private GridViewAdapter_51Book adapter51Book;//五个一教材列表适配器

    @BindColor(R.color.white)
    int whiteColor;//白色
    private int themeColor;//主题颜色

    private List<Book> listBook;//五个一教材列表

    private GestureDetector gesture;//手势识别
    private int tag;//当前页面标识

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);
        initVideo51();

        //根据父窗体getActivity()为fragment设置手势识别
        gesture = new GestureDetector(this.getActivity(), new MyOnGestureListener());
        //为fragment添加OnTouchListener监听器
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);//返回手势识别触发的事件
            }
        });

        return view;
    }

    //设置手势识别监听器
    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override//此方法必须重写且返回真，否则onFling不起效
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if ((e1.getX() - e2.getX() > 50) && Math.abs(velocityX) > 50) {
                //左划
                switch (tag) {
                    case 1:
                        showVideo51();
                        break;
                    case 2:
                        showVideoFree();
                        break;
                }
                return true;
            } else if ((e2.getX() - e1.getX() > 120) && Math.abs(velocityX) > 200) {
                //右划
                switch (tag) {
                    case 0:
                        showVideoFree();
                        break;
                    case 1:
                        showVideoVip();
                        break;
                }
                return true;
            }
            return false;
        }
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
        tag = 2;
        gvVideo51.setVisibility(View.GONE);
    }

    private void showVideoFree() {
        setDeaultBack();
        tvVideoFree.setBackgroundResource(R.drawable.text_view_back_free);
        tvVideoFree.setTextColor(whiteColor);
        tag = 1;
        gvVideo51.setVisibility(View.GONE);
    }

    //初始化五个一视频界面
    private void initVideo51() {
        tvVideo51.setBackgroundResource(R.drawable.text_view_back_51);
        tvVideo51.setTextColor(whiteColor);
        tag = 0;
        if (adapter51Book == null)
            adapter51Book = new GridViewAdapter_51Book(getContext());
        gvVideo51.setAdapter(adapter51Book);
        initDateVideo51();
    }

    private void showVideo51() {
        setDeaultBack();
        tvVideo51.setBackgroundResource(R.drawable.text_view_back_51);
        tvVideo51.setTextColor(whiteColor);
        tag = 0;
        gvVideo51.setVisibility(View.VISIBLE);
        initDateVideo51();
    }

    private void initDateVideo51() {
        String[] str = {"一", "二", "三", "四", "五", "六"};
        if (listBook == null) {
            listBook = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                listBook.add(new Book("七巧板智力阅读\n第" + str[i] + "册"));
            }
        }
        adapter51Book.appendDataed(listBook, true);
        adapter51Book.updateAdapter();
    }

    private void setDeaultBack() {
        themeColor = ((HomeActivity) getActivity()).getThemeColor();

        tvVideo51.setBackgroundResource(R.drawable.text_view_border_51);
        tvVideoFree.setBackgroundResource(R.drawable.text_view_border_free);
        tvVideoVip.setBackgroundResource(R.drawable.text_view_border_vip);

        tvVideo51.setTextColor(themeColor);
        tvVideoFree.setTextColor(themeColor);
        tvVideoVip.setTextColor(themeColor);
    }
}
