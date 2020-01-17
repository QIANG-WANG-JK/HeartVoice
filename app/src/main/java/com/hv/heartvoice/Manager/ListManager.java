package com.hv.heartvoice.Manager;

import com.hv.heartvoice.Domain.Song;

import java.util.List;

/**
 * 列表管理器
 * 封装列表相关的操作
 */
public interface ListManager {

    /**
     * 设置播放列表
     * @param datas
     */
    void setDatas(List<Song> datas);

    /**
     * 获取播放列表
     * @return
     */
    List<Song> getDatas();

    /**
     * 播放
     * @param data
     */
    void play(Song data);

    /**
     * 暂停
     */
    void pause();

    /**
     * 播放
     */
    void resume();

}
