package net.zgyejy.yudong.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.activity.ArticleReadActivity;
import net.zgyejy.yudong.activity.HomeActivity;
import net.zgyejy.yudong.activity.WebReadActivity;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDetectBigUrlFileListener;
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener;
import org.wlf.filedownloader.listener.simple.OnSimpleFileDownloadStatusListener;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActFragment extends Fragment {
    private Bundle bundle;
    private String url;
    private String newFileDir;
    private String newFileName;

    @BindView(R.id.btn_download_text)
    TextView btnDownloadText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileDownloader.registerDownloadStatusListener(mOnFileDownloadStatusListener);
    }

    private OnFileDownloadStatusListener mOnFileDownloadStatusListener = new OnSimpleFileDownloadStatusListener() {

        @Override
        public void onFileDownloadStatusDownloading(DownloadFileInfo downloadFileInfo, float downloadSpeed, long
                remainingTime) {
            // 正在下载，downloadSpeed为当前下载速度，单位KB/s，remainingTime为预估的剩余时间，单位秒
            showToast("正在下载，下载速度为" + downloadSpeed + "KB/s,预估" + remainingTime + "s内下载完毕");
        }

        @Override
        public void onFileDownloadStatusCompleted(DownloadFileInfo downloadFileInfo) {
            // 下载完成（整个文件已经全部下载完成）
            showToast("下载完成！");
        }

        @Override
        public void onFileDownloadStatusFailed(String url, DownloadFileInfo downloadFileInfo, FileDownloadStatusFailReason failReason) {
            // 下载失败了，详细查看失败原因failReason，有些失败原因你可能必须关心

            String failType = failReason.getType();
            String failUrl = failReason.getUrl();// 或：failUrl = url，url和failReason.getType()会是一样的

            if (FileDownloadStatusFailReason.TYPE_URL_ILLEGAL.equals(failType)) {
                // 下载failUrl时出现url错误
                showToast("下载链接错误！");
            } else if (FileDownloadStatusFailReason.TYPE_STORAGE_SPACE_IS_FULL.equals(failType)) {
                // 下载failUrl时出现本地存储空间不足
                showToast("本地存储空间不足！");
            } else if (FileDownloadStatusFailReason.TYPE_NETWORK_DENIED.equals(failType)) {
                // 下载failUrl时出现无法访问网络
                showToast("无法访问网络！");
            } else if (FileDownloadStatusFailReason.TYPE_NETWORK_TIMEOUT.equals(failType)) {
                // 下载failUrl时出现连接超时
                showToast("链接超时！");
            } else {
                // 更多错误....
                showToast("未安装sd卡或其他未知错误！");
            }

            // 查看详细异常信息
            Throwable failCause = failReason.getCause();// 或：failReason.getOriginalCause()

            // 查看异常描述信息
            String failMsg = failReason.getMessage();// 或：failReason.getOriginalCause().getMessage()
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_act, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.iv_act_now, R.id.btn_download_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_act_now:
                url = "http://www.rabbitpre.com/m/QnUzYv0";
                if (bundle == null)
                    bundle = new Bundle();
                bundle.putString("url", url);
                ((HomeActivity) getActivity()).openActivity(WebReadActivity.class, bundle);
                break;
            case R.id.btn_download_text:
                downLoadFile();
                break;
        }
    }

    private void downLoadFile() {
        showToast("开始下载！");
        url = "http://api.zgyejy.net/uploads/video/text/20161115/d60ddeb983f72232cf30383841388491.doc";
        FileDownloader.detect(url, new OnDetectBigUrlFileListener() {
            @Override
            public void onDetectNewDownloadFile(String url, String fileName, String saveDir, long fileSize) {
                // 如果有必要，可以改变文件名称fileName和下载保存的目录saveDir
                newFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                        "FileDownloader";
                newFileName = "2017招生密码.doc";
                FileDownloader.createAndStart(url, newFileDir, newFileName);
            }

            @Override
            public void onDetectUrlFileExist(String url) {
                // 继续下载，自动会断点续传（如果服务器无法支持断点续传将从头开始下载）
                FileDownloader.start(url);
            }

            @Override
            public void onDetectUrlFileFailed(String url, DetectBigUrlFileFailReason failReason) {
                // 探测一个网络文件失败了，具体查看failReason
            }
        });
    }

    @Override
    public void onDestroy() {
        FileDownloader.unregisterDownloadStatusListener(mOnFileDownloadStatusListener);
        super.onDestroy();
    }

    private void showToast(String str) {
        ((HomeActivity) getActivity()).showToast(str);
    }
}
