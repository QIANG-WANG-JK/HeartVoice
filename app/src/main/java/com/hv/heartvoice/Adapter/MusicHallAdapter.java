package com.hv.heartvoice.Adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hv.heartvoice.Domain.BaseMultiItemEntity;
import com.hv.heartvoice.R;

import java.util.ArrayList;

import static com.hv.heartvoice.Util.Constant.TYPE_SHEET;
import static com.hv.heartvoice.Util.Constant.TYPE_SONG;
import static com.hv.heartvoice.Util.Constant.TYPE_TITLE;

public class MusicHallAdapter extends BaseMultiItemQuickAdapter<BaseMultiItemEntity, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     */
    public MusicHallAdapter() {
        //第一次要传入数据
        super(new ArrayList<>());

        //添加多类型布局
        //添加标题类型
        addItemType(TYPE_TITLE, R.layout.item_title);

        //添加歌单类型
        addItemType(TYPE_SHEET,R.layout.item_sheet);

        //添加单曲类型
        addItemType(TYPE_SONG,R.layout.item_song);
    }

    /**
     * 绑定数据的方法
     * 框架内部自己处理
     * @param helper
     * @param item
     */
    @Override
    protected void convert(@NonNull BaseViewHolder helper, BaseMultiItemEntity item) {

    }

}
