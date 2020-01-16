package com.hv.heartvoice.Util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.hv.heartvoice.R;

/**
 * 通知相关工具类
 */
public class NotificationUtil {

    /**
     * 通知渠道
     */
    private static final String IMPORTANCE_HIGH_CHANNEL_ID = "IMPORTANCE_HIGH_CHANNEL_ID";

    /**
     * 通知管理器实例
     */
    private static NotificationManager notificationManager;

    public static Notification getServiceForeground(Context context) {

        //创建渠道 但一个ID只会创建一个
        getNotificationManager(context);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(IMPORTANCE_HIGH_CHANNEL_ID,"重要通知", NotificationManager.IMPORTANCE_LOW);

            //创建渠道
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(context, IMPORTANCE_HIGH_CHANNEL_ID)
                                        .setContentTitle("标题")
                                        .setContentText("内容")
                                        .setSmallIcon(R.mipmap.app_icon)
                                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.app_icon))
                                        .build();

        return  notification;

    }

    /**
     * 获取通知管理器
     * @param context
     */
    private static void getNotificationManager(Context context) {
        if(notificationManager == null){
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }

    /**
     * 显示通知
     * @param
     * @param notification
     */
    public static void showNotification(int id, Notification notification) {
        notificationManager.notify(id,notification);
    }
}
