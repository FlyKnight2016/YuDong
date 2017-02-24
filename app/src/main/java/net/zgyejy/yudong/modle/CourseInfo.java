package net.zgyejy.yudong.modle;


import java.io.Serializable;
import java.util.List;

import me.yejy.greendao.VideoIntegral;

/**
 * 课程详细信息（视频列表、mp3、文档）
 * Created by Administrator on 2016/11/14 0014.
 */

public class CourseInfo implements Serializable{
    private List<VideoIntegral> video;//视频列表
    private List<Mp3Text> mp3text;//mp3/文档列表

    public CourseInfo(List<VideoIntegral> video, List<Mp3Text> mp3text) {
        this.video = video;
        this.mp3text = mp3text;
    }

    public List<VideoIntegral> getVideo() {
        return video;
    }

    public void setVideo(List<VideoIntegral> video) {
        this.video = video;
    }

    public List<Mp3Text> getMp3text() {
        return mp3text;
    }

    public void setMp3text(List<Mp3Text> mp3text) {
        this.mp3text = mp3text;
    }

    @Override
    public String toString() {
        return "CourseInfo{" +
                "video=" + video +
                ", mp3text=" + mp3text +
                '}';
    }
}