package net.zgyejy.yudong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseAdapter;
import net.zgyejy.yudong.gloable.API;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.picasso.Picasso;
import me.yejy.greendao.VideoIntegral;

/**
 * Created by FlyKnight on 2016/10/14.
 */

public class ListViewAdapter_Integral extends MyBaseAdapter<VideoIntegral> {
    public ListViewAdapter_Integral(Context context) {
        super(context);
    }

    @Override
    public View getMyView(int i, View view, ViewGroup parent) {
        ViewHolder vh;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_video_integral_item, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        VideoIntegral videoIntegral = getItem(i);
        vh.tvVideoIntegralTitle.setText(videoIntegral.getVideo_name());
        vh.tvVideoIntegralBrowsed.setText("浏览" + videoIntegral.getSee_num() + "次");
        vh.tvVideoIntegralIntro.setText(videoIntegral.getVideo_describe());
        vh.tvVideoIntegralCollect.setText(videoIntegral.getCollect_num());
        vh.tvVideoIntegralComment.setText(videoIntegral.getEvaluate_num());
        vh.tvVideoIntegralIntegral.setText(videoIntegral.getVideo_jifen());
        //使用Picasso第三方库加载图片
        Picasso.with(context)
                .load(API.APP_SERVER_IP + videoIntegral.getVideo_zip())//加载地址
                .placeholder(R.drawable.loadingphoto)//占位图（加载中）
                .error(R.drawable.nophoto)//加载失败
                .into(vh.ivVideoIntegral);//加载到的ImageView
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.iv_video_integral)
        ImageView ivVideoIntegral;
        @BindView(R.id.tv_video_integral_title)
        TextView tvVideoIntegralTitle;
        @BindView(R.id.tv_video_integral_browsed)
        TextView tvVideoIntegralBrowsed;
        @BindView(R.id.tv_video_integral_intro)
        TextView tvVideoIntegralIntro;
        @BindView(R.id.iv_video_integral_collect)
        ImageView ivVideoIntegralCollect;
        @BindView(R.id.tv_video_integral_collect)
        TextView tvVideoIntegralCollect;
        @BindView(R.id.tv_video_integral_comment)
        TextView tvVideoIntegralComment;
        @BindView(R.id.iv_video_integral_integral)
        ImageView ivVideoIntegralIntegral;
        @BindView(R.id.tv_video_integral_integral)
        TextView tvVideoIntegralIntegral;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
