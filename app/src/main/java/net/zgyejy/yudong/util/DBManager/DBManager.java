package net.zgyejy.yudong.util.DBManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.zgyejy.yudong.gloable.Contacts;

/**
 * Created by FlyKnight on 2016/11/2.
 */

public class DBManager extends SQLiteOpenHelper{
    public static final String VIDEO_LIST = "videos.db";//数据库文件名
    public static final String VIDEO_51_LIST = "video_51";//表名(51视频列表)
    public static final String VIDEO_FREE_LIST = "video_free";//表名（免费视频列表）
    public static final String VIDEO_VIP_LIST = "news_favorite";//表名（VIP视频列表）

    public DBManager(Context context) {
        super(context, VIDEO_LIST, null, Integer.parseInt(Contacts.VER));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_1 = "create table " + VIDEO_51_LIST + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id INTEGER,video_name TEXT,video_zip TEXT,video_describe TEXT,video_style INTEGER," +
                "video_price TEXT,see_num TEXT,evaluate_num TEXT,collect_num TEXT,video_url TEXT," +
                "video_addtime LONG,cate_id INTEGER);";
        /*String sql_2 = "create table " + NEWS_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "summary TEXT,icon TEXT,stamp TEXT,title TEXT,nid INTEGER,link TEXT,type INTEGER);";
        String sql_3 = "create table " + NEWSFAVORITE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "summary TEXT,icon TEXT,stamp TEXT,title TEXT,nid INTEGER,link TEXT,type INTEGER);";*/
        db.execSQL(sql_1);
        /*db.execSQL(sql_2);
        db.execSQL(sql_3);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
