package com.hv.heartvoice.Manager;

import android.content.Context;

import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Service.MusicPlayerService;
import com.hv.heartvoice.Util.ResourceUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * 列表管理器默认实现
 */
public class ListManagerImpl implements ListManager{

    private static ListManagerImpl instance;

    private final Context context;

    /**
     * 列表
     */
    private List<Song> datas = new LinkedList<>();

    /**
     * 当前音乐对象
     */
    private Song data;

    /**
     * 音乐播放管理器
     */
    private MusicPlayerManager musicPlayerManager;

    private ListManagerImpl(Context context) {
        this.context = context.getApplicationContext();

        //初始化音乐播放管理器
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(this.context);
    }

    public static synchronized ListManagerImpl getInstance(Context context){
        if(instance == null){
            instance = new ListManagerImpl(context);
        }
        return instance;
    }

    @Override
    public void setDatas(List<Song> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
    }

    @Override
    public List<Song> getDatas() {
        return datas;
    }

    @Override
    public void play(Song data) {
        this.data = data;
        //播放音乐
        musicPlayerManager.play(ResourceUtil.resourceUri(data.getUri()),data);

    }

    @Override
    public void pause() {
        musicPlayerManager.pause();
    }

    @Override
    public void resume() {
        musicPlayerManager.resume();
    }

}
