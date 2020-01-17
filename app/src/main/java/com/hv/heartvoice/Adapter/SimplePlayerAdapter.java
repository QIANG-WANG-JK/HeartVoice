package com.hv.heartvoice.Adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hv.heartvoice.Domain.Song;

public class SimplePlayerAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {

    public SimplePlayerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song item) {
        helper.setText(android.R.id.text1,item.getTitle());
    }
}
