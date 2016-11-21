package net.zgyejy.yudong.modle;

/**
 * mp3text抽象类
 * Created by Administrator on 2016/11/14 0014.
 */

public class Mp3Text {
    private int id;
    private String music;//mp3地址
    private String text;//文档地址
    private int cateId;//父目录id
    private String music_price;//mp3价格
    private String text_price;//文档价格
    private int music_style;//mp3类型
    private int text_style;//文档类型
    /*private String music_jifen;
    private String text_jifen;
    private String music_name;
    private String text_name;
    private String cate_name;*/

    public Mp3Text(String music, String text, int music_style, int text_style,
                   String music_price, String text_price) {
        this.music = music;
        this.text = text;
        this.music_style = music_style;
        this.text_style = text_style;
        this.music_price = music_price;
        this.text_price = text_price;
    }

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
                '}';
    }
}
