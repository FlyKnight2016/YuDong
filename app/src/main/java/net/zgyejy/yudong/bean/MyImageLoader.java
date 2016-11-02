package net.zgyejy.yudong.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片三级缓存管理工具
 * Created by Administrator on 2016/9/9.
 */
public class MyImageLoader {
    private Context context;

    private static LruCache<String, Bitmap> mCache = new LruCache<>((int)
            (Runtime.getRuntime().freeMemory() / 4));

    public MyImageLoader(Context context) {
        this.context = context;
    }

    /**
     * @param path      图片的地址
     * @param imageView imageView控件
     * @return
     */
    public Bitmap display(String path, ImageView imageView) {
        Bitmap bitmap = null;
        if (path == null && path.length() <= 0) {
            return bitmap;
        }
        //先去内存缓存中看看有没有图片
        bitmap = loadImageFromReference(path);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return bitmap;
        }

        //去本地文件中的缓存去取
        bitmap = loadImageFromCache(path);
        if (bitmap != null) {
            mCache.put(path, bitmap);//存入缓存区
            return bitmap;
        }
        DownPie(path, imageView);
        //从网络中去取图片

        return bitmap;
    }

    private void DownPie(final String path, final ImageView iv) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int code = msg.what;
                if (code == 1) {
                    Bitmap bm = (Bitmap) msg.obj;
                    iv.setImageBitmap(bm);
                }
            }
        };

        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    InputStream is = con.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);//从网上加载图片
                    mCache.put(path, bitmap);//存入缓存
                    saveLocal(path, bitmap);//存入本地

                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 从缓存中取图片
     * 根据存的一个类似于key
     *
     * @param url
     * @return
     */
    private Bitmap loadImageFromReference(String url) {
        return mCache.get(url);
    }

    private Bitmap loadImageFromCache(String url) {
        //http://www.baidu.com/text/gg.jpg
        String name = url.substring(url.lastIndexOf("/") + 1);
        if (name == null) {
            return null;
        }

        File cacheDir = context.getExternalCacheDir();//返回一个缓存目录
        if (cacheDir == null) {
            return null;
        }

        File[] files = cacheDir.listFiles();
        if (files == null) {
            return null;
        }

        File bitmapFile = null;

        for (File file : files) {
            if (file.getName().equals(name)) {
                bitmapFile = file;
            }
        }
        if (bitmapFile == null) {
            return null;
        }

        Bitmap fileBitmap = null;
        fileBitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath());
        return fileBitmap;
    }

    /**
     * 将图片存入本地SD卡中
     *
     * @param url
     */
    private void saveLocal(String url, Bitmap bitmap) {
        String name = url.substring(url.lastIndexOf("/") + 1);
        File cacheDir = context.getExternalCacheDir();
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(cacheDir, name));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);//写入本地sd卡中
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
