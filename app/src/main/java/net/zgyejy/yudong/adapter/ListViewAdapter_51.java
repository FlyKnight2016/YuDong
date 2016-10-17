package net.zgyejy.yudong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseAdapter;
import net.zgyejy.yudong.modle.Video51;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by FlyKnight on 2016/10/17.
 */

public class ListViewAdapter_51 extends MyBaseAdapter<Video51> {
    public ListViewAdapter_51(Context context) {
        super(context);
    }

    @Override
    public View getMyView(int i, View view, ViewGroup parent) {
        ViewHolder vh;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_video_51_list_item, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        //vh.tvVideo51Item.setText(getItem(i).getVideoName());

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_video_51_item)
        TextView tvVideo51Item;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
