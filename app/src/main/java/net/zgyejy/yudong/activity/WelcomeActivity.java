package net.zgyejy.yudong.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.base.MyBaseActivity;
import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.gloable.Contacts;
import net.zgyejy.yudong.modle.BaseEntity;
import net.zgyejy.yudong.modle.Version;
import net.zgyejy.yudong.modle.parser.ParserBaseEntity;
import net.zgyejy.yudong.util.CommonUtil;
import net.zgyejy.yudong.util.VolleySingleton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends MyBaseActivity {
    private static final String TAG = "WelcomeActivity";
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.checkUpdate)
    TextView checkUpdate;
    @BindView(R.id.checkProgress)
    ProgressBar checkProgress;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.ll_update)
    LinearLayout llUpdate;
    @BindView(R.id.tv_update_percent)
    TextView tvUpdatePercent;
    private RequestQueue requestQueue;//volley接口对象
    private String apkUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interceptFlag) {
                    interceptFlag = false;
                    tvCancel.setText("取消");
                    downLoadApk();
                }else {
                    interceptFlag = true;
                    tvCancel.setText("重新下载");
                }
            }
        });
        checkUpdate();
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        if (requestQueue == null)
            requestQueue = VolleySingleton.getVolleySingleton(this).getRequestQueue();
        showToast("正在检查版本，请稍候！");
        String urlUpdate = API.APP_POST_UPDATE;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        BaseEntity<Version> baseEntity = ParserBaseEntity.getBaseEntityOfVersion(response);

                        if (baseEntity.getCode() == 201 || baseEntity.getCode() == 200) {//201强制更新，200非强制更新
                            //得到最新下载包链接
                            apkUrl =API.APP_SERVER_IP + baseEntity.getData().getVersionUrl();
                            checkUpdate.setText("发现新版本...");
                            if (!CommonUtil.getIsWifi(WelcomeActivity.this)) {
                                showNoticeDialog();
                            } else {
                                checkUpdate.setText("正在下载更新...");
                                downLoadApk();
                            }
                        } else {
                            showToast("已是最新版本！");
                            openActivity(LeadActivity.class);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> params = new HashMap();
                params.put("app", "android");
                params.put("versionCode", Contacts.VER);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("发现新版本！");
        builder.setMessage("当前非网络环境非WiFi，更新版本可能产生流量，是否继续？");
        builder.setPositiveButton("继续更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                downLoadApk();
            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    private Thread downLoadThread;//下载线程
    private void downLoadApk() {
        llUpdate.setVisibility(View.VISIBLE);
        tvUpdatePercent.setVisibility(View.VISIBLE);
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    private static final String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "apkPackage";//下载包安装路径
    private static final String saveFileName = savePath + File.separator +"UpdateRelease.apk";//下载包文件名
    private int progress;//下载进度值
    private boolean interceptFlag = false;//是否取消下载

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


    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    checkProgress.setProgress(progress);
                    tvUpdatePercent.setText(progress + "%");
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                default:
                    break;
            }
        }
    };

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
        startActivity(intent);
    }

}
