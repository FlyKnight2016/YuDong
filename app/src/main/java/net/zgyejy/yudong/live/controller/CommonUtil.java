package net.zgyejy.yudong.live.controller;


import net.zgyejy.yudong.MyApplication;

public class CommonUtil {

    public static int dip2px(float dpValue) {
        float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
