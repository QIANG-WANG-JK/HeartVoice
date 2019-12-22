package com.hv.heartvoice.View.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hv.heartvoice.Base.BaseCommonFragment;
import com.hv.heartvoice.R;

import butterknife.BindView;

public class MusicHallFragment extends BaseCommonFragment {

    /**
     * 列表控件
     */
    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    /**
     * 网格式布局控制器
     */
    private GridLayoutManager manager;

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

    @Override
    protected void initView() {
        super.initView();
        /**
         * 高度固定
         * 可以提高性能
         */
        recyclerView.setHasFixedSize(true);

        //设置显示3列
        manager = new GridLayoutManager(getMainActivity(), 3);
        //需要有layoutManager
        recyclerView.setLayoutManager(manager);


    }
}
