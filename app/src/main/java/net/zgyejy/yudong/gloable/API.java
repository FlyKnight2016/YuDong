package net.zgyejy.yudong.gloable;

/**
 * Created by FlyKnight on 2016/10/24.
 */

public class API {
    public static final String ZgyejyNetIP = "http://www.zgyejy.net";//中国幼儿教育网地址

    public static final String ServerIP = "http://118.244.212.82:9092/newsClient/";//服务器地址

    public static final String STUDY_ARTICLE = ZgyejyNetIP +
            "/index.php?c=article&a=connector";//学习园地文章接口

    public static final String STUDY_ARTICLE_PRINCIPAL = STUDY_ARTICLE + "&tid=2";//园长管理
    public static final String STUDY_ARTICLE_PARENTS = STUDY_ARTICLE + "&tid=3";//父母学堂
    public static final String STUDY_ARTICLE_SP = STUDY_ARTICLE + "&tid=22";//家园共育
    public static final String STUDY_ARTICLE_TEACHER = STUDY_ARTICLE + "&tid=1";//幼师之路

}
