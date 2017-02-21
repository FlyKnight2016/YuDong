package net.zgyejy.yudong.modle;

import java.util.List;

/**
 * Created by Administrator on 2017/2/5 0005.
 */

public class PortraitAndInfos {
    private String iconUrl;
    private List<UserInfoMore> userinfo;

    public PortraitAndInfos(String iconUrl, List<UserInfoMore> userinfo) {
        this.iconUrl = iconUrl;
        this.userinfo = userinfo;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public List<UserInfoMore> getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(List<UserInfoMore> userinfo) {
        this.userinfo = userinfo;
    }

    @Override
    public String toString() {
        return "PortraitAndInfos{" +
                "iconUrl='" + iconUrl + '\'' +
                ", userinfo=" + userinfo +
                '}';
    }
}
