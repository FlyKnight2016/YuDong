package net.zgyejy.yudong.util;

import android.content.Context;
import android.content.SharedPreferences;

import net.zgyejy.yudong.modle.UserBaseInfo;

import java.text.DateFormat;
import java.util.Date;

import static net.zgyejy.yudong.R.drawable.phone;

/**
 * Created by Administrator on 2016/9/5.
 */
public class SharedUtil {
    private static final String SHARED_PATH = "app_share";
    private static final String SHARED_PATH_USER = "user";

    /**
     * app数据缓存
     * @param context
     * @return
     */
    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
    }

    /**
     * 用户数据缓存
     * @param context
     * @return
     */
    public static SharedPreferences getDefaultSharedPreferences_user(Context context) {
        return context.getSharedPreferences(SHARED_PATH_USER, Context.MODE_PRIVATE);
    }

    /**
     * 清空用户信息
     */
    public static void clearAllInfo(Context context) {
        SharedPreferences userSharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor userEditor = userSharedPreferences.edit();
        userEditor.clear();
        userEditor.commit();
    }

    /**
     * 保存用户手机号
     * @param context
     * @param phone
     */
    public static void saveUserPhone(Context context,String phone) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", phone);
        editor.apply();
    }

    /**
     * 获取用户手机号
     * @param context
     * @return
     */
    public static String getUserPhone(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        return sharedPreferences.getString("phone",null);
    }

    /**
     * 保存是否记住密码
     * @param context
     * @param isRememberPsw
     */
    public static void saveIsRememberPsw(Context context,Boolean isRememberPsw) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isRememberPsw",isRememberPsw);
        editor.apply();
    }

    /**
     * 获取是否记住密码
     * @param context
     * @return
     */
    public static Boolean getIsRememberPsw(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        return sharedPreferences.getBoolean("isRememberPsw",false);
    }

    /**
     * 保存用户密码
     * @param context
     * @param phone
     */
    public static void saveUserPsw(Context context,String phone) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("psw", phone);
        editor.apply();
    }

    /**
     * 获取用户密码
     * @param context
     * @return
     */
    public static String getUserPsw(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        return sharedPreferences.getString("psw",null);
    }

    /**
     * 保存是否自动登录
     * @param context
     * @param isRememberPsw
     */
    public static void saveIsAutoLogin(Context context,Boolean isRememberPsw) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isAutoLogin",isRememberPsw);
        editor.apply();
    }

    /**
     * 获取是否自动登录
     * @param context
     * @return
     */
    public static Boolean getIsAutoLogin(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        return sharedPreferences.getBoolean("isAutoLogin",false);
    }

    /**
     * 获得用户是否已登录的信息
     * @param context
     * @return
     */
    public static boolean getIsLogined(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        return sharedPreferences.getBoolean("isLogined",false);
    }

    /**
     * 储存用户是否已登录的信息
     */
    public static void setIsLogined(Context context,boolean isLogined) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogined", isLogined);
        editor.apply();
    }

    /**
     * 存储用户Token
     * @param context
     * @param token
     */
    public static void saveToken(Context context, String token) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    /**
     * 获得存储的Token信息
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        return sharedPreferences.getString("token",null);
    }

    /**
     * 保存用户名、头像、积分等基本信息
     * @param context
     * @param userBaseInfo
     */
    public static void saveUserBaseInfo(Context context, UserBaseInfo userBaseInfo) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", userBaseInfo.getUsername());
        editor.putString("image",userBaseInfo.getImage());
        editor.putString("integral",userBaseInfo.getIntegralcount());
        editor.apply();
    }

    /**
     * 获取传入key对应的用户基本信息
     * @param context
     * @param key
     * @return
     */
    public static String getUserBaseInfo(Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        return sharedPreferences.getString(key,null);
    }

    /**
     * 存储签到时间
     */
    public static void saveSignInDate(Context context,String signInDate) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("signInDate", signInDate);//存储签到时间
        editor.apply();
    }

    /**
     * 获取签到时间
     * @return
     */
    public static String getSignInDate(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        return sharedPreferences.getString("signInDate",null);
    }


    public static void putInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();

    }

    public static int getInt(Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key, 0);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, null);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key, defaultValue);
    }
}
