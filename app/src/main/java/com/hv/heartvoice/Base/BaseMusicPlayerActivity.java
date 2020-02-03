package com.hv.heartvoice.Base;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Domain.event.PlayListChangedEvent;
import com.hv.heartvoice.Listener.MusicPlayerListener;
import com.hv.heartvoice.Manager.ListManager;
import com.hv.heartvoice.Manager.MusicPlayerManager;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Service.MusicPlayerService;
import com.hv.heartvoice.Util.ImageUtil;
import com.hv.heartvoice.Util.LogUtil;
import com.hv.heartvoice.View.activity.SimplePlayerActivity;
import com.hv.heartvoice.View.fragment.PlayListDialogFragment;
import com.hv.player.AudioPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hv.heartvoice.Util.Constant.MODEL_LOOP_ONE;

/**
 * 通用迷你播放器父类
 */
public class BaseMusicPlayerActivity extends BaseTitleActivity implements MusicPlayerListener {

    @BindView(R.id.playControll)
    LinearLayout playControll;

    @BindView(R.id.playControllBanner)
    ImageView playControllBanner;

    @BindView(R.id.playControllSong)
    TextView playControllSong;

    @BindView(R.id.playControllPlay)
    ImageView playControllPlay;

    @BindView(R.id.playControllProgress)
    ProgressBar playControllProgress;

    /**
     * 列表管理器
     */
    protected ListManager listManager;

    /**
     * 音乐播放管理器
     */
    protected MusicPlayerManager musicPlayerManager;

    protected  AudioPlayer player;

    @Override
    protected void initViews() {
        super.initViews();
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        super.initData();

        //初始化列表管理器
        listManager = MusicPlayerService.getListManager(getMainActivity());

        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getMainActivity());

        this.player = musicPlayerManager.getAudioPlayer();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //添加播放管理监听器
        musicPlayerManager.addMusicPlayerListener(this);

        //显示迷你控制器数据
        showSmallPlayControlData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        musicPlayerManager.removeMusicPlayerListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 播放列表改变事件
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayListChangeEvent(PlayListChangedEvent event){
        showSmallPlayControlData();
    }

    @Override
    public void onPaused(Song data) {
        showPlayStatus();
    }

    @Override
    public void onPlaying(Song data) {
        showPauseStatus();
    }

    @Override
    public void onPrepared(AudioPlayer player, Song data) {
        showInitData(data);
    }

    @Override
    public void onProgress(Song data) {
        //显示播放进度
        showProgress(data);
    }

    @Override
    public void onCompletion(AudioPlayer player) {
        if(listManager.getLoopModel() != MODEL_LOOP_ONE){
            Song next = listManager.next();
            if(next != null){
                listManager.play(next);
            }
        }else{
            Song data = listManager.getData();
            if(data != null){
                listManager.play(data);
            }
        }
    }

    private void showMusicPlayStatus() {
        if(musicPlayerManager.isPlaying()){
            showPauseStatus();
        }else{
            showPlayStatus();
        }
    }

    private void showPlayStatus() {
        playControllPlay.setSelected(false);
    }

    private void showPauseStatus() {
        playControllPlay.setSelected(true);
    }

    private void showProgress(Song data) {
        playControllProgress.setProgress((int) data.getProgress());
    }

    private void showDuration(Song data) {
        int end = (int)data.getDuration();
        //设置到进度条
        playControllProgress.setMax(end);
    }

    /**
     * 显示初始化数据
     * @param data
     */
    private void showInitData(Song data) {
        //封面
        ImageUtil.show(getMainActivity(),playControllBanner,data.getBanner(),0);
        //标题
        playControllSong.setText(data.getTitle());
    }

    protected void showSmallPlayControlData() {
        if(listManager.getDatas() != null && listManager.getDatas().size() > 0){
            playControll.setVisibility(View.VISIBLE);
            Song data = listManager.getData();
            if(data != null){
                //显示初始化数据
                showInitData(data);

                //显示音乐时长
                showDuration(data);

                //显示播放状态
                showMusicPlayStatus();

                showProgress(data);
            }
        }else{
            playControll.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.playControll)
    public void playControlContainer(){
        SimplePlayerActivity.start(getMainActivity());
    }

    @OnClick(R.id.playControllPlay)
    public void playControl(){
        if (musicPlayerManager.isPlaying()){
            listManager.pause();
        }else{
            listManager.resume();
        }
    }

    @OnClick(R.id.playControllMusicList)
    public void musicList(){
        showPlayListDialog();
    }

    private void showPlayListDialog() {
        PlayListDialogFragment.show(getSupportFragmentManager());
    }

    public int getCurrentSongPosition(){
        return listManager.getDatas().indexOf(listManager.getData());
    }

}
