package com.hv.heartvoice.Util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * 服务相关工具类
 */
public class ServiceUtil {

    /**
     * 启动service
     * @param cls
     */
    public static void startService(Context context, Class<?> cls) {

        if(!ServiceUtil.isServiceRunning(context,cls)){
            Intent intent = new Intent(context,cls);
            context.startService(intent);
        }

    }

    /**
     * service是否在运行
     * @param context
     * @param cls
     * @return
     */
    public static boolean isServiceRunning(Context context, Class<?> cls) {

        //获取所有运行的服务
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        if(services == null || services.size() ==0 ){
            return false;
        }

        for (ActivityManager.RunningServiceInfo service:services) {
            if(service.service.getClassName().equals(cls.getName())){
                return true;
            }
        }
        return false;
    }

}
