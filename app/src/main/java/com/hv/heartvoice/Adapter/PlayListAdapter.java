package com.hv.heartvoice.Adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Manager.ListManager;
import com.hv.heartvoice.R;

public class PlayListAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {

    private int selectedIndex = -1;

    /**
     * 列表管理器
     */
    private final ListManager listManager;

    public PlayListAdapter(int layoutResId, ListManager listManager) {
        super(layoutResId);

        this.listManager = listManager;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song item) {
        helper.setText(R.id.song_title,String.format("%s - %s",item.getTitle(),item.getSinger().getNickname()));

//        if(item.getId().equals(listManager.getData().getId())){
//            helper.setTextColor(R.id.song_title,mContext.getResources().getColor(R.color.colorPrimary,null));
//        }else{
//            helper.setTextColor(R.id.song_title,mContext.getResources().getColor(R.color.black,null));
//        }

        if(item.getId().equals(listManager.getData().getId())){
            this.selectedIndex = listManager.getDatas().indexOf(item);
            helper.setTextColor(R.id.song_title,mContext.getResources().getColor(R.color.colorPrimary,null));
        }else{
            helper.setTextColor(R.id.song_title,mContext.getResources().getColor(R.color.black,null));
        }

        //删除按钮点击事件
        helper.addOnClickListener(R.id.delete_song);

    }

    public void setSelectedIndex(int index){

        notifyItemChanged(this.selectedIndex);

        this.selectedIndex = index;

        notifyItemChanged(this.selectedIndex);

    }

}
