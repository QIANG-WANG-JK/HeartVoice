package com.hv.heartvoice.Util;

import java.util.Calendar;

/**
 * 返回当前日期工具类
 */
public class DateUtil {

    /**
     * 获取当前月的某一天
     * @return
     */
    public static int getDayOfMonth(){
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

}
