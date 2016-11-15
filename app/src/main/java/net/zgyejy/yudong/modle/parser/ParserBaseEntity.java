package net.zgyejy.yudong.modle.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.zgyejy.yudong.modle.BaseEntity;
import net.zgyejy.yudong.modle.CourseInfo;
import net.zgyejy.yudong.modle.Token;
import net.zgyejy.yudong.modle.Version;

import java.lang.reflect.Type;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * 解析基本jeson
 * Created by Administrator on 2016/11/11 0011.
 */

public class ParserBaseEntity{
    /**
     * 得到BaseEntity对象
     * @param json
     * @return
     */
    public static BaseEntity<List> getBaseEntityList(String json) {
        json = json.replace("\r\n", "");
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<List>>() {
        }.getType();
        BaseEntity<List> baseEntity = gson.fromJson(json,type);
        return baseEntity;
    }

    public static BaseEntity<Token> getBaseEntityOfToken(String json) {
        json = json.replace("\r\n", "");
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<Token>>() {
        }.getType();
        BaseEntity<Token> baseEntity = gson.fromJson(json,type);
        return baseEntity;
    }

    public static BaseEntity<Version> getBaseEntityOfVersion(String json) {
        json = json.replace("\r\n", "");
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<Version>>() {
        }.getType();
        BaseEntity<Version> baseEntity = gson.fromJson(json,type);
        return baseEntity;
    }
}
