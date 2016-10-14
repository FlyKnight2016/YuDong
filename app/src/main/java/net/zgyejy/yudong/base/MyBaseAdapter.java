package net.zgyejy.yudong.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter 基类
 * Created by Administrator on 2016/9/5.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
    protected Context context;
    protected LayoutInflater layoutInflater;
    protected List<T> myList = new ArrayList<>();

    public MyBaseAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 获得Adapter的所有数据
     *
     * @return
     */
    public List<T> getAdapterData() {
        return myList;
    }

    /**
     * 添加一条数据的方法
     *
     * @param t
     * @param isClearOld
     */
    public void appendDataed(T t, boolean isClearOld) {
        if (t == null)
            return;
        if (isClearOld)
            myList.clear();
        myList.add(t);
    }

    /**
     * 添加一组数据的方法
     *
     * @param t
     * @param isClearOld
     */
    public void appendDataed(List<T> t, boolean isClearOld) {
        if (t == null)
            return;
        if (isClearOld)
            myList.clear();
        myList.addAll(t);
    }


    /**
     * 顶头添加一条数据
     *
     * @param t
     * @param isClearOld
     */
    public void appendDataTop(T t, boolean isClearOld) {
        if (t == null)
            return;
        if (isClearOld)
            myList.clear();
        myList.add(0, t);
    }

    /**
     * 顶头添加一组数据
     *
     * @param t
     * @param isClearOld
     */
    public void appendDataTop(List<T> t, boolean isClearOld) {
        if (t == null)
            return;
        if (isClearOld)
            myList.clear();
        myList.addAll(0, t);
    }

    public void isClear() {
        myList.clear();
    }

    public void updateAdapter() {
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(myList!=null) {
            return myList.size();
        }
        return 0;
    }

    @Override
    public T getItem(int i) {
        if (myList == null || myList.size() < 0)
            return null;
        return myList.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getMyView(position,convertView,parent);
    }

    public abstract View getMyView (int position, View convertView, ViewGroup parent);
}
