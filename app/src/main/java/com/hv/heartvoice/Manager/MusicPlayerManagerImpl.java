package com.hv.heartvoice.Manager;

import android.content.Context;
import android.media.MediaPlayer;

import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Listener.MusicPlayerListener;

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
            for (MusicPlayerListener listener : listeners) {
                listener.onPaused(data);
            }

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

    /**
     * 发布播放中状态
     */
    private void publishPlayingStatus(){
        for (MusicPlayerListener listener : listeners) {
            listener.onPlaying(data);
        }
    }

}
