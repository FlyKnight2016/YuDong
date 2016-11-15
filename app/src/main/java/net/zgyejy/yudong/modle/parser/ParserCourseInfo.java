package net.zgyejy.yudong.modle.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.zgyejy.yudong.bean.Course;
import net.zgyejy.yudong.modle.BaseEntity;
import net.zgyejy.yudong.modle.CourseInfo;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 解析到详细的课程信息（mp3、文档等）
 * Created by Administrator on 2016/11/14 0014.
 */

public class ParserCourseInfo {
    /**
     * 得到文章列表的方法
     * @param json
     * @return
     */
    public static CourseInfo getCourseInfo(String json) {
        json = json.replace("\r\n", "");
        Gson gson = new Gson();
        Type type = new TypeToken<BaseEntity<CourseInfo>>() {
        }.getType();
        BaseEntity<CourseInfo> baseEntity = gson.fromJson(json,type);
        return baseEntity.getData();
    }
}
