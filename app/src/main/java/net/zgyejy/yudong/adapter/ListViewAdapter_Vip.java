package net.zgyejy.yudong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseAdapter;
import net.zgyejy.yudong.modle.VideoVip;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by FlyKnight on 2016/10/14.
 */

public class ListViewAdapter_Vip extends MyBaseAdapter <VideoVip>{
    public ListViewAdapter_Vip(Context context) {
        super(context);
    }

    @Override
    public View getMyView(int i, View view, ViewGroup parent) {
        ViewHolder vh;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_video_vip_item, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        //vh.tvVideo51Book.setText(getItem(i).getBookName());
        //vh.ivVideo51Book.setImageResource();

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.iv_video_vip)
        ImageView ivVideoVip;
        @BindView(R.id.tv_video_vip_title)
        TextView tvVideoVipTitle;
        @BindView(R.id.tv_video_vip_browsed)
        TextView tvVideoVipBrowsed;
        @BindView(R.id.tv_video_vip_intro)
        TextView tvVideoVipIntro;
        @BindView(R.id.iv_video_vip_collect)
        ImageView ivVideoVipCollect;
        @BindView(R.id.tv_video_vip_collect)
        TextView tvVideoVipCollect;
        @BindView(R.id.iv_video_vip_comment)
        ImageView ivVideoVipComment;
        @BindView(R.id.tv_video_vip_comment)
        TextView tvVideoVipComment;
        @BindView(R.id.tv_video_vip_price)
        TextView tvVideoVipPrice;
        @BindView(R.id.iv_video_vip_price)
        ImageView ivVideoVipPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
