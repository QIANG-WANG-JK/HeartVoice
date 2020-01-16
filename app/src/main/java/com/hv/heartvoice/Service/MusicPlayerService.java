package com.hv.heartvoice.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.hv.heartvoice.Manager.MusicPlayerManager;
import com.hv.heartvoice.Manager.MusicPlayerManagerImpl;
import com.hv.heartvoice.Util.LogUtil;
import com.hv.heartvoice.Util.ServiceUtil;

/**
 * 音乐播放相关的服务
 */
public class MusicPlayerService extends Service {

    public MusicPlayerService() {
    }

    /**
     * 保活
     * @param context
     * @return
     */
    public static MusicPlayerManager getMusicPlayerManager(Context context){
        context = context.getApplicationContext();
        ServiceUtil.startService(context,MusicPlayerService.class);
        return MusicPlayerManagerImpl.getInstance(context);
    }

    /**
     * service创建时调用
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 启动service一次调用一次
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 销毁时调用
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
