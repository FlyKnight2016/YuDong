package net.zgyejy.yudong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseAdapter;
import net.zgyejy.yudong.modle.Comment;
import net.zgyejy.yudong.view.XCRoundRectImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by FlyKnight on 2016/10/21.
 */

public class CommentListAdapter extends MyBaseAdapter<Comment> {
    public CommentListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getMyView(int i, View view, ViewGroup parent) {
        ViewHolder vh;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_list_comment_item, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        }else {
            vh = (ViewHolder) view.getTag();
        }

        //适配数据...

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.iv_commentList_portrait)
        XCRoundRectImageView ivCommentListPortrait;
        @BindView(R.id.tv_commentList_userName)
        TextView tvCommentListUserName;
        @BindView(R.id.tv_commentList_date)
        TextView tvCommentListDate;
        @BindView(R.id.tv_commentList_content)
        TextView tvCommentListContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
