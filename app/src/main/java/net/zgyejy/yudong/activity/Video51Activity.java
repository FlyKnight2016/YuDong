package net.zgyejy.yudong.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import net.zgyejy.yudong.R;
import net.zgyejy.yudong.adapter.ListMp3Adapter;
import net.zgyejy.yudong.adapter.ListViewAdapter_51;
import net.zgyejy.yudong.adapter.MyPagerAdapter;
import net.zgyejy.yudong.base.MyBaseActivity;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import me.yejy.greendao.VideoIntegral;

import net.zgyejy.yudong.gloable.API;
import net.zgyejy.yudong.modle.CourseInfo;
import net.zgyejy.yudong.modle.Mp3Text;
import net.zgyejy.yudong.modle.parser.ParserCourseInfo;
import net.zgyejy.yudong.util.CommonUtil;
import net.zgyejy.yudong.util.ConstUtil;
import net.zgyejy.yudong.util.PlayService;
import net.zgyejy.yudong.util.VolleySingleton;

import org.json.JSONObject;
import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDetectBigUrlFileListener;
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener;
import org.wlf.filedownloader.listener.simple.OnSimpleFileDownloadStatusListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static net.zgyejy.yudong.util.PlayService.mediaPlayer;

public class Video51Activity extends MyBaseActivity {
    ListView lvVideo51;
    @BindView(R.id.tv_video51_video)
    TextView tvVideo51Video;
    @BindView(R.id.tv_video51_mp3)
    TextView tvVideo51Mp3;
    @BindView(R.id.tv_video51_text)
    TextView tvVideo51Text;
    @BindView(R.id.vp_video51_vmt)
    ViewPager vpVideo51Vmt;
    MyPagerAdapter viewPagerAdapter;
    private int topGuideTag = 0;//当前页面标识
    @BindColor(R.color.themeColor)
    int themeColor;
    @BindColor(R.color.white)
    int whiteColor;

    private CourseInfo courseInfo;//课程详细信息
    private ListViewAdapter_51 adapter;//51视频适配器
    private List<VideoIntegral> list51VideoIntegral;//一课的视频列表(5个视频)
    private String url51;//5个视频列表的链接
    private RequestQueue requestQueue;//volley接口对象

    private List<Mp3Text> mp3Texts;//mp3文档列表
    private Mp3Text mp3Text;//mp3文档信息
    public static TextView mp3Name, timeNow, timeAll;
    private TextView tvMp3CallList;//唤出mp3列表
    private ListView lvMp3List;//mp3列表
    private ListMp3Adapter mp3ListAdapter;
    private boolean isMp3ListShow = false;//mp3列表是否显示的tag;
    private ImageView cd, needle, back, play, front, downLoad,share;
    public static SeekBar mp3Progress;
    boolean isPlaying = false;//是否正在播放
    private MusicBoxReceiver receiver;

    private TextView tv_txtShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video51);
        ShareSDK.initSDK(this);
        ButterKnife.bind(this);
        FileDownloader.registerDownloadStatusListener(mOnFileDownloadStatusListener);

        url51 = getIntent().getStringExtra("result");
        //showToast(url51);

        initView();

        loadListData();
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
            showNoticeDialog();
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

    /**
     * 显示提示小窗口
     */
    private void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("小提示！");
        builder.setMessage("文档存储路径：手机内置内存/FileDownloader/***，或者您也可以在文档管理器中搜索（MP3文件名）快速找到该文档。");
        builder.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

    private Dialog noticeDialog;

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PlayService.class);
        this.stopService(intent);
        if (receiver!=null) {
            try {
                unregisterReceiver(receiver);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        FileDownloader.unregisterDownloadStatusListener(mOnFileDownloadStatusListener);
        super.onDestroy();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        viewPagerAdapter = new MyPagerAdapter(this);
        vpVideo51Vmt.setAdapter(viewPagerAdapter);
        vpVideo51Vmt.setOnPageChangeListener(pageChangeListener);

        LinearLayout linearLayout;

        //加载视频列表页面
        linearLayout = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.layout_video51_list, null);
        lvVideo51 = (ListView) linearLayout.findViewById(R.id.lv_video51);
        if (adapter == null)
            adapter = new ListViewAdapter_51(this);
        lvVideo51.setAdapter(adapter);
        lvVideo51.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                VideoIntegral videoIntegral = adapter.getItem(position);
                bundle.putSerializable("videoIntegral", videoIntegral);
                openActivity(VideoPlayActivity.class, bundle);
            }
        });
        viewPagerAdapter.addToAdapterView(linearLayout);

        //加载MP3页面
        linearLayout = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.layout_video51_mp3, null);

        //初始化mp3界面
        mp3Name = (TextView) linearLayout.findViewById(R.id.tv_mp3_name);
        tvMp3CallList = (TextView) linearLayout.findViewById(R.id.tv_mp3_callList);
        lvMp3List = (ListView) linearLayout.findViewById(R.id.lv_mp3_list);
        timeNow = (TextView) linearLayout.findViewById(R.id.tv_mp3_timeNow);
        timeAll = (TextView) linearLayout.findViewById(R.id.tv_mp3_timeAll);
        cd = (ImageView) linearLayout.findViewById(R.id.iv_mp3_cd);
        needle = (ImageView) linearLayout.findViewById(R.id.iv_mp3_needle);
        back = (ImageView) linearLayout.findViewById(R.id.iv_mp3_back);
        play = (ImageView) linearLayout.findViewById(R.id.iv_mp3_play);
        front = (ImageView) linearLayout.findViewById(R.id.iv_mp3_front);
        share = (ImageView) linearLayout.findViewById(R.id.iv_mp3_share);
        downLoad = (ImageView) linearLayout.findViewById(R.id.iv_mp3_downLoad);
        mp3Progress = (SeekBar) linearLayout.findViewById(R.id.sb_mp3_progress);
        //（加载MP3数据）..................
        tvMp3CallList.setVisibility(View.VISIBLE);//显示列表按钮
        tvMp3CallList.setOnClickListener(listener);

        if (mp3ListAdapter == null)
            mp3ListAdapter = new ListMp3Adapter(this);
        lvMp3List.setAdapter(mp3ListAdapter);
        lvMp3List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isFirstPalying = true;

                Intent intent = new Intent(Video51Activity.this, PlayService.class);
                Video51Activity.this.stopService(intent);
                mp3Text = mp3Texts.get(position);
                showMp3();
                lvMp3List.setVisibility(View.INVISIBLE);
                isMp3ListShow = false;
            }
        });

     mp3Progress.setOnSeekBarChangeListener(sChangeListener);
        play.setOnClickListener(listener);
        needle.setOnClickListener(listener);
        downLoad.setOnClickListener(listener);
        share.setOnClickListener(listener);
        viewPagerAdapter.addToAdapterView(linearLayout);

        //加载文档页面
        linearLayout = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.layout_video51_text, null);
        tv_txtShow = (TextView) linearLayout.findViewById(R.id.tv_txtShow);
        viewPagerAdapter.addToAdapterView(linearLayout);

        viewPagerAdapter.notifyDataSetChanged();

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    tv_txtShow.setText(sb);
                    break;
                default:
                    break;
            }
        }
    };

    BufferedReader buffer = null;
    StringBuffer sb = new StringBuffer();
    String line = null;
    /**
     * 预览txt文档
     */
    private void previewTXT(final String txtUrl) {
        if (sb.length()>0)
            sb.delete(0,sb.length());
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(txtUrl);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    buffer = new BufferedReader(new InputStreamReader(is,"GB2312"));

                    while((line = buffer.readLine())!=null){
                        sb.append(line);
                    }

                    mHandler.sendEmptyMessage(0);

                    buffer.close();
                    is.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * ViewPager页面改变的监听
     */
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            topGuideTag = position;
            setTopGuide();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * SeekBar进度改变事件
     */
    SeekBar.OnSeekBarChangeListener sChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            //当拖动停止后，控制mediaPlayer播放指定位置的音乐
            mediaPlayer.seekTo(seekBar.getProgress());
            PlayService.isChanging = false;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            PlayService.isChanging = true;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // TODO Auto-generated method stub

        }
    };

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            switch (v.getId()) {
                case R.id.tv_mp3_callList:
                    if (isMp3ListShow) {
                        lvMp3List.setVisibility(View.INVISIBLE);
                        isMp3ListShow = false;
                    }else {
                        lvMp3List.setVisibility(View.VISIBLE);
                        isMp3ListShow = true;
                    }
                    break;
                case R.id.iv_mp3_needle:
                case R.id.iv_mp3_play://播放或暂停
                    if (!isPlaying) {
                        play.setImageResource(R.drawable.mp3pause);
                        sendBroadcastToService(ConstUtil.STATE_PLAY);
                        turnCD();
                        needlePlay();
                        isPlaying = true;
                    } else {
                        play.setImageResource(R.drawable.mp3play2);
                        sendBroadcastToService(ConstUtil.STATE_PAUSE);
                        cd.clearAnimation();
                        needlePause();
                        isPlaying = false;
                    }
                    break;
                case R.id.iv_mp3_downLoad:
                    //下载mp3文件
                    downLoadFile(API.APP_SERVER_IP + mp3Text.getMusic());
                    break;
                case R.id.iv_mp3_share:
                    showShare();
                    break;
                default:
                    break;
            }
        }
    };

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(mp3Text.getMusic_name());
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(API.APP_SERVER_IP + mp3Text.getMusic());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mp3Text.getMusic_name());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("给大家分享个音乐！");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.zgyejy.net/");

        // 启动分享GUI
        oks.show(this);
    }

    /**
     * 下载文件
     */
    private void downLoadFile(String url) {
        showToast("开始下载！");
        FileDownloader.detect(url, new OnDetectBigUrlFileListener() {
            @Override
            public void onDetectNewDownloadFile(String url, String fileName, String saveDir, long fileSize) {
                // 如果有必要，可以改变文件名称fileName和下载保存的目录saveDir
                String newFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                        "FileDownloader";
                String newFileName = mp3Text.getMusic_name();
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

    /**
     * 转动cd
     */
    private void turnCD(){
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.cd_animation);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        cd.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //设置上标题和对应数据加载
    private void setTopGuide() {
        setDefaultTopGuide();
        switch (topGuideTag) {
            case 0:
                tvVideo51Video.setBackgroundResource(R.drawable.text_view_back_51);
                tvVideo51Video.setTextColor(themeColor);
                showVideo();
                break;
            case 1:
                tvVideo51Mp3.setBackgroundResource(R.drawable.text_view_back_free);
                tvVideo51Mp3.setTextColor(themeColor);
                showMp3();
                break;
            case 2:
                tvVideo51Text.setBackgroundResource(R.drawable.text_view_back_vip);
                tvVideo51Text.setTextColor(themeColor);
                //先显示列表，点击列表条目后再文档预览
                showText();
                break;
        }
    }

    //显示文档
    private void showText() {
        if (mp3Text!=null) {
            String url = API.APP_SERVER_IP + mp3Text.getText();
            previewTXT(url);
        }else {
            tv_txtShow.setText("暂无文档...");
        }
    }

    //显示MP3
    private void showMp3() {
        if (mp3Text != null) {
            mp3Name.setText(mp3Text.getMusic_name());
            if (isFirstPalying) {
                //注册接收器
                showToast("mp3加载中\n请稍候...");
                if (receiver == null)
                    receiver = new MusicBoxReceiver();
                IntentFilter filter = new IntentFilter();
                filter.addAction(ConstUtil.MUSICBOX_ACTION);
                registerReceiver(receiver, filter);

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(1000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //启动后台Service
                                    Intent intent = new Intent(Video51Activity.this, PlayService.class);
                                    intent.putExtra("mp3Url", API.APP_SERVER_IP + mp3Text.getMusic());//传递过去MP3资源地址
                                    intent.putExtra("from",0);
                                    startService(intent);

                                    isFirstPalying = false;

                                    showToast("mp3缓冲完毕，开始播放！");
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        }
    }

    private boolean isFirstPalying = true;

    private void needlePause() {
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.needle_animation_pause);
        needle.startAnimation(animation);
    }

    private void needlePlay() {
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.needle_animation_play);
        needle.startAnimation(animation);
    }

    //显示视频列表
    private void showVideo() {

    }

    /**
     * 设置上方所有导航标题为未选中状态
     */
    private void setDefaultTopGuide() {

        tvVideo51Video.setBackgroundResource(R.drawable.text_view_border_51);
        tvVideo51Mp3.setBackgroundResource(R.drawable.text_view_border_free);
        tvVideo51Text.setBackgroundResource(R.drawable.text_view_border_vip);

        tvVideo51Video.setTextColor(whiteColor);
        tvVideo51Mp3.setTextColor(whiteColor);
        tvVideo51Text.setTextColor(whiteColor);
    }

    /**
     * 根据传入的url返回课程详细信息
     */
    private void loadListData() {
        if (!CommonUtil.isNetworkAvailable(this)) {
            showToast("当前无网络连接，请连接网络!");
        } else {
            if (requestQueue == null)
                //实例化一个RequestQueue对象
                requestQueue = VolleySingleton.getVolleySingleton(this).getRequestQueue();
            showLoadingDialog(this, "数据正在加载...", true);//显示加载动画
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url51,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            courseInfo = ParserCourseInfo.getCourseInfo(jsonObject.toString());

                            mp3Texts = courseInfo.getMp3text();//获取mp3文档列表
                            mp3ListAdapter.appendDataed(mp3Texts,true);
                            mp3ListAdapter.notifyDataSetChanged();
                            if (mp3Texts!=null&&mp3Texts.size()!=0)
                                mp3Text = mp3Texts.get(0);//得到mp3文档信息

                            list51VideoIntegral = courseInfo.getVideo();
                            sort(list51VideoIntegral);
                            adapter.appendDataed(list51VideoIntegral, true);
                            adapter.updateAdapter();
                            cancelDialog();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                        }
                    }
            );
            requestQueue.add(jsonObjectRequest);
        }
    }

    /**
     * 整理排序课程列表
     *
     */
    private void sort(List<VideoIntegral> videoIntegrals) {
        Collections.sort(videoIntegrals, new Comparator<VideoIntegral>() {
            /**
             * 返回负数表示o1小于o2
             * 返回0表示o1等于o2
             * 返回正数表示o1大于o2
             * @param o1
             * @param o2
             * @return
             */
            @Override
            public int compare(VideoIntegral o1, VideoIntegral o2) {
                String str1 = o1.getVideo_name();
                String str2 = o2.getVideo_name();

                if (str1.contains("第")&&str2.contains("第")
                        &&str1.contains("天")&&str2.contains("天")){
                    String num1 = str1.substring(str1.indexOf("天") - 1, str1.indexOf("天"));
                    String num2 = str2.substring(str1.indexOf("天") - 1, str1.indexOf("天"));
                    return num1.compareTo(num2);
                }
                return 0;
            }
        });
    }

    @OnClick({R.id.tv_video51_video, R.id.tv_video51_mp3, R.id.tv_video51_text, R.id.iv_video51_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_video51_video:
                vpVideo51Vmt.setCurrentItem(0);
                break;
            case R.id.tv_video51_mp3:
                vpVideo51Vmt.setCurrentItem(1);
                break;
            case R.id.tv_video51_text:
                vpVideo51Vmt.setCurrentItem(2);
                break;
            case R.id.iv_video51_return:
                finish();
                break;
        }
    }

    /**
     * 向后台Service发送控制广播
     *
     * @param state int state 控制状态码
     */
    protected void sendBroadcastToService(int state) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setAction(ConstUtil.MUSICSERVICE_ACTION);
        intent.putExtra("control", state);
        //向后台Service发送播放控制的广播
        sendBroadcast(intent);
    }

    //创建一个广播接收器用于接收后台Service发出的广播
    class MusicBoxReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            int play=intent.getIntExtra("play", -1);
            switch (play) {
                case ConstUtil.SERVICE_READY://后台服务准备完毕
                    playPrepared();
                    break;
                case ConstUtil.SERVICE_OVER://音乐播放完毕
                    playComplete();
                    break;
                case ConstUtil.SERVICE_UPDATE://更新进度
                    timeNow.setText(intent.getStringExtra("timeNow"));
                    break;
                default:
                    break;
            }
        }
    }

    private void playPrepared() {
        play.setImageResource(R.drawable.mp3pause);
        /*sendBroadcastToService(ConstUtil.STATE_PLAY);*/
        turnCD();
        isPlaying = true;
    }

    private void playComplete() {
        showToast("音乐播放完了！");
        play.setImageResource(R.drawable.mp3play2);
        sendBroadcastToService(ConstUtil.STATE_PAUSE);
        cd.clearAnimation();
        isPlaying = false;
    }
}
