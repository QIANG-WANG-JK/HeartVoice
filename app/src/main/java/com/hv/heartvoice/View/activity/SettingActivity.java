package com.hv.heartvoice.View.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.hv.heartvoice.Base.BaseTitleActivity;
import com.hv.heartvoice.Domain.event.CloseEvent;
import com.hv.heartvoice.MyApplication;
import com.hv.heartvoice.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hv.heartvoice.Util.Constant.Transparent;

public class SettingActivity extends BaseTitleActivity {

    /**
     * 退出登录
     */
    @BindView(R.id.settingLogOut)
    Button settingLogOut;

    @BindView(R.id.back)
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void initViews() {
        super.initViews();

        //禁用ToolBar按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        EventBus.getDefault().register(this);

        //设置状态栏透明并且字体黑色
        lightStatusBarAndBAR(Transparent);

        //设置内容到状态栏下
        setMargins(toolbar,0,getStatusBarHeight(getMainActivity()),0,0);
    }

    @OnClick(R.id.settingLogOut)
    public void logout(){
        MyApplication.getInstance().logout(sp);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void close(CloseEvent event){
        finish();
    }

    @OnClick(R.id.back)
    public void back(){
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
