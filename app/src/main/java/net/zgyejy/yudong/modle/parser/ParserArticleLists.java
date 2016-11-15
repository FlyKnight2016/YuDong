package net.zgyejy.yudong.modle.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.zgyejy.yudong.bean.Article;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 解析文章列表jeson数据的类
 * Created by FlyKnight on 2016/10/24.
 */
public class ParserArticleLists {

    /**
     * 得到文章列表的方法
     * @param json
     * @return
     */
    public static List<Article> getArticleList(String json) {
        json = json.replace("\r\n", "\\r\\n");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Article>>() {
        }.getType();
        List<Article> list = gson.fromJson(json,type);
        return list;
    }
}
