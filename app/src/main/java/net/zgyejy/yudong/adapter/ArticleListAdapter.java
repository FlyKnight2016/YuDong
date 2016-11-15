package net.zgyejy.yudong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseAdapter;
import net.zgyejy.yudong.bean.Article;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by FlyKnight on 2016/10/21.
 */

public class ArticleListAdapter extends MyBaseAdapter<Article> {
    public ArticleListAdapter(Context context) {
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

        //适配数据...
        vh.tvArticleTitle.setText((i+1)+"、"+getItem(i).getTitle());

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
