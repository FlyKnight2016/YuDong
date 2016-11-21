package net.zgyejy.yudong.modle.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.zgyejy.yudong.modle.BaseEntity;
import net.zgyejy.yudong.modle.Comment;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 解析评论的抽象类
 * Created by Administrator on 2016/11/16 0016.
 */

public class ParserComment {

    /**
     * 得到评论列表的方法
     * @param json
     * @return
     */
    public static BaseEntity<List<Comment>> getBaseCommentList(String json) {
        json = json.replace("\r\n", "");
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<List<Comment>>>() {
        }.getType();
        BaseEntity<List<Comment>> baseEntity = gson.fromJson(json,type);
        return baseEntity;
    }
}
