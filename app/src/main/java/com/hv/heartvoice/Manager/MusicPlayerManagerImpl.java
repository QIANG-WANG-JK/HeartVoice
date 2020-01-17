package com.hv.heartvoice.Manager;

import android.content.Context;
import android.media.MediaPlayer;

import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Listener.Consume;
import com.hv.heartvoice.Listener.MusicPlayerListener;
import com.hv.heartvoice.Util.ListUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            //playOrPause();
            
            //回调监听器
            publishPlayingStatus();

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

            //回调监听器
//            for (MusicPlayerListener listener : listeners) {
//                listener.onPaused(data);
//            }
            ListUtil.eachListener(listeners, new Consume<MusicPlayerListener>() {
                @Override
                public void accept(MusicPlayerListener listener) {
                    listener.onPaused(data);
                }
            });

        }
    }

    @Override
    public void resume() {
        if(!isPlaying()){
            mediaPlayer.start();

            //回调监听器
            publishPlayingStatus();
        }
    }

    @Override
    public void addMusicPlayerListener(MusicPlayerListener listener) {

        /**
         * 只有没有包含时才添加
         */
        if(!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    @Override
    public void removeMusicPlayerListener(MusicPlayerListener listener) {
        listeners.remove(listener);
    }

    @Override
    public Song getData() {
        return this.data;
    }

    /**
     * 发布播放中状态
     */
    private void publishPlayingStatus(){
//        for (MusicPlayerListener listener : listeners) {
//            listener.onPlaying(data);
//        }
        //重构
        ListUtil.eachListener(listeners, new Consume<MusicPlayerListener>() {
            @Override
            public void accept(MusicPlayerListener listener) {
                listener.onPlaying(data);
            }
        });

    }

}
