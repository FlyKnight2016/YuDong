package net.zgyejy.yudong.modle;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/11/11 0011.
 */

public class UserBaseInfo {
    private String username;//用户昵称
    private String image;//用户头像
    private String integralcount;//积分数
    private int[] payVideo;//已付费视频id列表
    private int[] collectVideo;//已收藏视频id列表
    private List<Coupon> coupon;//优惠券列表

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIntegralcount() {
        return integralcount;
    }

    public void setIntegralcount(String integralcount) {
        this.integralcount = integralcount;
    }

    public int[] getPayVideo() {
        return payVideo;
    }

    public void setPayVideo(int[] payVideo) {
        this.payVideo = payVideo;
    }

    public int[] getCollectVideo() {
        return collectVideo;
    }

    public void setCollectVideo(int[] collectVideo) {
        this.collectVideo = collectVideo;
    }

    public List<Coupon> getCoupon() {
        return coupon;
    }

    public void setCoupon(List<Coupon> coupon) {
        this.coupon = coupon;
    }

    @Override
    public String toString() {
        return "UserBaseInfo{" +
                "username='" + username + '\'' +
                ", image='" + image + '\'' +
                ", integralcount='" + integralcount + '\'' +
                ", payVideo=" + Arrays.toString(payVideo) +
                ", collectVideo=" + Arrays.toString(collectVideo) +
                ", coupon=" + coupon +
                '}';
    }
}
