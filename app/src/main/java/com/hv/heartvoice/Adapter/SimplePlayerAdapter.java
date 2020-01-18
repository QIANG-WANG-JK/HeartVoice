package com.hv.heartvoice.Adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.R;

import java.util.ArrayList;

public class SimplePlayerAdapter extends BaseItemDraggableAdapter<Song, BaseViewHolder> {

    /**
     * 选择索引
     */
    private int selectedIndex = -1;

    public SimplePlayerAdapter(int layoutResId) {
        super(layoutResId,new ArrayList<>());
    }

//    public SimplePlayerAdapter(int layoutResId) {
//        super(layoutResId);
//    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song item) {

        helper.setText(android.R.id.text1,item.getTitle());

        //处理选中状态
        if(selectedIndex == helper.getAdapterPosition()){
            //选中行
            helper.setTextColor(android.R.id.text1,mContext.getResources().getColor(R.color.colorPrimary,null));
        }else{
            //未选中
            helper.setTextColor(android.R.id.text1,mContext.getResources().getColor(R.color.light_grey,null));
        }
    }

    public void setSelectedIndex(int index){

        notifyItemChanged(this.selectedIndex);

        this.selectedIndex = index;

        notifyItemChanged(this.selectedIndex);

    }

}
