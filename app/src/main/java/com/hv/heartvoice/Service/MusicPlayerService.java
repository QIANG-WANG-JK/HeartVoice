package com.hv.heartvoice.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.hv.heartvoice.Manager.ListManager;
import com.hv.heartvoice.Manager.ListManagerImpl;
import com.hv.heartvoice.Manager.MusicNotificationManager;
import com.hv.heartvoice.Manager.MusicPlayerManager;
import com.hv.heartvoice.Manager.MusicPlayerManagerImpl;
import com.hv.heartvoice.Util.LogUtil;
import com.hv.heartvoice.Util.NotificationUtil;
import com.hv.heartvoice.Util.ServiceUtil;

/**
 * 音乐播放相关的服务
 */
public class MusicPlayerService extends Service {

    private MusicNotificationManager musicNotificationManager;

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
     * 获取列表管理器
     * @param context
     * @return
     */
    public static ListManager getListManager(Context context){
        context = context.getApplicationContext();
        ServiceUtil.startService(context,MusicPlayerService.class);
        return ListManagerImpl.getInstance(context);
    }

    /**
     * service创建时调用
     */
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化音乐通知管理器
        musicNotificationManager = MusicNotificationManager.getInstance(getApplicationContext());
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

        /**
         * 设置前台服务
         */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            Notification notification = NotificationUtil.getServiceForeground(getApplicationContext());

            //传0不显示通知
            startForeground(0,notification);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 销毁时调用
     */
    @Override
    public void onDestroy() {
        //停止前台服务 移除之前通知
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
