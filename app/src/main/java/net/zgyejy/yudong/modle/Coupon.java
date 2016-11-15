package net.zgyejy.yudong.modle;

/**
 * 优惠券抽象类
 * Created by Administrator on 2016/11/11 0011.
 */

public class Coupon {
    private long addtime;//开始时间
    private long endtime;//结束时间
    private String content;//优惠内容

    public Coupon(long addtime, long endtime, String content) {
        this.addtime = addtime;
        this.endtime = endtime;
        this.content = content;
    }

    public long getAddtime() {
        return addtime;
    }

    public void setAddtime(long addtime) {
        this.addtime = addtime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "addtime=" + addtime +
                ", endtime=" + endtime +
                ", content='" + content + '\'' +
                '}';
    }
}
