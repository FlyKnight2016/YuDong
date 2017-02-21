package net.zgyejy.yudong.modle;

import java.io.Serializable;

/**
 * mp3text抽象类
 * Created by Administrator on 2016/11/14 0014.
 */

public class Mp3Text implements Serializable{
    private int id;
    private String music;//mp3地址
    private String text;//文档地址
    private int cateId;//父目录id
    private String music_price;//mp3价格
    private String text_price;//文档价格
    private int music_style;//mp3类型
    private int text_style;//文档类型
    private String music_jifen;//mp3积分
    private String text_jifen;//文档积分
    private String music_name;//mp3名字
    private String text_name;//文档名字
    private String cate_name;//父目录名字

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getMusic_price() {
        return music_price;
    }

    public void setMusic_price(String music_price) {
        this.music_price = music_price;
    }

    public String getText_price() {
        return text_price;
    }

    public void setText_price(String text_price) {
        this.text_price = text_price;
    }

    public int getMusic_style() {
        return music_style;
    }

    public void setMusic_style(int music_style) {
        this.music_style = music_style;
    }

    public int getText_style() {
        return text_style;
    }

    public void setText_style(int text_style) {
        this.text_style = text_style;
    }

    public String getMusic_jifen() {
        return music_jifen;
    }

    public void setMusic_jifen(String music_jifen) {
        this.music_jifen = music_jifen;
    }

    public String getText_jifen() {
        return text_jifen;
    }

    public void setText_jifen(String text_jifen) {
        this.text_jifen = text_jifen;
    }

    public String getMusic_name() {
        return music_name;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }

    public String getText_name() {
        return text_name;
    }

    public void setText_name(String text_name) {
        this.text_name = text_name;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    @Override
    public String toString() {
        return "Mp3Text{" +
                "id=" + id +
                ", music='" + music + '\'' +
                ", text='" + text + '\'' +
                ", cateId=" + cateId +
                ", music_price='" + music_price + '\'' +
                ", text_price='" + text_price + '\'' +
                ", music_style=" + music_style +
                ", text_style=" + text_style +
                ", music_jifen='" + music_jifen + '\'' +
                ", text_jifen='" + text_jifen + '\'' +
                ", music_name='" + music_name + '\'' +
                ", text_name='" + text_name + '\'' +
                ", cate_name='" + cate_name + '\'' +
                '}';
    }
}
