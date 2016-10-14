package net.zgyejy.yudong.modle;

/**
 * Created by FlyKnight on 2016/10/14.
 */

public class Book {
    private String bookName;
    private String bookImage;

    public Book(String bookName) {
        this.bookName = bookName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }
}
