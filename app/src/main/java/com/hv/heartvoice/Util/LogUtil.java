package com.hv.heartvoice.Util;

import android.util.Log;

import com.hv.heartvoice.BuildConfig;

/**
 * 日志工具类
 */
public class LogUtil {

    /**
     * 是否是调试状态
     */
    public static boolean isDebug = BuildConfig.DEBUG;

    public static void e(String tag, String value) {
        if(isDebug){
            Log.e(tag,value);
        }
    }

    public static void d(String tag, String value) {
        if(isDebug){
            Log.d(tag,value);
        }
    }

    public static void w(String tag, String value) {
        if(isDebug){
            Log.w(tag,value);
        }
    }

}
