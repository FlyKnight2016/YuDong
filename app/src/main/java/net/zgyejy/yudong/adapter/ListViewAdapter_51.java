package net.zgyejy.yudong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseAdapter;
import me.yejy.greendao.VideoIntegral;
import net.zgyejy.yudong.gloable.API;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by FlyKnight on 2016/10/14.
 */

public class ListViewAdapter_51 extends MyBaseAdapter <VideoIntegral>{
    public ListViewAdapter_51(Context context) {
        super(context);
    }

    @Override
    public View getMyView(int i, View view, ViewGroup parent) {
        ViewHolder vh;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_video_51_item, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        VideoIntegral videoIntegral = getItem(i);
        vh.tvVideo51Title.setText(videoIntegral.getVideo_name());
        vh.tvVideo51Browsed.setText("浏览" + videoIntegral.getSee_num() + "次");
        vh.tvVideo51Intro.setText(videoIntegral.getVideo_describe());
        vh.tvVideo51Collect.setText(videoIntegral.getCollect_num());
        vh.tvVideo51Comment.setText(videoIntegral.getEvaluate_num());
        //使用Picasso第三方库加载图片
        Picasso.with(context)
                .load(API.APP_SERVER_IP + videoIntegral.getVideo_zip())//加载地址
                .placeholder(R.drawable.loadingphoto)//占位图（加载中）
                .error(R.drawable.nophoto)//加载失败
                .into(vh.ivVideo51);//加载到的ImageView
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.iv_video_51)
        ImageView ivVideo51;
        @BindView(R.id.tv_video_51_title)
        TextView tvVideo51Title;
        @BindView(R.id.tv_video_51_browsed)
        TextView tvVideo51Browsed;
        @BindView(R.id.tv_video_51_intro)
        TextView tvVideo51Intro;
        @BindView(R.id.iv_video_51_collect)
        ImageView ivVideo51Collect;
        @BindView(R.id.tv_video_51_collect)
        TextView tvVideo51Collect;
        @BindView(R.id.iv_video_51_comment)
        ImageView ivVideo51Comment;
        @BindView(R.id.tv_video_51_comment)
        TextView tvVideo51Comment;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
