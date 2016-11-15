package net.zgyejy.yudong.modle.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.zgyejy.yudong.bean.VideoIntegral;
import net.zgyejy.yudong.modle.BaseEntity;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 解析视频列表jeson数据的类
 * Created by FlyKnight on 2016/11/1.
 */

public class ParserVideoList {
    /**
     * 得到视频列表的方法
     * @param json
     * @return
     */
    public static List<VideoIntegral> getVideoList(String json) {
        json = json.replace("\r\n", "");
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<List<VideoIntegral>>>() {
        }.getType();
        BaseEntity<List<VideoIntegral>> baseEntity = gson.fromJson(json,type);
        List<VideoIntegral> list = baseEntity.getData();
        return list;
    }
}
