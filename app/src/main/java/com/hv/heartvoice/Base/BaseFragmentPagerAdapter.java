package com.hv.heartvoice.Base;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * 页面切换碎片适配器
 * @param <T>
 */
public abstract class BaseFragmentPagerAdapter<T> extends FragmentPagerAdapter {

    /**
     * 上下文
     */
    protected final Context context;

    /**
     * 列表数据源
     */
    protected List<T> datas = new ArrayList<>();

    /**
     * 构造方法
     * @param fm
     * @param context
     */
    public BaseFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    /**
     * 获取当前数据
     * @param position
     * @return
     */
    public T getData(int position) {
        return datas.get(position);
    }

    /**
     * 有多少个
     * @return
     */
    @Override
    public int getCount() {
        return datas.size();
    }

    public void setData(List<T> data){
        if(data != null && data.size() > 0){
            this.datas.clear();
            this.datas.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addData(List<T> data){
        if(datas != null && datas.size() > 0) {
            this.datas.addAll(data);
            notifyDataSetChanged();
        }
    }

}
