package net.zgyejy.yudong.modle;

/**
 * Created by Administrator on 2017/2/5 0005.
 */

public class UserInfoMore {
    private String nickname;//昵称
    private String gender;//性别
    private String identity;//身份
    private String qq;//QQ号码
    private String weichat;//微信
    private String kindergarten;//幼儿园
    private String email;//邮箱地址
    private String signature;//个性签名
    private String image;//头像地址
    private int iid;//头像id

    public UserInfoMore() {

    }

    public UserInfoMore(String nickname, String gender, String identity,
                        String qq, String weichat, String kindergarten,
                        String email, String signature, String image, int iid) {
        this.nickname = nickname;
        this.gender = gender;
        this.identity = identity;
        this.qq = qq;
        this.weichat = weichat;
        this.kindergarten = kindergarten;
        this.email = email;
        this.signature = signature;
        this.image = image;
        this.iid = iid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeichat() {
        return weichat;
    }

    public void setWeichat(String weichat) {
        this.weichat = weichat;
    }

    public String getKindergarten() {
        return kindergarten;
    }

    public void setKindergarten(String kindergarten) {
        this.kindergarten = kindergarten;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    @Override
    public String toString() {
        return "UserInfoMore{" +
                "nickname='" + nickname + '\'' +
                ", gender='" + gender + '\'' +
                ", identity='" + identity + '\'' +
                ", qq='" + qq + '\'' +
                ", weichat='" + weichat + '\'' +
                ", kindergarten='" + kindergarten + '\'' +
                ", email='" + email + '\'' +
                ", signature='" + signature + '\'' +
                ", image='" + image + '\'' +
                ", iid=" + iid +
                '}';
    }
}
