package net.zgyejy.yudong.bean;

import java.io.Serializable;

/**
 * 积分视频
 * Created by FlyKnight on 2016/11/1.
 */

public class VideoIntegral implements Serializable {
    private int id;//视频id
    private String video_name;//视频名称
    private String video_zip;//视频缩略图
    private String video_describe;//视频描述
    private int video_style;//视频类型
    private String video_price;//视频价格
    private String see_num;//浏览次数
    private String evaluate_num;//评论数
    private String collect_num;//收藏次数
    private String video_url;//视频链接
    private long video_addtime;//视频上传时间
    private int cate_id;//父分类id
    private String video_jifen;//视频所需积分



    public VideoIntegral(int id, String video_name, String video_url) {
        this.id = id;
        this.video_name = video_name;
        this.video_url = video_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getVideo_zip() {
        return video_zip;
    }

    public void setVideo_zip(String video_zip) {
        this.video_zip = video_zip;
    }

    public String getVideo_describe() {
        return video_describe;
    }

    public void setVideo_describe(String video_describe) {
        this.video_describe = video_describe;
    }

    public int getVideo_style() {
        return video_style;
    }

    public void setVideo_style(int video_style) {
        this.video_style = video_style;
    }

    public String getVideo_price() {
        return video_price;
    }

    public void setVideo_price(String video_price) {
        this.video_price = video_price;
    }

    public String getSee_num() {
        return see_num;
    }

    public void setSee_num(String see_num) {
        this.see_num = see_num;
    }

    public String getEvaluate_num() {
        return evaluate_num;
    }

    public void setEvaluate_num(String evaluate_num) {
        this.evaluate_num = evaluate_num;
    }

    public String getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(String collect_num) {
        this.collect_num = collect_num;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public long getVideo_addtime() {
        return video_addtime;
    }

    public void setVideo_addtime(long video_addtime) {
        this.video_addtime = video_addtime;
    }

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public String getVideo_jifen() {
        return video_jifen;
    }

    public void setVideo_jifen(String video_jifen) {
        this.video_jifen = video_jifen;
    }

    @Override
    public String toString() {
        return "VideoIntegral{" +
                "video_name='" + video_name + '\'' +
                ", video_url='" + video_url + '\'' +
                ", id=" + id +
                '}';
    }
}
