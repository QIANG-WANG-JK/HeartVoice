package com.hv.heartvoice.View.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hv.heartvoice.Base.BaseCommonFragment;
import com.hv.heartvoice.R;

public class MusicHallFragment extends BaseCommonFragment {

    /**
     * 返回实例
     * @return
     */
    public static MusicHallFragment newInstance() {
        Bundle args = new Bundle();
        MusicHallFragment fragment = new MusicHallFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 布局
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music_hall,null);
    }

}
