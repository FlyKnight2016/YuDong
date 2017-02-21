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

public class HListViewAdapter_Integral extends MyBaseAdapter<VideoIntegral> {

    public HListViewAdapter_Integral(Context context) {
        super(context);
    }

    @Override
    public View getMyView(int i, View view, ViewGroup parent) {
        ViewHolder vh;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_video_51_trial_grid_item, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        vh.tvVideo51Book.setText(getItem(i).getVideo_name());
        Picasso.with(context)
                .load(API.APP_SERVER_IP + getItem(i).getVideo_zip())//加载地址
                .placeholder(R.drawable.loadingphoto)//占位图（加载中）
                .error(R.drawable.nophoto)//加载失败
                .into(vh.ivVideo51Book);//加载到的ImageView
        return view;
    }

    class ViewHolder {
        @BindView(R.id.iv_video_51_trial_book)
        ImageView ivVideo51Book;
        @BindView(R.id.tv_video_51_trial_book)
        TextView tvVideo51Book;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
