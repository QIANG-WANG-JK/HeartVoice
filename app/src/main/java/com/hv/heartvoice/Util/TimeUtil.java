package com.hv.heartvoice.Util;

/**
 * 日期时间格式化
 */
public class TimeUtil {

    public static String formatMinuteSecond(int data) {
        if(data == 0){
            return "00:00";
        }
//        转秒
        //data /= 1000;

        //计算分钟
        int minute = data / 60;

        int second = data - (minute * 60);

        return String.format("%02d:%02d",minute,second);
    }

}
