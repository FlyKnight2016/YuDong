package net.zgyejy.yudong.util.DBManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.zgyejy.yudong.modle.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FlyKnight on 2016/11/2.
 */

public class DBTools {
    private Context context;
    private DBManager dbManager;
    private SQLiteDatabase sd;

    public DBTools(Context context) {
        this.context = context;
        dbManager = new DBManager(context);
    }

    public boolean saveLocalVideo51List(Video video) {
        sd = dbManager.getReadableDatabase();
        String sql = "select subid from " + DBManager.VIDEO_51_LIST + " where id = ?";
        Cursor c = sd.rawQuery(sql, new String[]{video.getId()+""});
        if (c.moveToNext()) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",video.getId());
        contentValues.put("video_name",video.getVideo_name());
        contentValues.put("video_zip",video.getVideo_zip());
        contentValues.put("video_describe",video.getVideo_describe());
        contentValues.put("video_style",video.getVideo_style());
        contentValues.put("video_price",video.getVideo_price());
        contentValues.put("see_num",video.getSee_num());
        contentValues.put("evaluate_num",video.getEvaluate_num());
        contentValues.put("collect_num",video.getCollect_num());
        contentValues.put("video_url",video.getVideo_url());
        contentValues.put("video_addtime",video.getVideo_addtime());
        contentValues.put("cate_id",video.getCate_id());
        sd.insert(DBManager.VIDEO_51_LIST,null,contentValues);
        return true;
    }

    public List<Video> getLocalVideo51List() {
        List<Video> listVideo51 = new ArrayList<>();
        sd = dbManager.getReadableDatabase();
        Cursor c = sd.query(DBManager.VIDEO_51_LIST,null,null,null,null,null,null);
        if (c.moveToFirst()) {
            do{
                int id = c.getInt(c.getColumnIndex("id"));
                String video_name = c.getString(c.getColumnIndex("video_name"));
                String video_zip = c.getString(c.getColumnIndex("video_zip"));
                String video_describe = c.getString(c.getColumnIndex("video_describe"));
                int video_style = c.getInt(c.getColumnIndex("video_style"));
                String video_price = c.getString(c.getColumnIndex("video_price"));
                String see_num = c.getString(c.getColumnIndex("see_num"));
                String evaluate_num = c.getString(c.getColumnIndex("evaluate_num"));
                String collect_num = c.getString(c.getColumnIndex("collect_num"));
                String video_url = c.getString(c.getColumnIndex("video_url"));
                long video_addtime = c.getLong(c.getColumnIndex("video_addtime"));
                int cate_id = c.getInt(c.getColumnIndex("cate_id"));
                Video video = new Video(id,video_name,video_url);
                video.setVideo_zip(video_zip);
                video.setVideo_describe(video_describe);
                video.setVideo_style(video_style);
                video.setVideo_price(video_price);
                video.setSee_num(see_num);
                video.setEvaluate_num(evaluate_num);
                video.setCollect_num(collect_num);
                video.setVideo_addtime(video_addtime);
                video.setCate_id(cate_id);
                listVideo51.add(video);
            }while (c.moveToNext());
            c.close();
            sd.close();
        }
        return listVideo51;
    }
}
