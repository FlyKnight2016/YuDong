package net.zgyejy.yudong.live.controller;

import android.media.MediaPlayer;

/**
 * 语音播放控制器
 * Created by Administrator on 2017/1/19 0019.
 */

public class MediaManager {
    private static MediaPlayer mMediaPlayer;
    private static boolean isPause;

    /**
     * 播放音乐
     * @param filePath
     * @param onCompletionListener
     */
    public static void playSound(String filePath,MediaPlayer.OnCompletionListener onCompletionListener) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();

            //设置一个error监听器
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                    mMediaPlayer.reset();
                    return false;
                }
            });
        } else {
            mMediaPlayer.reset();
        }

        try {
            mMediaPlayer.setAudioStreamType(3);//AudioManager.STREAM_MUSIC = 3
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {

        }
    }

    /**
     * 暂停播放
     */
    public static void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) { //正在播放的时候
            mMediaPlayer.pause();
            isPause = true;
        }
    }

    /**
     * 当前是isPause状态
     */
    public static void resume() {
        if (mMediaPlayer != null && isPause) {
            mMediaPlayer.start();
            isPause = false;
        }
    }

    /**
     * 释放资源
     */
    public static void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
