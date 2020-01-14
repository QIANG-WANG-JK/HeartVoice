package com.hv.heartvoice.Adapter;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hv.heartvoice.Domain.BaseMultiItemEntity;
import com.hv.heartvoice.Domain.Sheet;
import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Domain.Title;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Util.ImageUtil;

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
        switch (helper.getItemViewType()){
            case TYPE_TITLE:
                //标题
                Title title = (Title) item;
                //设置标题
                helper.setText(R.id.musicHallTitle,title.getTitle());
                break;
            case TYPE_SHEET:
                //歌单
                Sheet sheet = (Sheet) item;

                //显示图片
                ImageUtil.show((Activity) mContext,helper.getView(R.id.musicHallBanner),sheet.getBanner(),1);

                //设置歌单标题
                helper.setText(R.id.musicHallSheetTitle,sheet.getTitle());

                break;
            case TYPE_SONG:
                //单曲
                Song song = (Song) item;

                //显示封面
                ImageUtil.show((Activity) mContext,helper.getView(R.id.musicHallSongBanner),song.getBanner(),1);

                //设置标题
                helper.setText(R.id.musicHallSongName,song.getTitle());

                //播放量
                helper.setText(R.id.musicHallSongNumber,String.valueOf(song.getClicks_count()));

                //歌手头像
                ImageUtil.show((Activity) mContext,helper.getView(R.id.musicHallSongHead),song.getSinger().getAvatar(),0);

                //歌手昵称
                helper.setText(R.id.musicHallSongNickName,song.getSinger().getNickname());
                break;
        }
    }

}
