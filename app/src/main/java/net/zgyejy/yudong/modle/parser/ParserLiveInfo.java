package net.zgyejy.yudong.modle.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.zgyejy.yudong.modle.BaseEntity;
import net.zgyejy.yudong.modle.CourseInfo;
import net.zgyejy.yudong.modle.LiveInfo;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public class ParserLiveInfo {
    /**
     * 获取正在直播的信息
     * @param json
     * @return
     */
    public static BaseEntity<LiveInfo> getBaseEntity(String json) {
        json = json.replace("\r\n", "");
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<LiveInfo>>() {
        }.getType();
        return gson.fromJson(json,type);
    }
}
