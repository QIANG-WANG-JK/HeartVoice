package com.hv.heartvoice.Listener;

import android.media.MediaPlayer;

import com.hv.heartvoice.Domain.Song;

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
     * @param mp
     * @param data
     */
    void onPrepared(MediaPlayer mp, Song data);

    /**
     * 播放进度回调
     * @param data
     */
    void onProgress(Song data);

    /**
     * 播放完毕回调
     * @param mp
     */
    void onCompletion(MediaPlayer mp);

}
