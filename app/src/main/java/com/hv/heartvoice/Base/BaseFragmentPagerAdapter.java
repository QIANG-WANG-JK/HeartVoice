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
public abstract class BaseFragmentPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {

    protected final Context context;

    protected List<T> datas = new ArrayList<>();

    public BaseFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public T getItem(int i) {
        return datas.get(i);
    }

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
