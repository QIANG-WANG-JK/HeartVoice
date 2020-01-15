package com.hv.heartvoice.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.R;

import java.util.List;

/**
 * 歌单详情适配器
 */
public class SongAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {

    /**
     * 构造方法
     * @param layoutResId
     */
    public SongAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * 显示数据
     * @param helper
     * @param item
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song item) {

        //显示位置
        helper.setText(R.id.songPosition,String.valueOf(helper.getAdapterPosition()));

        //显示标题
        helper.setText(R.id.songTitle,item.getTitle());

        //显示歌手
        helper.setText(R.id.songerTitle,item.getSinger().getNickname());

    }
}
