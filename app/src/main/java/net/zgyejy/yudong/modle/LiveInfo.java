package net.zgyejy.yudong.modle;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public class LiveInfo {
    String liveing;//流编号
    String pull_url;//直播地址
    String hls_url;//直播地址
    String hdl_url;//直播地址
    String icon_url;//直播图片

    public LiveInfo(String liveing, String pull_url, String hls_url, String hdl_url, String icon_url) {
        this.liveing = liveing;
        this.pull_url = pull_url;
        this.hls_url = hls_url;
        this.hdl_url = hdl_url;
        this.icon_url = icon_url;
    }

    public String getLiveing() {
        return liveing;
    }

    public void setLiveing(String liveing) {
        this.liveing = liveing;
    }

    public String getPull_url() {
        return pull_url;
    }

    public void setPull_url(String pull_url) {
        this.pull_url = pull_url;
    }

    public String getHls_url() {
        return hls_url;
    }

    public void setHls_url(String hls_url) {
        this.hls_url = hls_url;
    }

    public String getHdl_url() {
        return hdl_url;
    }

    public void setHdl_url(String hdl_url) {
        this.hdl_url = hdl_url;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    @Override
    public String toString() {
        return "LiveInfo{" +
                "liveing='" + liveing + '\'' +
                ", pull_url='" + pull_url + '\'' +
                ", hls_url='" + hls_url + '\'' +
                ", hdl_url='" + hdl_url + '\'' +
                ", icon_url='" + icon_url + '\'' +
                '}';
    }
}
