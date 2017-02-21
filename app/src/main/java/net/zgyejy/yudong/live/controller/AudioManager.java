package net.zgyejy.yudong.live.controller;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 录音管理类
 * Created by Administrator on 2017/1/19 0019.
 */

public class AudioManager {
    private MediaRecorder mMediaRecorder;
    private String mDir;
    private String mCurrentFilePath;

    private static AudioManager mInstance;

    private boolean isPrepare;

    private AudioManager(String dir) {
        mDir = dir;
    }

    public static AudioManager getInstance(String dir) {
        if (mInstance == null) {
            synchronized (AudioManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioManager(dir);
                }
            }
        }
        return mInstance;
    }

    /**
     * 使用接口，用于回调
     */
    public interface AudioStateListener {
        void wellPrepared();
    }

    public AudioStateListener mAudioStateListener;

    /**
     * 回调方法
     */
    public void setOnAudioStateListener(AudioStateListener listener) {
        mAudioStateListener = listener;
    }

    /**
     * 录音准备
     */
    public void prepareAudio() {
        try {
            isPrepare = false;
            File dir = new File(mDir);//创建语音存放文件夹
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = generateFileName();
            File file = new File(dir, fileName);

            mCurrentFilePath =file.getAbsolutePath();

            mMediaRecorder = new MediaRecorder();
            // 设置输出文件
            /*mMediaRecorder.setOutputFile(dir.getAbsolutePath());*/
            mMediaRecorder.setOutputFile(mCurrentFilePath);
            // 设置MediaRecorder的音频源为麦克风
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置音频格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            // 设置音频编码
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            // 准备录音
            mMediaRecorder.prepare();
            // 开始
            mMediaRecorder.start();
            // 准备结束
            isPrepare = true;
            if (mAudioStateListener != null) {
                mAudioStateListener.wellPrepared();
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 随机生成文件的名称
     */
    private String generateFileName() {
        return UUID.randomUUID().toString() + ".amr";
    }

    public int getVoiceLevel(int maxlevel) {
        if (isPrepare) {
            try {
                // mMediaRecorder.getMaxAmplitude() 1~32767
                return maxlevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;
            } catch (Exception e) {
            }
        }
        return 1;
    }

    /**
     * 释放资源
     */
    public void release() {
        //mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder = null;
    }

    /**
     * 取消录音
     */
    public void cancel() {
        release();
        if (mCurrentFilePath != null) {
            File file = new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath = null;
        }

    }

    public String getCurrentFilePath() {

        return mCurrentFilePath;
    }
}
