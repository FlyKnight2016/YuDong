package net.zgyejy.yudong.util;

/**
 * Describe:
 * <p>
 * <p>
 * 常量工具类
 * <p>
 * 主要用于为其它类提供程序所用到的常量
 *
 * @author jph
 *         <p>
 *         Date:2014.08.07
 */
public class ConstUtil {
    //MusicBox接收器所能响应的Action
    public static final String MUSICBOX_ACTION = "net.zgyejy.yudong.MUSICBOX_ACTION";
    //MusicService接收器所能响应的Action
    public static final String MUSICSERVICE_ACTION = "net.zgyejy.yudong.MUSICSERVICE_ACTION";
    //初始化flag
    public static final int STATE_NON = 0x122;
    //播放的flag
    public static final int STATE_PLAY = 0x123;
    //暂停的flag
    public static final int STATE_PAUSE = 0x124;
    //停止放的flag
    public static final int STATE_STOP = 0x125;
    //播放上一首的flag
    public static final int STATE_PREVIOUS = 0x126;
    //播放下一首的flag
    public static final int STATE_NEXT = 0x127;
    //菜单关于选项的itemId
    public static final int MENU_ABOUT = 0x200;
    //菜单退出选的项的itemId
    public static final int MENU_EXIT = 0x201;

    //服务准备就绪
    public static final int SERVICE_READY = 0x300;
    //播放完毕
    public static final int SERVICE_OVER = 0x301;
    //更新播放进度
    public static final int SERVICE_UPDATE = 0x302;

    public ConstUtil() {
        // TODO Auto-generated constructor stub
    }
}
