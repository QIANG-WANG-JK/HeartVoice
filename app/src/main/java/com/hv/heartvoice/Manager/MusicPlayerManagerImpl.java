package com.hv.heartvoice.Manager;

import android.content.Context;

/**
 * 实现类
 */
public class MusicPlayerManagerImpl implements MusicPlayerManager {

    private static MusicPlayerManagerImpl manager;

    private final Context context;

    private MusicPlayerManagerImpl(Context context){
        this.context = context.getApplicationContext();
    }

    public static synchronized MusicPlayerManager getInstance(Context context){
        if(manager == null){
            manager = new MusicPlayerManagerImpl(context);
        }

        return manager;
    }

}
