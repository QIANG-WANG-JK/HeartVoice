package com.hv.heartvoice.Listener;

/**
 * 消费者接口
 * @param <T>
 */
public interface Consume<T> {

    void accept(T t);

}
