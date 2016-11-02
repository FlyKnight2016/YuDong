package net.zgyejy.yudong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseAdapter;
import net.zgyejy.yudong.bean.MyImageLoader;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.modle.Video;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by FlyKnight on 2016/10/14.
 */

public class ListViewAdapter_Free extends MyBaseAdapter <Video>{
    public ListViewAdapter_Free(Context context) {
        super(context);
    }

    @Override
    public View getMyView(int i, View view, ViewGroup parent) {
        ViewHolder vh;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_video_free_item, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        Video video = getItem(i);
        vh.tvVideoFreeTitle.setText(video.getVideo_name());
        vh.tvVideoFreeBrowsed.setText("浏览" + video.getSee_num() + "次");
        vh.tvVideoFreeIntro.setText(video.getVideo_describe());
        vh.tvVideoFreeCollect.setText(video.getCollect_num());
        vh.tvVideoFreeComment.setText(video.getEvaluate_num());
        new MyImageLoader(context).display(API.APP_SERVER_IP + video.getVideo_zip(),vh.ivVideoFree);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.iv_video_free)
        ImageView ivVideoFree;
        @BindView(R.id.tv_video_free_title)
        TextView tvVideoFreeTitle;
        @BindView(R.id.tv_video_free_browsed)
        TextView tvVideoFreeBrowsed;
        @BindView(R.id.tv_video_free_intro)
        TextView tvVideoFreeIntro;
        @BindView(R.id.iv_video_free_collect)
        ImageView ivVideoFreeCollect;
        @BindView(R.id.tv_video_free_collect)
        TextView tvVideoFreeCollect;
        @BindView(R.id.iv_video_free_comment)
        ImageView ivVideoFreeComment;
        @BindView(R.id.tv_video_free_comment)
        TextView tvVideoFreeComment;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
