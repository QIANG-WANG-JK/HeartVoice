package com.hv.heartvoice.Manager;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Listener.Consume;
import com.hv.heartvoice.Listener.MusicPlayerListener;
import com.hv.heartvoice.Util.ListUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.hv.heartvoice.Util.Constant.DEFAULT_TIME;
import static com.hv.heartvoice.Util.Constant.MESSAGE_PROGRESS;

/**
 * 实现类
 */
public class MusicPlayerManagerImpl implements MusicPlayerManager {

    private static MusicPlayerManagerImpl manager;

    private final Context context;

    /**
     * 音乐播放器对象
     */
    private final MediaPlayer mediaPlayer;

    /**
     * 当前播放的音乐对象
     */
    private Song data;

    /**
     * 播放器状态监听器
     */
    private List<MusicPlayerListener> listeners = new ArrayList<>();

    /**
     * 定时器
     */
    private TimerTask timerTask;

    private Timer timer;

    private MusicPlayerManagerImpl(Context context){
        this.context = context.getApplicationContext();

        //初始化播放器
        mediaPlayer = new MediaPlayer();

        //设置播放器监听
        initListeners();
    }

    private void initListeners() {
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            /**
             * 播放器准备开始播放时
             * @param mp
             */
            @Override
            public void onPrepared(MediaPlayer mp) {
                //播放准备时回调
                //将总进度保存至音乐对象
                data.setDuration(mp.getDuration());

                ListUtil.eachListener(listeners, new Consume<MusicPlayerListener>() {
                    @Override
                    public void accept(MusicPlayerListener listener) {
                        listener.onPrepared(mp,data);
                    }
                });

            }
        });

    }

    public static synchronized MusicPlayerManager getInstance(Context context){
        if(manager == null){
            manager = new MusicPlayerManagerImpl(context);
        }

        return manager;
    }

    @Override
    public void play(String uri, Song data) {
        try {
            //保存音乐对象
            this.data = data;
            //释放播放器
            mediaPlayer.reset();
            //设置数据源
            mediaPlayer.setDataSource(uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            //回调监听器
            publishPlayingStatus();
            //启动播放进度通知定时器
            startPublishProgress();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO 处理错误
        }
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void pause() {
        if(isPlaying()){
            mediaPlayer.pause();
            ListUtil.eachListener(listeners, new Consume<MusicPlayerListener>() {
                @Override
                public void accept(MusicPlayerListener listener) {
                    listener.onPaused(data);
                }
            });
            //停止进度
            stopPublishProgress();
        }
    }

    @Override
    public void resume() {
        if(!isPlaying()){
            mediaPlayer.start();
            //回调监听器
            publishPlayingStatus();
            //启动进度通知
            startPublishProgress();
        }
    }

    @Override
    public void addMusicPlayerListener(MusicPlayerListener listener) {
        if(!listeners.contains(listener)){
            listeners.add(listener);
        }
        //启动进度通知
        startPublishProgress();
    }

    @Override
    public void removeMusicPlayerListener(MusicPlayerListener listener) {
        listeners.remove(listener);
    }

    @Override
    public Song getData() {
        return this.data;
    }

    @Override
    public void seekTo(int progress) {
        mediaPlayer.seekTo(progress);
    }

    /**
     * 发布播放中状态
     */
    private void publishPlayingStatus(){
        ListUtil.eachListener(listeners, new Consume<MusicPlayerListener>() {
            @Override
            public void accept(MusicPlayerListener listener) {
                listener.onPlaying(data);
            }
        });
    }

    /**
     * 启动播放进度通知
     */
    private void startPublishProgress(){
        if (listeners.size() == 0){
            return;
        }

        if(timerTask != null){
            //已经启动
            return;
        }
        //创建一个任务
        timerTask = new TimerTask() {

            @Override
            public void run() {
                //如果没有监听器了就停止定时器
                if(listeners.size() == 0){
                    stopPublishProgress();
                    return;
                }
                handler.obtainMessage(MESSAGE_PROGRESS).sendToTarget();
            }
        };

        //创建一个定时器
        timer = new Timer();

        //启动一个持续的任务
        timer.schedule(timerTask,0,DEFAULT_TIME);
    }

    /**
     * 停止播放进度通知
     */
    private void stopPublishProgress() {
        if(timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }

        if (timer !=null){
            timer.cancel();
            timer = null;
        }
    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_PROGRESS:
                    data.setProgress(mediaPlayer.getCurrentPosition());

                    //回调监听
                    ListUtil.eachListener(listeners, new Consume<MusicPlayerListener>() {
                        @Override
                        public void accept(MusicPlayerListener listener) {
                            listener.onProgress(data);
                        }
                    });
                    break;
            }
        }
    };

}
