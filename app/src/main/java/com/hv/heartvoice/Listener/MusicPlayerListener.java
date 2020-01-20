package com.hv.heartvoice.Listener;

import com.hv.heartvoice.Domain.Song;
import com.hv.player.AudioPlayer;

public interface MusicPlayerListener {

    /**
     * 已经暂停了
     */
    void onPaused(Song data);

    /**
     * 已经播放了
     */
    void onPlaying(Song data);

    /**
     * 播放器准备完毕了
     * @param player
     * @param data
     */
    void onPrepared(AudioPlayer player, Song data);

    /**
     * 播放进度回调
     * @param data
     */
    void onProgress(Song data);

    /**
     * 播放完毕回调
     * @param player
     */
    default void onCompletion(AudioPlayer player){

    }

}
