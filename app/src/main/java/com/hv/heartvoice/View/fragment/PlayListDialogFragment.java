package com.hv.heartvoice.View.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hv.heartvoice.Adapter.PlayListAdapter;
import com.hv.heartvoice.Base.BaseBottomSheetDialogFragment;
import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Domain.event.PlayListChangedEvent;
import com.hv.heartvoice.Manager.ListManager;
import com.hv.heartvoice.Manager.MusicPlayerManager;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Service.MusicPlayerService;
import com.hv.heartvoice.Util.PreferenceUtil;
import com.hv.heartvoice.Util.ToastUtil;
import com.hv.player.AudioPlayer;
import com.hv.player.Listener.OnNextListener;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hv.heartvoice.Util.Constant.MAIN_THREAD_TRANSCATION;
import static com.hv.heartvoice.Util.Constant.MODEL_LOOP_LIST;
import static com.hv.heartvoice.Util.Constant.MODEL_LOOP_ONE;
import static com.hv.heartvoice.Util.Constant.PLAY_MODEL;

/**
 * 迷你控制器播放列表
 */
public class PlayListDialogFragment extends BaseBottomSheetDialogFragment {

    /**
     * 循环模式
     */
    @BindView(R.id.loop_model)
    TextView loop_model;

    @BindView(R.id.song_count)
    TextView song_count;

    @BindView(R.id.collect_all)
    Button collect_all;

    @BindView(R.id.close)
    ImageView close;

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    @BindView(R.id.play_control)
    ImageView play_control;

    private PlayListAdapter adapter;

    private ListManager listManager;

    private PreferenceUtil sp;

    private MusicPlayerManager musicPlayerManager;

    private AudioPlayer player;

    private InterHandler handler = new InterHandler(this);

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_play_list,container);
    }

    public static void show(FragmentManager fragmentManager){
        
        //创建Fragment
        PlayListDialogFragment fragment = newInstance();

        fragment.show(fragmentManager,"dialog");
        
    }

    public static PlayListDialogFragment newInstance() {
        
        Bundle args = new Bundle();

        PlayListDialogFragment fragment = new PlayListDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initViews() {
        super.initViews();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());
        recyclerView.setLayoutManager(layoutManager);

        //分割线
        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), RecyclerView.VERTICAL);
        recyclerView.addItemDecoration(decoration);

    }

    @Override
    protected void initData() {
        super.initData();

        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getMainActivity());

        player = musicPlayerManager.getAudioPlayer();

        sp = PreferenceUtil.getInstance(getMainActivity());

        //创建列表管理器
        listManager = MusicPlayerService.getListManager(getMainActivity());

        adapter = new PlayListAdapter(R.layout.item_play_list,listManager);
        recyclerView.setAdapter(adapter);

        //设置数据
        adapter.replaceData(listManager.getDatas());

        showLoopModel(sp.getPlayModel(PLAY_MODEL));
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //关闭dialog
                //dismiss();
                int index = listManager.getDatas().indexOf(listManager.getData());

                if(position != index){
                    //播放点击的音乐
                    //listManager.play(listManager.getDatas().get(position));
                    play(position);
                }
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(R.id.delete_song == view.getId()){
                    //删除按钮点击
                    listManager.delete(position);
                }
                notifyData();
                scrollPosition();
            }
        });

    }

    /**
     * 播放当前位置的音乐
     * 处理一下当重复点击相同歌曲时，不重复播放而是跳转歌词界面
     * 使用数据缓存类实现
     * @param position
     */
    private void play(int position) {
        if(musicPlayerManager.getFistPlayer()){
            Song data = adapter.getItem(position);
            listManager.setDatas(adapter.getData());
            listManager.play(data);
        }else{
            Song data = adapter.getItem(position);
            player.stop();
            player.setOnNextListener(new OnNextListener() {
                @Override
                public void onNext() {
                    Message msg = new Message();
                    msg.obj = data;
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    private void showLoopModel(String playModel) {

        int model = Integer.parseInt(playModel);

        int size = listManager.getDatas().size();

        switch (model){
            case MODEL_LOOP_LIST:
                loop_model.setText(String.format("%s(%d)",getString(R.string.list_loop),size));
                break;
            case MODEL_LOOP_ONE:
                loop_model.setText(String.format("%s(%d)",getString(R.string.one_loop),size));
                break;
            default:
                loop_model.setText(String.format("%s(%d)",getString(R.string.random_loop),size));
                break;
        }

    }

    private void notifyData(){

        if(listManager.getDatas().size() == 0){
            //发送通知 关闭迷你音乐播放器
            EventBus.getDefault().post(new PlayListChangedEvent());
            dismiss();
            return;
        }

        showLoopModel(sp.getPlayModel(PLAY_MODEL));
        adapter.replaceData(listManager.getDatas());
        adapter.notifyDataSetChanged();
    }

    private void scrollPosition() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                //获取当前音乐位置
                int index = listManager.getDatas().indexOf(listManager.getData());
                if(index != -1){
                    //滚动到该位置
                    recyclerView.smoothScrollToPosition(index);

                    //选中
                    adapter.setSelectedIndex(index);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        showMusicPlayStatus();
    }

    @OnClick(R.id.play_control)
    public void playControl(){
        if(musicPlayerManager.isPlaying()){
            listManager.pause();
        }else{
            listManager.resume();
        }
        showMusicPlayStatus();
    }

    private void showMusicPlayStatus() {
        if(musicPlayerManager.isPlaying()){
            showPauseStatus();
        }else{
            showPlayStatus();
        }
    }

    private void showPlayStatus() {
        play_control.setSelected(false);
    }

    private void showPauseStatus() {
        play_control.setSelected(true);
    }

    private static final class InterHandler extends Handler {
        private WeakReference<PlayListDialogFragment> mPlayListDialogFragment;
        public InterHandler(PlayListDialogFragment mPlayListDialogFragment){
            this.mPlayListDialogFragment = new WeakReference<>(mPlayListDialogFragment);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ListManager listManager = mPlayListDialogFragment.get().listManager;
            if(mPlayListDialogFragment != null){
                switch (msg.what){
                    case MAIN_THREAD_TRANSCATION:
                        Song data = (Song) msg.obj;
                        listManager.play(data);
                        //选中当前音乐
                        mPlayListDialogFragment.get().scrollPosition();
                        break;
                }
            }
        }
    }

    @OnClick(R.id.close)
    public void close(){
        dismiss();
        //删除全部
        listManager.deleteAll();

        EventBus.getDefault().post(new PlayListChangedEvent());
    }

    @OnClick(R.id.loop_model)
    public void changeLoopModel(){
        showLoopModel(String.valueOf(listManager.changeLoopModel()));
    }

}
