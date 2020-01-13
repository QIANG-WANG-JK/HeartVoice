package com.hv.heartvoice.Model.myObserver;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 观察者适配器
 * @param <T>
 */
public class ObserverAdapter<T> implements Observer<T> {

    //执行前
    @Override
    public void onSubscribe(Disposable d) {

    }

    //下一个
    @Override
    public void onNext(T t) {

    }

    //发生错误
    @Override
    public void onError(Throwable e) {

    }

    //回调onNext方法后调用
    @Override
    public void onComplete() {

    }

}
