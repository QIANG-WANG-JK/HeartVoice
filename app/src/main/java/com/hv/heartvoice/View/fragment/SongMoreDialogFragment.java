package com.hv.heartvoice.View.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;

import com.hv.heartvoice.Base.BaseBottomSheetDialogFragment;
import com.hv.heartvoice.Domain.Sheet;
import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Util.Constant;

/**
 * 歌曲 - 更多对话框
 */
public class SongMoreDialogFragment extends BaseBottomSheetDialogFragment {

    /**
     * 返回布局。
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_song_more,null);
    }

    /**
     * 构造方法
     * @param data
     * @param song
     * @return
     */
    public static SongMoreDialogFragment newInstance(Sheet data, Song song) {
        //创建bundle
        Bundle args = new Bundle();

        //创建fragment
        SongMoreDialogFragment fragment = new SongMoreDialogFragment();

        //添加参数
        args.putSerializable(Constant.SHEET,data);
        args.putSerializable(Constant.SONG,song);

        //设置参数
        fragment.setArguments(args);

        //返回fragment
        return fragment;
    }

    /**
     * 显示对话框
     * @param manager
     * @param sheet
     * @param song
     */
    public static void show(FragmentManager manager,Sheet sheet,Song song){
        SongMoreDialogFragment songMoreDialogFragment = newInstance(sheet, song);

        //显示
        songMoreDialogFragment.show(manager,"song_more_dialog");
    }

}
