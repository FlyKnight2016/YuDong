package net.zgyejy.yudong.modle.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.zgyejy.yudong.modle.BaseEntity;
import net.zgyejy.yudong.bean.Course;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 得到课程列表的方法
 * Created by Administrator on 2016/11/4 0004.
 */

public class ParserCourseList {
    /**
     * 得到文章列表的方法
     * @param json
     * @return
     */
    public static List<Course> getCourseList(String json) {
        json = json.replace("\r\n", "");
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<List<Course>>>() {
        }.getType();
        BaseEntity<List<Course>> baseEntity = gson.fromJson(json,type);
        List<Course> list = baseEntity.getData();
        return list;
    }
}
