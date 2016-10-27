package net.zgyejy.yudong.activity;

import android.os.Bundle;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.modle.Article;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文章阅读界面
 */
public class ArticleReadActivity extends MyBaseActivity {
    private Article article;

    @BindView(R.id.tv_article_articleTitle)
    TextView tvArticleArticleTitle;
    @BindView(R.id.tv_article_articleContent)
    TextView tvArticleArticleContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_read);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        article = (Article) bundle.getSerializable("article");//得到Article对象
        initData();
    }

    private void initData() {
        tvArticleArticleTitle.setText(article.getTitle());
        tvArticleArticleContent.setText(article.getBody().getBody());
    }

    @OnClick(R.id.iv_article_back)
    public void onClick() {
        openActivity(HomeActivity.class);
    }
}
