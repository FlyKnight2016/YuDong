package net.zgyejy.yudong;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import net.zgyejy.yudong.gloable.Contacts;
import net.zgyejy.yudong.live.LiveKit;
import net.zgyejy.yudong.live.fakeserver.FakeServer;
import org.wlf.filedownloader.FileDownloadConfiguration;
import org.wlf.filedownloader.FileDownloader;
import java.io.File;
import cn.jpush.android.api.JPushInterface;
import me.yejy.greendao.DaoMaster;
import me.yejy.greendao.DaoSession;

/**
 * Created by Administrator on 2016/11/10 0010.
 */

public class MyApplication extends Application{
    private static MyApplication mInstance;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        if(mInstance == null)
            mInstance = this;

        //初始化FileDownloader
        initFileDownloader();

        //初始化融云
        LiveKit.init(context, FakeServer.getAppKey());
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        
        //释放FileDownloader
        releaseFileDownloader();
    }

    /**
     * 取得DaoMaster
     *
     * @param context
     * @return
     */
    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, Contacts.DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    /**
     * 释放FileDownloader
     */
    private void releaseFileDownloader() {
        FileDownloader.release();
    }

    /**
     * 初始化FileDownloader
     */
    private void initFileDownloader() {
        //1、创建Builder
        FileDownloadConfiguration.Builder builder = new FileDownloadConfiguration.Builder(this);

        //2、配置Builder
        // 配置下载文件保存的文件夹
        builder.configFileDownloadDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                "FileDownloader");
        // 配置同时下载任务数量，如果不配置默认为2
        builder.configDownloadTaskSize(3);
        // 配置失败时尝试重试的次数，如果不配置默认为0不尝试
        builder.configRetryDownloadTimes(5);
        // 开启调试模式，方便查看日志等调试相关，如果不配置默认不开启
        builder.configDebugMode(true);
        // 配置连接网络超时时间，如果不配置默认为15秒
        builder.configConnectTimeout(25000);// 25秒

        // 3、使用配置文件初始化FileDownloader
        FileDownloadConfiguration configuration = builder.build();
        FileDownloader.init(configuration);
    }
}
