package com.hv.heartvoice.Manager;

import android.content.Context;

/**
 * 音乐通知管理器
 */
public class MusicNotificationManager {

    private static MusicNotificationManager instance;
    private final Context context;

    public MusicNotificationManager(Context context) {
        this.context = context;
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

}
