package net.zgyejy.yudong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseAdapter;
import net.zgyejy.yudong.modle.Course;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/4 0004.
 */

public class ListCourseAdapter extends MyBaseAdapter<Course> {
    public ListCourseAdapter(Context context) {
        super(context);
    }

    @Override
    public View getMyView(int i, View view, ViewGroup parent) {
        ViewHolder vh;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_study_articlel_title, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        vh.tvArticleTitle.setText(getItem(i).getName());

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_article_title)
        TextView tvArticleTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
