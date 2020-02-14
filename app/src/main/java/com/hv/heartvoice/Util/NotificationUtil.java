package com.hv.heartvoice.Util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.hv.heartvoice.Domain.Song;
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

        getNotificationManager(context);

        createNotification();

        Notification notification = new NotificationCompat.Builder(context, IMPORTANCE_HIGH_CHANNEL_ID)
                                        .setContentTitle("标题")
                                        .setContentText("内容")
                                        .setSmallIcon(R.mipmap.app_icon)
                                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.app_icon))
                                        .build();

        return  notification;

    }

    public static void createNotification(){
        //创建渠道 但一个ID只会创建一个

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(IMPORTANCE_HIGH_CHANNEL_ID,"重要通知", NotificationManager.IMPORTANCE_LOW);

            //创建渠道
            notificationManager.createNotificationChannel(channel);
        }
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

    /**
     * 显示音乐通知
     * @param context
     * @param data
     * @param isPlaying
     */
    public static void showMusicNotification(Context context, Song data, boolean isPlaying) {
        //创建通知渠道
        createNotification();

        //创建RemoteView
        //显示自定义通知固定写法
        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_music_layout);

        //创建大通知
        //RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_music_layout_large);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,IMPORTANCE_HIGH_CHANNEL_ID)
                //点击后不会消失
                .setAutoCancel(false)

                .setSmallIcon(R.mipmap.app_icon)

                //设置样式
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())

                .setCustomContentView(contentView);

                //.setCustomBigContentView(大布局);

        NotificationUtil.notify(context,Constant.NOTIFICATION_MUSIC_ID,builder.build());
    }

    /**
     * 显示通知
     * @param context
     * @param id
     * @param build
     */
    private static void notify(Context context, int id, Notification build) {
        getNotificationManager(context);

        //显示通知
        notificationManager.notify(id,build);
    }
}
