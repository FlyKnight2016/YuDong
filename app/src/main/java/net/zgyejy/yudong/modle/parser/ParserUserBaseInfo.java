package net.zgyejy.yudong.modle.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.zgyejy.yudong.modle.BaseEntity;
import net.zgyejy.yudong.modle.CourseInfo;
import net.zgyejy.yudong.modle.PortraitAndInfos;
import net.zgyejy.yudong.modle.UserBaseInfo;
import net.zgyejy.yudong.modle.UserInfoMore;

import java.lang.reflect.Type;

/**
 * 解析用户基本信息的类
 * Created by Administrator on 2016/11/14 0014.
 */

public class ParserUserBaseInfo {
    /**
     * 得到用户基本信息的方法
     * @param json
     * @return
     */
    public static UserBaseInfo getUserBaseInfo(String json) {
        json = json.replace("\r\n", "");
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<UserBaseInfo>>() {
        }.getType();
        BaseEntity<UserBaseInfo> baseEntity = gson.fromJson(json,type);
        return baseEntity.getData();
    }

    public static PortraitAndInfos getPortraitAndInfos(String json) {
        json = json.replace("\r\n", "");
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<PortraitAndInfos>>() {
        }.getType();
        BaseEntity<PortraitAndInfos> baseEntity = gson.fromJson(json,type);
        return baseEntity.getData();
    }
}
