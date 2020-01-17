package com.hv.heartvoice.Listener;

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
}
