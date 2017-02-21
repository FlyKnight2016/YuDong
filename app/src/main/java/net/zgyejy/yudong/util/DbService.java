package net.zgyejy.yudong.util;

import android.content.Context;
import net.zgyejy.yudong.MyApplication;

import java.util.List;

import de.greenrobot.dao.query.Query;
import me.yejy.greendao.Book;
import me.yejy.greendao.BookDao;
import me.yejy.greendao.CollectVideoId;
import me.yejy.greendao.CollectVideoIdDao;
import me.yejy.greendao.DaoSession;
import me.yejy.greendao.PayVideoId;
import me.yejy.greendao.PayVideoIdDao;
import me.yejy.greendao.VideoIntegral;
import me.yejy.greendao.VideoIntegralDao;

import static android.R.id.list;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;


/**
 * 数据库工具类
 * Created by Administrator on 2016/11/17 0017.
 */

public class DbService {
    private static DbService instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private BookDao bookDao;
    private VideoIntegralDao videoIntegralDao;
    private CollectVideoIdDao collectVideoIdDao;
    private PayVideoIdDao payVideoIdDao;

    private DbService() {
    }

    public static DbService getInstance(Context context) {
        if (instance == null) {
            instance = new DbService();
            if (appContext == null){
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = MyApplication.getDaoSession(context);
            instance.bookDao = instance.mDaoSession.getBookDao();
            instance.videoIntegralDao = instance.mDaoSession.getVideoIntegralDao();
            instance.collectVideoIdDao = instance.mDaoSession.getCollectVideoIdDao();
            instance.payVideoIdDao = instance.mDaoSession.getPayVideoIdDao();
        }
        return instance;
    }

    /**
     * 根据id得到园长文章对象
     * @param id
     * @return
     */
    public Book loadBook(long id) {
        return bookDao.load(id);
    }

    /**
    * 根据id得到积分视频对象
     * @param id
     * @return
     */
    public VideoIntegral loadVideoIntegral(long id) {
        return videoIntegralDao.load(id);
    }

    /**
     * 根据id判断是否已收藏
     * @param id
     * @return
     */
    public boolean isCollected(long id) {
        if (collectVideoIdDao.load(id) == null) {
            return false;
        }else {
            return true;
        }
    }

    /**
     * 根据id判断是否已付费
     * @param id
     * @return
     */
    public boolean isPayed(long id) {
        if (payVideoIdDao.load(id) == null) {
            return false;
        }else {
            return true;
        }
    }

    public List<VideoIntegral> loadAllVideoIntegral() {
        return videoIntegralDao.loadAll();
    }

    /**
     * 得到所有的园长文章
     * @return
     */
    public List<Book> loadAllBook(){
        return bookDao.loadAll();
    }

    /**
     * 根据参数查找
     * query list with where clause
     * ex: begin_date_time >= ? AND end_date_time <= ?
     * @param where where clause, include 'where' word
     * @param params query parameters
     * @return
     */

    public List<Book> queryBook(String where, String... params){
        return bookDao.queryRaw(where, params);
    }


    /**
     * 添加或更新数据
     * insert or update note
     * @param book
     * @return insert or update note id
     */
    public long saveBook(Book book){
        return bookDao.insertOrReplace(book);
    }

    /**
     * 添加或更新积分视频
     * insert or update note
     * @param videoIntegral
     * @return insert or update note id
     */
    public long saveVideoIntegral(VideoIntegral videoIntegral){
        return videoIntegralDao.insertOrReplace(videoIntegral);
    }

    /**
     * 添加或更新一批数据
     * insert or update noteList use transaction
     * @param list
     */
    public void saveBookLists(final List<Book> list){
        if(list == null || list.isEmpty()){
            return;
        }
        bookDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<list.size(); i++){
                    Book book = list.get(i);
                    bookDao.insertOrReplace(book);
                }
            }
        });

    }

    /**
     * 添加或更新一批积分视频
     * insert or update noteList use transaction
     * @param list
     */
    public void saveVideoIntegralList(final List<VideoIntegral> list){
        if(list == null || list.isEmpty()){
            return;
        }
        videoIntegralDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<list.size(); i++){
                    VideoIntegral videoIntegral = list.get(i);
                    videoIntegralDao.insertOrReplace(videoIntegral);
                }
            }
        });

    }

    /**
     * 添加或更新收藏视频列表
     * insert or update noteList use transaction
     * @param list
     */
    public void saveAllCollectVideo(final long[] list){
        if(list == null || list.length == 0){
            return;
        }
        bookDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<list.length; i++){
                    CollectVideoId collectVideoId = new CollectVideoId(list[i]);
                    collectVideoIdDao.insertOrReplace(collectVideoId);
                }
            }
        });

    }

    /**
     * 添加或更新已付视频列表
     * insert or update noteList use transaction
     * @param list
     */
    public void saveAllPayVideo(final long[] list){
        if(list == null || list.length == 0){
            return;
        }
        bookDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<list.length; i++){
                    PayVideoId payVideoId = new PayVideoId(list[i]);
                    payVideoIdDao.insertOrReplace(payVideoId);
                }
            }
        });

    }

    /**
     * 删除所有数据
     * delete all note
     */
    public void deleteAllBook(){
        bookDao.deleteAll();
    }

    /**
     * 根据id删除相应数据
     * delete note by id
     * @param id
     */
    public void deleteBook(long id){
        bookDao.deleteByKey(id);
    }

    /**
     * 直接删除传入的对象
     * @param book
     */
    public void deleteBook(Book book){
        bookDao.delete(book);
    }
}
