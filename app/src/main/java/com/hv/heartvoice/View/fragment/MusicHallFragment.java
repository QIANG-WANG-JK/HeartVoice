package com.hv.heartvoice.View.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hv.heartvoice.Adapter.MusicHallAdapter;
import com.hv.heartvoice.Base.BaseCommonFragment;
import com.hv.heartvoice.Domain.BaseMultiItemEntity;
import com.hv.heartvoice.Domain.Sheet;
import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Domain.Title;
import com.hv.heartvoice.R;

import java.util.ArrayList;
import java.util.List;

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
     * 音乐馆适配器
     */
    private MusicHallAdapter adapter;

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

    @Override
    protected void initData() {
        super.initData();

        //创建适配器
        adapter = new MusicHallAdapter();

        //设置适配器
        recyclerView.setAdapter(adapter);

        //请求数据
        fetchData();
    }

    /**
     * 数据请求
     */
    private void fetchData() {
        List<BaseMultiItemEntity> datas = new ArrayList<>();

        //添加标题
        datas.add(new Title());

        //添加歌单数据
        for (int i = 0; i < 5; i++) {
            datas.add(new Sheet());
        }

        //添加标题
        datas.add(new Title());

        //添加单曲数据
        for (int i = 0; i < 9; i++) {
            datas.add(new Song());
        }

        //将数据设置到适配器
        adapter.replaceData(datas);

    }
}

