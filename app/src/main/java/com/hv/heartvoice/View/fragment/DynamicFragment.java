package com.hv.heartvoice.View.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hv.heartvoice.Base.BaseCommonFragment;
import com.hv.heartvoice.R;

public class DynamicFragment extends BaseCommonFragment {

    /**
     * 返回实例
     * @return
     */
    public static DynamicFragment newInstance() {
        Bundle args = new Bundle();
        DynamicFragment fragment = new DynamicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 返回布局
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dynamic,null);
    }

}
