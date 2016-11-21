package net.zgyejy.yudong.modle;

/**
 * 用户评论列表适配对象
 * Created by FlyKnight on 2016/10/21.
 */

public class Comment {
    //数据名称据接口返回数据确定
    private String userImage;//头像地址
    private String phone;//用户名
    private String content;//评论内容
    private String addtime;//评论时间

    public Comment(String userImage, String phone, String content, String addtime) {
        this.userImage = userImage;
        this.phone = phone;
        this.content = content;
        this.addtime = addtime;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "userImage='" + userImage + '\'' +
                ", phone='" + phone + '\'' +
                ", content='" + content + '\'' +
                ", addtime='" + addtime + '\'' +
                '}';
    }
}
