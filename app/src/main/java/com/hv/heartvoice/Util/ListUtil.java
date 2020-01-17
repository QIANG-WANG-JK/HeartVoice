package com.hv.heartvoice.Util;

import com.hv.heartvoice.Listener.Consume;
import com.hv.heartvoice.Listener.MusicPlayerListener;

import java.util.List;

/**
 * 列表工具类
 */
public class ListUtil {

    /**
     * 变量每一个接口
     * @param datas
     * @param action
     * @param <T>
     */
    public static <T> void eachListener(List<T> datas, Consume<T> action){

        for (T listener:datas) {
            //将列表中的每一个对象传递给action
            action.accept(listener);
        }

    }

}
