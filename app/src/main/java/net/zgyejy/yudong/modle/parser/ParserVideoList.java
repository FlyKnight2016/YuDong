package net.zgyejy.yudong.modle.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.zgyejy.yudong.modle.BaseEntity;
import net.zgyejy.yudong.modle.Video;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 解析视频列表jeson数据的类
 * Created by FlyKnight on 2016/11/1.
 */

public class ParserVideoList {
    /**
     * 得到文章列表的方法
     * @param json
     * @return
     */
    public static List<Video> getVideoList(String json) {
        json = json.replace("\r\n", "");
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<List<Video>>>() {
        }.getType();
        BaseEntity<List<Video>> baseEntity = gson.fromJson(json,type);
        List<Video> list = baseEntity.getData();
        return list;
    }
}
