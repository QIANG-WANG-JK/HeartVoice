package com.hv.heartvoice.Manager;

import android.content.Context;

import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Listener.MusicPlayerListener;
import com.hv.heartvoice.Service.MusicPlayerService;
import com.hv.heartvoice.Util.NotificationUtil;
import com.hv.player.AudioPlayer;

/**
 * 音乐通知管理器
 */
public class MusicNotificationManager implements MusicPlayerListener {

    private static MusicNotificationManager instance;
    private final Context context;

    private MusicPlayerManager musicPlayerManager;

    public MusicNotificationManager(Context context) {
        this.context = context;

        //获取播放管理器
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(this.context);

        //添加播放管理器监听器
        musicPlayerManager.addMusicPlayerListener(this);
    }

    /**
     * 获取通知管理器实例
     * @param context
     * @return
     */
    public static MusicNotificationManager getInstance(Context context) {
        if(instance == null){
            instance = new MusicNotificationManager(context);
        }
        return instance;
    }

    @Override
    public void onPaused(Song data) {
        //显示通知
        NotificationUtil.showMusicNotification(context,data,false);
    }

    @Override
    public void onPlaying(Song data) {
        //显示通知
        NotificationUtil.showMusicNotification(context,data,true);
    }

    @Override
    public void onPrepared(AudioPlayer player, Song data) {
        //显示通知
        //NotificationUtil.showMusicNotification(context,data,true);
    }

    @Override
    public void onProgress(Song data) {

    }

}
