package net.zgyejy.yudong.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import net.zgyejy.yudong.modle.UserBaseInfo;
import net.zgyejy.yudong.modle.UserInfoMore;

import java.text.DateFormat;
import java.util.Date;

import io.rong.imlib.model.UserInfo;

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
     * 保存用户头像
     */
    public static void saveUserId(Context context, String userId) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id",userId);
        editor.apply();
    }

    /**
     * 保存用户头像
     */
    public static void saveUserName(Context context, String userName) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name",userName);
        editor.apply();
    }

    /**
     * 保存用户头像
     */
    public static void saveUserIcon(Context context, String portraitUri) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("portraitUri",portraitUri);
        editor.apply();
    }

    /**
     * 获取传入key对应的用户基本信息
     * @param context
     * @return
     */
    public static UserInfo getUserInfo(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        return new UserInfo(sharedPreferences.getString("id","01"),
                sharedPreferences.getString("name","未命名用户"),
                Uri.parse(sharedPreferences.getString("portraitUri","http://api.zgyejy.net/image/pic.jpg")));
        //此处头像用户名为临时，接口改变后需修改
    }

    /**
     * 得到用户名
     * @param context
     * @return
     */
    public static String getUserName(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        return sharedPreferences.getString("name","未命名用户");
    }

    /**
     * 得到用户名
     * @param context
     * @return
     */
    public static String getUserIcon(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        return sharedPreferences.getString("portraitUri","http://api.zgyejy.net/image/pic.jpg");
    }

    /**
     * 存储其他用户信息
     * @param context
     * @param userInfoMore
     */
    public static void saveUserInfoMore(Context context, UserInfoMore userInfoMore) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nickname",userInfoMore.getNickname());
        editor.putString("gender",userInfoMore.getGender());
        editor.putString("identity",userInfoMore.getIdentity());
        editor.putString("qq",userInfoMore.getQq());
        editor.putString("weichat",userInfoMore.getWeichat());
        editor.putString("kindergarten",userInfoMore.getKindergarten());
        editor.putString("email",userInfoMore.getEmail());
        editor.putString("signature",userInfoMore.getSignature());
        editor.putString("image",userInfoMore.getImage());
        editor.putInt("iid",userInfoMore.getIid());
        editor.apply();
    }

    public static UserInfoMore getUserInfoMore(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        return new UserInfoMore(
                sharedPreferences.getString("nickname","未知用户"),
                sharedPreferences.getString("gender","1"),
                sharedPreferences.getString("identity","1"),
                sharedPreferences.getString("qq",""),
                sharedPreferences.getString("weichat",""),
                sharedPreferences.getString("kindergarten",""),
                sharedPreferences.getString("email",""),
                sharedPreferences.getString("signature",""),
                sharedPreferences.getString("image",""),
                sharedPreferences.getInt("iid",1)
        );
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
