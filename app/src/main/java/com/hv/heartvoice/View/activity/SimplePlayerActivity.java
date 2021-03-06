package com.hv.heartvoice.View.activity;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.hv.heartvoice.Adapter.SimplePlayerAdapter;
import com.hv.heartvoice.Base.BaseTitleActivity;
import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Listener.MusicPlayerListener;
import com.hv.heartvoice.Manager.ListManager;
import com.hv.heartvoice.Manager.MusicPlayerManager;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Service.MusicPlayerService;
import com.hv.heartvoice.Util.NotificationUtil;
import com.hv.heartvoice.Util.TimeUtil;
import com.hv.heartvoice.Util.ToastUtil;
import com.hv.player.AudioPlayer;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hv.heartvoice.Util.Constant.MODEL_LOOP_LIST;
import static com.hv.heartvoice.Util.Constant.MODEL_LOOP_ONE;
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

    private ListManager listManager;

    private SimplePlayerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_player);
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

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getMainActivity());

        recyclerView.setLayoutManager(layoutManager);
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
                if(fromUser){
                    musicPlayerManager.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Song data = listManager.getDatas().get(position);
                if(data != null){
                    listManager.play(data);
                }
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        //初始化列表管理器
        listManager = MusicPlayerService.getListManager(getMainActivity());

        //初始化音乐播放器
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getMainActivity());

        showLoopModel();

        //创建适配器
        adapter = new SimplePlayerAdapter(android.R.layout.simple_expandable_list_item_1);

        recyclerView.setAdapter(adapter);

        adapter.replaceData(listManager.getDatas());

        //列表的滑动删除功能
        //1.item拖拽和滑动回调
        ItemDragAndSwipeCallback swipeCallback = new ItemDragAndSwipeCallback(adapter) {

            /**
             * 获取移动的参数
             * @param recyclerView
             * @param viewHolder
             * @return
             */
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                //arg1 控制拖拽 arg2控制滑动
                return isViewCreateByAdapter(viewHolder) ?
                        makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE,ItemTouchHelper.ACTION_STATE_IDLE)
                        :makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE,ItemTouchHelper.RIGHT);
            }
        };

        //关联列表
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);

        //将帮助类附加到RecycerView
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //开启滑动
        adapter.enableSwipeItem();

        //滑动监听器
;        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {

            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int i) {

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int i) {

            }

            /**
             * 滑动成功
             * @param viewHolder
             * @param position
             */
            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int position) {
                listManager.delete(position);

                /**
                 * 没有音乐关闭界面
                 */
                if(listManager.getDatas().size() == 0){
                    finish();
                }
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1, boolean b) {

            }
        };

        //设置滑动监听
        adapter.setOnItemSwipeListener(onItemSwipeListener);
    }

    /**
     *
     * @return
     * @param viewHolder
     */
    private boolean isViewCreateByAdapter(RecyclerView.ViewHolder viewHolder) {
        int type = viewHolder.getItemViewType();
        return type == 273 || type == 546 || type == 819 || type == 1365;
    }

    public static void start(Activity activity){

        Intent intent = new Intent(activity,SimplePlayerActivity.class);

        activity.startActivity(intent);

    }

    /**
     * 播放或暂停
     */
    private void playOrPause() {
        if(musicPlayerManager.isPlaying()){
            listManager.pause();
        }else{
            listManager.resume();
        }
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

    @Override
    public void onPrepared(AudioPlayer player, Song data) {
        Log.e("TAG","调用了");
        showInitData();

        showDuration();

        //选中当前音乐
        scrollPosition();
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

    private void showInitData() {
        //获取当前正在播放的音乐
        Song data = listManager.getData();

        songTitle.setText(data.getTitle());
    }

    @Override
    public void onProgress(Song data) {
        showProgress();
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

    /**
     * 显示播放进度
     */
    private void showProgress() {
        long progress = musicPlayerManager.getData().getProgress();

        tv_start.setText(TimeUtil.formatMinuteSecond((int)progress));

        sb_progress.setProgress((int) progress);
    }

    /**
     * 显示时长
     */
    private void showDuration() {
        //获取正在播放音乐的时长
        long end = musicPlayerManager.getData().getDuration();

        //将其格式化为分钟:秒
        tv_end.setText(TimeUtil.formatMinuteSecond((int)end));

        //设置到进度条
        sb_progress.setMax((int)end);
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
     * 界面可见 设置监听器
     */
    @Override
    protected void onResume() {
        super.onResume();
        //设置播放监听器
        musicPlayerManager.addMusicPlayerListener(this);

        //显示初始化数据
        showInitData();

        //显示音乐时长
        showDuration();

        //显示播放进度
        showProgress();

        //显示播放状态
        showMusicPlayStatus();

        //选中当前播放的音乐
        scrollPosition();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //取消监听器
        musicPlayerManager.removeMusicPlayerListener(this);
    }

    @OnClick(R.id.bt_previous)
    public void previous(){
        //获取下一首音乐
        Song song = listManager.previous();
        if(song != null){
            listManager.play(song);
        }else{
            ToastUtil.errorShort(R.string.not_play);
        }
    }

    @OnClick(R.id.bt_play)
    public void play(){
//        Notification notification = NotificationUtil.getServiceForeground(getApplicationContext());
//
//        //显示通知
//        NotificationUtil.showNotification(1,notification);

        playOrPause();

    }

    @OnClick(R.id.bt_next)
    public void next(){
        //获取下一首音乐
        Song song = listManager.next();
        if(song != null){
            listManager.play(song);
        }else{
            ToastUtil.errorShort(R.string.not_play);
        }
    }

    @OnClick(R.id.bt_loop_model)
    public void loop(){
        listManager.changeLoopModel();
        showLoopModel();
    }

    @OnClick(R.id.back)
    public void back(){
        onBackPressed();
    }

    private void showLoopModel() {
        int model = listManager.getLoopModel();
        switch (model){
            case MODEL_LOOP_LIST:
                bt_loop_model.setText(R.string.list_loop);
                break;
            case MODEL_LOOP_ONE:
                bt_loop_model.setText(R.string.one_loop);
                break;
            default:
                bt_loop_model.setText(R.string.random_loop);
                break;
        }
    }

}
