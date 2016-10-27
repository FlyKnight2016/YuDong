package net.zgyejy.yudong.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/9/5.
 */
public class SharedUtil {
    private static final String SHARED_PATH = "app_share";
    private static final String SHARED_PATH_REGISTER = "register";
    private static final String SHARED_PATH_USER = "user";

    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getDefaultSharedPreferences_register(Context context) {
        return context.getSharedPreferences(SHARED_PATH_REGISTER, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getDefaultSharedPreferences_user(Context context) {
        return context.getSharedPreferences(SHARED_PATH_USER, Context.MODE_PRIVATE);
    }

    /**
     * 清空用户注册和登录信息
     */
    public static void clearAllInfos(Context context) {
        SharedPreferences userSharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor userEditor = userSharedPreferences.edit();
        userEditor.clear();
        userEditor.commit();

        SharedPreferences registerSharedPreferences = getDefaultSharedPreferences_register(context);
        SharedPreferences.Editor registerEditor = registerSharedPreferences.edit();
        registerEditor.clear();
        registerEditor.commit();
    }

    /**
     * 保存用户的基本信息
     */
    /*public static void saveUserInfo(Context context,User user) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", user.getUid());
        editor.putString("portrait",user.getPortrait());
        editor.commit();
    }*/

    public static void saveUserName(Context context,String userName) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uid", userName);
        editor.commit();
    }

    public static void saveUserIcon(Context context,String userIcon) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("portrait",userIcon);
        editor.commit();
    }

    /**
     * 保存是否第三方登录的信息
     * @param context
     * @param isThirdPartyLogin
     */
    public static void saveIsThirdPartyLogin(Context context,Boolean isThirdPartyLogin) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isThirdPartyLogin",isThirdPartyLogin);
        editor.commit();
    }

    /**；；；；
     * 得到是否第三方登录的信息
     * @param context
     * @return
     */
    public static Boolean getIsThirdPartyLogin(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        return sharedPreferences.getBoolean("isThirdPartyLogin",false);
    }

    /**
     * 得到已登录的用户名
     * @param context
     * @return
     */
    public static String getUserUid(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        return sharedPreferences.getString("uid",null);
    }

    /**
     * 得到已登录的用户头像地址
     * @param context
     * @return
     */
    public static String getUserPortrait(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_user(context);
        return sharedPreferences.getString("portrait",null);
    }

    /**
     * 保存用户的注册信息
     *
     * @param baseRegister
     * @param context
     */
    /*public static void saveRegisterInfo(BaseEntity<Register> baseRegister, Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_register(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogined", true);
        Register register = baseRegister.getData();
        editor.putInt("result", register.getResult());
        editor.putString("explain", register.getExplain());
        editor.putString("token", register.getToken());
        editor.commit();
    }*/

    /**
     * 获得用户是否已登录的信息
     * @param context
     * @return
     */
    public static boolean getIsLogined(Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_register(context);
        return sharedPreferences.getBoolean("isLogined",false);
    }

    /**
     * 储存用户是否已登录的信息
     */
    public static void setIsLogined(Context context,boolean isLogined) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_register(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogined", isLogined);
        editor.commit();
    }

    /**
     * 获得用户注册后的Tokey信息
     * @param context
     * @param key
     * @return
     */
    public static String getTokey(Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences_register(context);
        return sharedPreferences.getString(key,null);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();

    }

    public static int getInt(Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key, 0);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, null);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key, defaultValue);
    }
}
