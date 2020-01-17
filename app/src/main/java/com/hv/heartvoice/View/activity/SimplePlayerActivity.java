package com.hv.heartvoice.View.activity;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hv.heartvoice.Base.BaseTitleActivity;
import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Listener.MusicPlayerListener;
import com.hv.heartvoice.Manager.MusicPlayerManager;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Service.MusicPlayerService;
import com.hv.heartvoice.Util.LogUtil;
import com.hv.heartvoice.Util.NotificationUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hv.heartvoice.Util.Constant.Transparent;

/**
 * 简单音乐播放器
 */
public class SimplePlayerActivity extends BaseTitleActivity implements MusicPlayerListener {

    private static final String TAG = "SimplePlayerActivity";

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    @BindView(R.id.songTitle)
    TextView songTitle;

    @BindView(R.id.tv_start)
    TextView tv_start;

    @BindView(R.id.sb_progress)
    SeekBar sb_progress;

    @BindView(R.id.tv_end)
    TextView tv_end;

    @BindView(R.id.bt_previous)
    Button bt_previous;

    @BindView(R.id.bt_play)
    Button bt_play;

    @BindView(R.id.bt_next)
    Button bt_next;

    @BindView(R.id.bt_loop_model)
    Button bt_loop_model;

    private MusicPlayerManager musicPlayerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_player);
    }

    @Override
    public void initListeners() {
        super.initListeners();

        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            /**
             * 当进度改变
             * @param seekBar
             * @param progress
             * @param fromUser
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getMainActivity());

        //测试音乐播放
        String songUrl = "http://dev-courses-misuc.ixuea.com/assets/s1.mp3";

        Song song=new Song();
        song.setUri(songUrl);

        musicPlayerManager.play(songUrl, song);
    }

    /**
     * 界面可见 设置监听器
     */
    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e(TAG,"onResume");

        //设置播放监听器
        musicPlayerManager.addMusicPlayerListener(this);

        //显示播放状态
        showMusicPlayStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //取消监听器
        musicPlayerManager.removeMusicPlayerListener(this);
    }

    @OnClick(R.id.bt_previous)
    public void previous(){

    }

    @OnClick(R.id.bt_play)
    public void play(){
        Notification notification = NotificationUtil.getServiceForeground(getApplicationContext());

        //显示通知
        NotificationUtil.showNotification(1,notification);

        playOrPause();

    }

    /**
     * 播放或暂停
     */
    private void playOrPause() {

        if(musicPlayerManager.isPlaying()){
            musicPlayerManager.pause();
        }else{
            musicPlayerManager.resume();
        }

    }

    @OnClick(R.id.bt_next)
    public void next(){

    }

    @OnClick(R.id.bt_loop_model)
    public void loop(){

    }

    @Override
    protected void initViews() {
        super.initViews();
        //禁用ToolBar按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //设置状态栏透明并且字体黑色
        lightStatusBarAndBAR(Transparent);

        //设置内容到状态栏下
        setMargins(toolbar,0,getStatusBarHeight(getMainActivity()),0,0);
    }

    @OnClick(R.id.back)
    public void back(){
        onBackPressed();
    }

    public static void start(Activity activity){

        Intent intent = new Intent(activity,SimplePlayerActivity.class);

        activity.startActivity(intent);
    }

    /**
     * 播放管理器监听器
     * @param data
     */
    @Override
    public void onPaused(Song data) {
        showPlayStatus();
    }

    @Override
    public void onPlaying(Song data) {
        showPauseStatus();
    }

    /**
     * 显示按钮状态
     */
    private void showPlayStatus() {
        bt_play.setText("播放");
    }

    private void showPauseStatus() {
        bt_play.setText("暂停");
    }

    /**
     * 显示音乐播放状态
     */
    private void showMusicPlayStatus(){
        if(musicPlayerManager.isPlaying()) {
            showPauseStatus();
        }else{
            showPlayStatus();
        }
    }

}
