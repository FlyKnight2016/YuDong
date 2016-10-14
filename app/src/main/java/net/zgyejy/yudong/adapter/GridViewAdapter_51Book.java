package net.zgyejy.yudong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseAdapter;
import net.zgyejy.yudong.modle.Book;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by FlyKnight on 2016/10/14.
 */

public class GridViewAdapter_51Book extends MyBaseAdapter<Book> {

    public GridViewAdapter_51Book(Context context) {
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

        vh.tvVideo51Book.setText(getItem(i).getBookName());
        //vh.ivVideo51Book.setImageResource();

        return view;
    }

    class ViewHolder {
        @BindView(R.id.iv_video_51_book)
        ImageView ivVideo51Book;
        @BindView(R.id.tv_video_51_book)
        TextView tvVideo51Book;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
