package com.hv.heartvoice.Base;

import com.hv.heartvoice.Domain.Session;
import com.hv.heartvoice.Domain.User;
import com.hv.heartvoice.Domain.event.CloseLoginEvent;
import com.hv.heartvoice.Model.Api;
import com.hv.heartvoice.Model.MyObserver.HttpObserver;
import com.hv.heartvoice.Model.Response.DetailResponse;
import com.hv.heartvoice.MyApplication;
import com.hv.heartvoice.View.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;

public class BaseLoginActivity extends BaseCommonActivity {

    /**
     * 登录
     * @param phone
     * @param password
     */
    public void login(String phone,String password,boolean isLogin){
        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        Api.getInstance().login(user)
                .subscribe(new HttpObserver<DetailResponse<Session>>(getMainActivity(),isLogin) {
                    @Override
                    public void onSucceeded(DetailResponse<Session> data) {
                        MyApplication.getContext().login(sp,data.getData());
                        //关闭当前界面并启动主界面
                        startActivityAfterFinsh(MainActivity.class);
                        if(!isLogin){
                            EventBus.getDefault().post(new CloseLoginEvent());
                        }
                    }
                });
    }

}
