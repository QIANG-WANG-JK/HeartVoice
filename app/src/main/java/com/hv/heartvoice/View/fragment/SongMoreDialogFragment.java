package com.hv.heartvoice.View.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.hv.heartvoice.Base.BaseBottomSheetDialogFragment;
import com.hv.heartvoice.Domain.Sheet;
import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Domain.event.CollectSongClickEvent;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Util.Constant;
import com.hv.heartvoice.Util.ImageUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 歌曲 - 更多对话框
 */
public class SongMoreDialogFragment extends BaseBottomSheetDialogFragment {

    /**
     * 封面图
     */
    @BindView(R.id.iv_banner)
    ImageView iv_banner;

    /**
     * 标题
     */
    @BindView(R.id.tv_title)
    TextView tv_title;

    /**
     * 歌手名称
     */
    @BindView(R.id.tv_singer)
    TextView tv_singer;

    /**
     * 评论数
     */
    @BindView(R.id.tv_comment_count)
    TextView tv_comment_count;

    @BindView(R.id.ll_collect_song)
    LinearLayout ll_collect_song;

    /**
     * 歌单
     */
    private Sheet sheet;

    /**
     * 歌曲
     */
    private Song song;

    @Override
    protected void initData() {
        super.initData();

        sheet = (Sheet) getArguments().getSerializable(Constant.SHEET);
        song = (Song) getArguments().getSerializable(Constant.SONG);

        ImageUtil.show(getMainActivity(),iv_banner,song.getBanner(),0);

        tv_title.setText(song.getTitle());

        tv_singer.setText(song.getSinger().getNickname());

        tv_comment_count.setText(getResources().getString(R.string.comment_count,sheet.getComments_count()));
    }

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

    @OnClick(R.id.ll_collect_song)
    public void onCollectSongClick(){
        //关闭对话框
        dismiss();
        EventBus.getDefault().post(new CollectSongClickEvent());
    }

}
