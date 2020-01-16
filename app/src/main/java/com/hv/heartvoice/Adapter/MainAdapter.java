package com.hv.heartvoice.Adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.hv.heartvoice.Base.BaseFragmentPagerAdapter;
import com.hv.heartvoice.R;
import com.hv.heartvoice.View.fragment.DynamicFragment;
import com.hv.heartvoice.View.fragment.MeFragment;
import com.hv.heartvoice.View.fragment.MusicHallFragment;

/**
 * 主界面ViewPagerAdapter
 */
public class MainAdapter extends BaseFragmentPagerAdapter<Integer> {

    /**
     * 指示器标题
     */
    private static int[] titleName = {R.string.my,R.string.musicHall,R.string.dynamic};

    /**
     * 构造方法
     *
     * @param fm
     * @param context
     */
    public MainAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    /**
     * 返回当前位置Fragment
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return MeFragment.newInstance();
            case 1:
                return MusicHallFragment.newInstance();
            default:
                return DynamicFragment.newInstance();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(titleName[position]);
    }

}
