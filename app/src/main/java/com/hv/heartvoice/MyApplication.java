package com.hv.heartvoice;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

import com.hv.heartvoice.Domain.Session;
import com.hv.heartvoice.Util.PreferenceUtil;
import com.hv.heartvoice.Util.ToastUtil;
import es.dmoral.toasty.Toasty;

/**
 * 全局Application
 */
public class MyApplication extends Application {

    public static MyApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Toasty.Config.getInstance().apply();
        ToastUtil.init(getApplicationContext()) ;

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);//初始化MultiDex
    }

    public static MyApplication getContext(){
        return context;
    }

    public void login(PreferenceUtil sp,Session data){
        //保存登录后的session
        sp.setSession(data.getSession());
        sp.setUserId(data.getUser());
        //初始化其它需要登录后初始化的内容
        onLogin();
    }

    private void onLogin() {

    }

    public static MyApplication getInstance(){
        return context;
    }

}
