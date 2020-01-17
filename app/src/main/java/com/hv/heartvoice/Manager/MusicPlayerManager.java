package com.hv.heartvoice.Manager;

import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Listener.MusicPlayerListener;
import com.hv.heartvoice.View.activity.SimplePlayerActivity;

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

}
