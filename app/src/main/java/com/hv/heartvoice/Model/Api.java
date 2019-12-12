package com.hv.heartvoice.Model;

import com.hv.heartvoice.Base.BaseResponse;
import com.hv.heartvoice.Domain.BaseModel;
import com.hv.heartvoice.Domain.Session;
import com.hv.heartvoice.Domain.User;
import com.hv.heartvoice.Model.Response.DetailResponse;
import com.hv.heartvoice.Util.Constant;
import com.hv.heartvoice.Util.LogUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    private static Api instance;

    private final Service service;

    private Api(){
        OkHttpClient.Builder ok = new OkHttpClient.Builder();
        if(LogUtil.isDebug){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            ok.addInterceptor(interceptor);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .client(ok.build())
                .baseUrl(Constant.ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(Service.class);
    }

    public static Api getInstance(){
        if(instance == null){
            instance = new Api();
        }
        return instance;
    }

    /**
     * 注册
     * @param data
     * @return
     */
    public Observable<DetailResponse<BaseModel>> register(User data){
        return service.register(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 登录
     * @param data
     * @return
     */
    public Observable<DetailResponse<Session>> login(User data){
        return service.login(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 重置密碼
     * @param data
     * @return
     */
    public Observable<BaseResponse> resetPassword(User data){
        return service.resetPassword(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
