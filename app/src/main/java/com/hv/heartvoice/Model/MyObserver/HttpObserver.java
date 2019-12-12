package com.hv.heartvoice.Model.MyObserver;

import com.hv.heartvoice.Base.BaseCommonActivity;
import com.hv.heartvoice.Base.BaseResponse;
import com.hv.heartvoice.Util.HttpUtil;
import com.hv.heartvoice.Util.LoadingUtil;

import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * 网络请求处理类
 * @param <T>
 */
public abstract class HttpObserver<T> extends ObserverAdapter<T> {

    private static final String TAG = "HttpObserver";

    private BaseCommonActivity activity;

    private boolean isShowLoading;

    public HttpObserver(BaseCommonActivity activity,boolean isShowLoading){
        this.activity = activity;
        this.isShowLoading = isShowLoading;
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        if(isShowLoading){
            LoadingUtil.showLoading(activity);
        }
    }

    public abstract void onSucceeded(T data);

    @Override
    public void onNext(T t) {
        super.onNext(t);
        checkHideLoading();
        if(isSucceeded(t)){
            onSucceeded(t);
        }else{
            requestErrorHandler(t,null);
        }
    }

    public boolean onFailed(T t,Throwable e){
        return false;
    }

    private void checkHideLoading() {
        if(isShowLoading){
            LoadingUtil.hideLoading();
        }
    }

    protected boolean isSucceeded(T t) {
        if(t instanceof Response){
            Response response = (Response) t;
            //无状态码代表成功
            int code = response.code();
            return code >= 200 && code <= 299 ? true : false;
        } else if(t instanceof BaseResponse){
            BaseResponse response = (BaseResponse) t;
            return response.getStatus() == 0;
        }
        return false;
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        requestErrorHandler(null,e);
    }

    protected void requestErrorHandler(T t, Throwable e) {
        if(!onFailed(t,e)){
            HttpUtil.handlerRequest(t,e);
        }
    }

}
