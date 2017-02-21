package net.zgyejy.yudong.modle;

import java.util.Arrays;
import java.util.List;

import static io.rong.imlib.statistics.UserData.username;

/**
 * Created by Administrator on 2016/11/11 0011.
 */

public class UserBaseInfo {
    private String nickname;//用户昵称
    private String image;//用户头像
    private String integralcount;//积分数
    private long[] payVideo;//已付费视频id列表
    private long[] collectVideo;//已收藏视频id列表
    private List<Coupon> coupon;//优惠券列表

    public String getUsername() {
        return nickname;
    }

    public void setUsername(String nickname) {
        this.nickname = nickname;
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

    public long[] getPayVideo() {
        return payVideo;
    }

    public void setPayVideo(long[] payVideo) {
        this.payVideo = payVideo;
    }

    public long[] getCollectVideo() {
        return collectVideo;
    }

    public void setCollectVideo(long[] collectVideo) {
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
                "username='" + nickname + '\'' +
                ", image='" + image + '\'' +
                ", integralcount='" + integralcount + '\'' +
                ", payVideo=" + Arrays.toString(payVideo) +
                ", collectVideo=" + Arrays.toString(collectVideo) +
                ", coupon=" + coupon +
                '}';
    }
}
