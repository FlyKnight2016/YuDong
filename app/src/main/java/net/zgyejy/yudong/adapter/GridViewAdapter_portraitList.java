package net.zgyejy.yudong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseAdapter;
import net.zgyejy.yudong.modle.Portrait;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.picasso.Picasso;
import me.yejy.greendao.Book;

/**
 * Created by FlyKnight on 2016/10/14.
 */

public class GridViewAdapter_portraitList extends MyBaseAdapter<Portrait> {

    public GridViewAdapter_portraitList(Context context) {
        super(context);
    }

    @Override
    public View getMyView(int i, View view, ViewGroup parent) {
        ViewHolder vh;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_portrait_grid_item, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        vh.ivVideo51Book.setImageResource(getItem(i).getPortraitCode());
        return view;
        //vh.ivVideo51Book.setImageResource();
    }

    class ViewHolder {
        @BindView(R.id.iv_portrait_book)
        ImageView ivVideo51Book;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
