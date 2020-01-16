package com.hv.heartvoice.Model;

import com.hv.heartvoice.Base.BaseResponse;
import com.hv.heartvoice.Domain.BaseModel;
import com.hv.heartvoice.Domain.Session;
import com.hv.heartvoice.Domain.Sheet;
import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Domain.User;
import com.hv.heartvoice.Model.response.DetailResponse;
import com.hv.heartvoice.Model.response.ListResponse;
import com.hv.heartvoice.MyApplication;
import com.hv.heartvoice.Util.Constant;
import com.hv.heartvoice.Util.LogUtil;
import com.hv.heartvoice.Util.PreferenceUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    private static Api instance;

    private final Service service;

    private Api(){

        OkHttpClient.Builder ok = new OkHttpClient.Builder();

        Cache cache = new Cache(MyApplication.getInstance().getCacheDir(), Constant.NETWORK_CACHE_SIZE);
        ok.cache(cache);

        if(LogUtil.isDebug){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            ok.addInterceptor(interceptor);
        }

        //公共请求参数
        ok.addNetworkInterceptor(chain -> {
            //获取偏好设置工具类
            PreferenceUtil sp = PreferenceUtil.getInstance(MyApplication.getInstance());

            //获取到request
            Request request = chain.request();

            if(sp.isLogin()){
                String user = sp.getUserId();
                String session = sp.getSession();

                //将id和token添加到请求头
                request = request.newBuilder()
                         .addHeader("Content-Type","application/json")
                         .addHeader("User",user)
                         .addHeader("Authorization",session)
                         .build();
            }

            //继续执行
            return chain.proceed(request);
        });

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

    /**
     * 发送短信验证码
     * @param data
     * @return
     */
    public Observable<DetailResponse<BaseModel>> sendSMSCode(User data){
        return service.sendSMSCode(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 用户详情
     * @param id
     * @param nickname
     * @return
     */
    public Observable<DetailResponse<User>> userDetail(String id,String nickname){
        //添加查询参数
        HashMap<String, String> data = new HashMap<>();
        if (StringUtils.isNotBlank(nickname)) {
            //如果昵称不为空才添加
            data.put(Constant.NICKNAME, nickname);
        }
        return service.userDetail(id, data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取用户详情
     * @param id
     * @return
     */
    public Observable<DetailResponse<User>> userDeatil(String id){
        return userDetail(id,null);
    }

    /**
     * 歌单列表
     * @return
     */
    public Observable<ListResponse<Sheet>> sheets(){
        return service.sheets()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 单曲
     * @return
     */
    public Observable<ListResponse<Song>> songs(){
        return service.songs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 歌单详情
     *
     * @param id
     * @return
     */
    public Observable<DetailResponse<Sheet>> sheetDetail(String id) {
        return service.sheetDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 收藏歌单
     * @param id
     * @return
     */
    public Observable<Response<Void>> collection(String id){
        return service.collect(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 取消收藏歌单
     * @param id
     * @return
     */
    public Observable<Response<Void>> cancelCollection(String id){
        return service.deleteCollect(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
