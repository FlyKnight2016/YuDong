package net.zgyejy.yudong.gloable;


/**
 * Created by FlyKnight on 2016/10/24.
 */

public class API {
    public static final String ZgyejyNetIP = "http://www.zgyejy.net";//中国幼儿教育网地址
    public static final String APP_SERVER_IP = "http://api.zgyejy.net";//app服务器地址


    public static final String STUDY_ARTICLE = ZgyejyNetIP +
            "/index.php?c=article&a=connector";//学习园地文章接口

    public static final String STUDY_ARTICLE_PRINCIPAL = STUDY_ARTICLE + "&tid=2";//园长管理
    public static final String STUDY_ARTICLE_PARENTS = STUDY_ARTICLE + "&tid=3";//父母学堂
    public static final String STUDY_ARTICLE_SP = STUDY_ARTICLE + "&tid=22";//家园共育
    public static final String STUDY_ARTICLE_TEACHER = STUDY_ARTICLE + "&tid=1";//幼师之路

    /**
     * 七巧板课程列表
     */
    public static final String VIDEO_51_BOOK_LIST = APP_SERVER_IP +
            "/index.php/admin/admin/videoCates?id=";//七巧板51课程列表/课程视频列表接口（参数为册目录id）

    /**
     * 注册、改密、登录
     */
    public static final String REGISTER_GET_PHONE_CODE = APP_SERVER_IP+
            "/index.php/home/home/phoneCode?phone=";//注册获取手机验证码接口（参数为手机号码）
    public static final String REGISTER_POST_REGISTER = APP_SERVER_IP +
            "/index.php/home/home/register";//用户注册的post接口（添加参数为：phone password phoneCode）
    public static final String REGISTER_POST_EDITPASSWORD = APP_SERVER_IP +
            "/index.php/home/home/EditPassword";//用户修改密码的post接口（添加参数为：phone password phoneCode）

    public static final String LOGIN_POST_LOGIN = APP_SERVER_IP +
            "/index.php/home/home/login";//用户登录的post接口（添加参数为：phone password）

    /**
     * 视频
     */
    public static final String VIDEO_GET_VIDEOINFO = APP_SERVER_IP +
            "/index.php/home/home/videoinfo?";//获取视频信息的接口（添加参数为：token id）
    public static final String VIDEO_GET_VIDEOLIST = APP_SERVER_IP +
            "/index.php/home/home/videoList?";//获取视频列表的接口（page=2&pageSize=6&videoStyle=1）
        // videoStyle=1：免费视频（基本不用）2:付费视频 3：积分视频 4：收藏的视频（不需要）

    //视频收藏接口，参数：id=38&token=jDeVZKPSx$2JHoEnwMBR
    public static final String VIDEO_GET_VIDEOCOLLECT = APP_SERVER_IP +
            "/index.php/home/home/videoCollect?";

    //评论接口，参数：id=38&token=jDeVZKPSx$2JHoEnwMBR
    public static final String VIDEO_GET_COMMENT = APP_SERVER_IP +
            "/index.php/home/home/comment?";

    //查看评论，参数：id=60
    public static final String VIDEO_GET_COMMENTLIST = APP_SERVER_IP +
            "/index.php/home/home/comment?";

    /**
     * 个人中心
     */

    //个人基本信息，参数：token=jDeVZKPSx$2JHoEnwMBR
    public static final String USER_GET_USERINFO = APP_SERVER_IP +
            "/index.php/home/home/userInfo?";

    //积分增加接口，参数：flag=1&token=jDeVZKPSx$2JHoEnwMBR  flag = 1 登录、flag=2 分享
    public static final String USER_GET_INTEGRAL = APP_SERVER_IP +
            "/index.php/home/home/integral?flag=1&token=jDeVZKPSx$2JHoEnwMBR";

    /**
     * APP相关
     */

    //活动推送接口，参数：无
    public static final String APP_GET_ACTIVES = APP_SERVER_IP +
            "/index.php/home/home/actives";

    //版本更新，参数：
            //客户端类型app=android
            //版本号versionCode=1.0
    public static final String APP_POST_UPDATE = APP_SERVER_IP +
            "/index.php/home/home/version";
}
