package com.hv.heartvoice.Manager;

import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Listener.MusicPlayerListener;
import com.hv.heartvoice.View.activity.SimplePlayerActivity;
import com.hv.player.AudioPlayer;

/**
 * 音乐播放器对外暴露的接口
 */
public interface MusicPlayerManager {

    /**
     * 播放音乐
     * @param uri
     * @param data
     */
    void play(String uri, Song data);

    /**
     * 是否在播放
     * @return
     */
    boolean isPlaying();

    /**
     * 暂停
     */
    void pause();

    /**
     * 继续播放
     */
    void resume();

    /**
     * 添加播放监听器
     * @param listener
     */
    void addMusicPlayerListener(MusicPlayerListener listener);

    /**
     * 移除播放监听器
     * @param listener
     */
    void removeMusicPlayerListener(MusicPlayerListener listener);

    /**
     * 获取当前播放的音乐
     * @return
     */
    Song getData();

    /**
     * 从指定位置播放
     * @param progress
     */
    void seekTo(int progress);

    /**
     * 设置循环模式
     * @param b
     */
    //void setLooping(boolean b);

    /**
     * 是否第一次播放
     * @return
     */
    boolean getFistPlayer();

    AudioPlayer getAudioPlayer();

    void stop();

    void stop_nocall();

}
