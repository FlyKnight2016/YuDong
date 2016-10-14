package net.zgyejy.yudong.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/2.
 */
public class MyPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<View> arrayList = new ArrayList<>();//存储每个ImageView页面中显示的图片

    public MyPagerAdapter(Context context) {
        super();
        this.context = context;
    }

    /**
     * 往集合中添加View
     * @param view
     */
    public void addToAdapterView(View view) {
        arrayList.add(view);
    }

    /**
     * 返回页面数量
     * @return
     */
    @Override
    public int getCount() {
        return arrayList.size();
    }

    /**
     * 判断前后两页面是否同一个页面
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 超出ViewPager的缓存页面，将页面销毁
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(arrayList.get(position));
    }

    /**
     * 将要缓存的View添加进集合中
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(arrayList.get(position));
        return arrayList.get(position);
    }
}
