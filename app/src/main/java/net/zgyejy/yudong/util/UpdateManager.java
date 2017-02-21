package net.zgyejy.yudong.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import net.zgyejy.yudong.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 版本更新管理类
 * Created by Administrator on 2016/9/14.
 */
public class UpdateManager {
    private Context context;

    private String updateMsg = "发现新版本，请下载更新！";//提示语

    private String apkUrl;//返回的安装包url

    private Dialog noticeDialog, downloadDialog;//选择更新或取消更新的弹出框

    private static final String savePath = "/sdcard/apkPackage";//下载包安装路径

    private static final String saveFileName = savePath + "UpdateRelease.apk";//下载包文件名

    private ProgressBar mProgress;//进度条与通知UI刷新的handler和msg常量

    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private int progress;//下载进度值

    private Thread downLoadThread;//下载线程

    private boolean interceptFlag = false;//?


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                default:
                    break;
            }
        }
    };

    public UpdateManager(Context context, String apkUrl) {
        this.context = context;
        this.apkUrl = apkUrl;
    }

    /**
     * 外部接口，让主Acitivity调用
     */
    public void checkUpdateInfo() {
        showNoticeDialog();
    }

    private void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("软件版本更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("软件版本更新");
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_update_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.pb_update_progressBar);

        builder.setView(v);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();

        downloadApk();
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);//更新进度条
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);//点击取消就停止下载
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 下载apk
     */
    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
