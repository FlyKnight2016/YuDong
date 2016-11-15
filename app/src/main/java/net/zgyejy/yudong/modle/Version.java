package net.zgyejy.yudong.modle;

/**
 * 版本抽象类
 * Created by Administrator on 2016/11/15 0015.
 */

public class Version {
    String versionUrl;

    public Version(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    @Override
    public String toString() {
        return "Version{" +
                "versionUrl='" + versionUrl + '\'' +
                '}';
    }
}
