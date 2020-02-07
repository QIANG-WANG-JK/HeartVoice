package com.hv.heartvoice.Adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Util.LogUtil;

import java.util.List;

/**
 * 歌单详情适配器
 */
public class SongAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {

    /**
     * 监听器接口
     */
    private SongListener songListener;

    /**
     * 选中索引
     */
    private int selectedIndex = -1;

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

        if(selectedIndex == helper.getAdapterPosition()){
            helper.setTextColor(R.id.songPosition,mContext.getResources().getColor(R.color.colorPrimary,null));

            helper.setTextColor(R.id.songTitle,mContext.getResources().getColor(R.color.colorPrimary,null));

            helper.setTextColor(R.id.songerTitle,mContext.getResources().getColor(R.color.colorPrimary,null));

        }else{
            helper.setTextColor(R.id.songPosition,mContext.getResources().getColor(R.color.black,null));

            helper.setTextColor(R.id.songTitle,mContext.getResources().getColor(R.color.black,null));

            helper.setTextColor(R.id.songerTitle,mContext.getResources().getColor(R.color.black,null));
        }

        View songMore = helper.getView(R.id.songMore);

        //设置点击事件
        songMore.setOnClickListener(view->songListener.onMoreClick(item));

    }

    /**
     * 设置选中索引
     * @param index
     */
    public void setSelectedIndex(int index) {

        notifyItemChanged(this.selectedIndex);

        this.selectedIndex = index;

        notifyItemChanged(this.selectedIndex);

    }

    /**
     * 监听器注册设置
     * @param songListener
     */
    public void setSongListener(SongListener songListener) {
        this.songListener = songListener;
    }

    /**
     * 类监听器
     */
    public interface SongListener{

        /**
         * 点击音乐更多
         * @param data
         */
        void onMoreClick(Song data);
    }

}
