package net.zgyejy.yudong.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getSystime() {
        String systime;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        systime = dateFormat.format(new Date(System.currentTimeMillis()));
        return systime;
    }

    public static String getStrTime(long filetime) {
        if (filetime == 0) {
            return "未知";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String ftime = formatter.format(new Date(filetime));
        return ftime;
    }

    private static DecimalFormat df = new DecimalFormat("#.00");// 数据类型格式为括号内样式

    public static String getFileInfo(long file) {
        StringBuffer strBuff = new StringBuffer();
        if (file < 1024) {
            strBuff.append(file);
            strBuff.append("B");
        } else if (file < (1024l * 1024)) {
            strBuff.append(df.format((((double) file) / 1024)));
            strBuff.append("K");
        } else if (file < (1024l * 1024 * 1024)) {
            strBuff.append(df.format(((double) file) / (1024 * 1024)));
            strBuff.append("M");
        } else if (file < (1024l * 1024 * 1024 * 1024)) {
            strBuff.append(df.format(((double) file) / (1024 * 1024 * 1024)));
            strBuff.append("G");
        } else if (file < (1024l * 1024 * 1024 * 1024 * 1024)) {
            strBuff.append(df.format(((double) file)
                    / (1024 * 1024 * 1024 * 1024)));
            strBuff.append("T");
        }
        return strBuff.toString();
    }

    /**
     * 获取当前版本号
     *
     * @param context
     * @return
     */
    public static int getVersonCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 验证密码格式
     *
     * @param password
     * @return
     */
    public static boolean verifyPassword(String password) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{6,16}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * 验证邮箱格式
     *
     * @param email
     * @return
     */
    public static boolean verifyEmail(String email) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)"
                + "|(([a-zA-Z0-9\\-]+\\.)+))"
                + "([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 检查当前网络是否可用
     *
     * @param activity
     * @return
     */
    public static boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        //获取手机所有连接管理对象（包括对wi-fi，net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            //获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    //判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取手机的IMEI值
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService
                (Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}