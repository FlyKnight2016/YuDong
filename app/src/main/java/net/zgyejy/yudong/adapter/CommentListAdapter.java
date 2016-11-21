package net.zgyejy.yudong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseAdapter;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.modle.Comment;
import net.zgyejy.yudong.util.CommonUtil;
import net.zgyejy.yudong.view.XCRoundRectImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.picasso.Picasso;

import static net.zgyejy.yudong.R.drawable.phone;

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
        Comment comment = getItem(myList.size()-i-1);//评论反序
        if (comment.getUserImage()!=null) {
            //使用Picasso第三方库加载图片
            Picasso.with(context)
                    .load(API.APP_SERVER_IP + comment.getUserImage())//加载地址
                    .placeholder(R.drawable.loadingphoto)//占位图（加载中）
                    .error(R.drawable.nophoto)//加载失败
                    .into(vh.ivCommentListPortrait);//加载到的ImageView
        }else {
            vh.ivCommentListPortrait.setImageResource(R.drawable.user_selected);
        }
        String name = comment.getPhone();
        vh.tvCommentListUserName.setText("用户" + name.substring(0, 3) + "****" + name.substring(7));
        vh.tvCommentListDate.setText(CommonUtil.getStrTime(Long.parseLong(comment.getAddtime())));//转化评论时间
        vh.tvCommentListContent.setText("    " + comment.getContent());

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
