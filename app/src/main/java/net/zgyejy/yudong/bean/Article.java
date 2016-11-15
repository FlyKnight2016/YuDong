package net.zgyejy.yudong.bean;

import java.io.Serializable;

/**
 * 文章对象
 * Created by FlyKnight on 2016/10/21.
 */

public class Article implements Serializable{
    private String id;
    private String title;
    private String url;
    private ArticleBody body;

    public Article(String id, String title, String url, ArticleBody body) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArticleBody getBody() {
        return body;
    }

    public void setBody(ArticleBody body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", body=" + body +
                '}';
    }
}
