package com.hv.heartvoice.View.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.hv.heartvoice.Base.BaseTitleActivity;
import com.hv.heartvoice.Domain.User;
import com.hv.heartvoice.Domain.event.CloseEvent;
import com.hv.heartvoice.Model.Api;
import com.hv.heartvoice.Model.MyObserver.HttpObserver;
import com.hv.heartvoice.Model.Response.DetailResponse;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Util.Constant;
import com.hv.heartvoice.Util.ImageUtil;
import com.hv.heartvoice.Util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseTitleActivity {

    public static final String TAG = "MainActivity";

    /**
     * 侧滑控件
     */
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.mainClose)
    ImageView mainClose;
    @BindView(R.id.userHead)
    ImageView userHead;
    @BindView(R.id.mainNickname)
    TextView mainNickname;
    @BindView(R.id.mainDescription)
    TextView mainDescription;
    @BindView(R.id.userHeadToolBar)
    ImageView userHeadToolBar;
    @BindView(R.id.mainMy)
    LinearLayout mainMy;
    @BindView(R.id.mainMyFriends)
    LinearLayout mainMyFriends;
    @BindView(R.id.mainMessage)
    LinearLayout mainMessage;
    @BindView(R.id.mainIdentify)
    LinearLayout mainIdentify;
    @BindView(R.id.mainScan)
    LinearLayout mainScan;
    @BindView(R.id.mainTimingStop)
    LinearLayout mainTimingStop;
    @BindView(R.id.mainChangeBac)
    LinearLayout mainChangeBac;
    @BindView(R.id.mainAboutUs)
    LinearLayout mainAboutUs;
    @BindView(R.id.mainSetting)
    RelativeLayout mainSetting;
    @BindView(R.id.mainLogOutApp)
    RelativeLayout mainLogOutApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initViews() {
        super.initViews();
        EventBus.getDefault().register(this);
        lightStatusBarAndBAR(Constant.Transparent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setMargins(toolbar,0,getStatusBarHeight(getMainActivity()),0,0);
        setMargins(userHead,0,getStatusBarHeight(getMainActivity()),0,0);
        setMargins(mainNickname,0,getStatusBarHeight(getMainActivity()),0,0);
        setMargins(mainDescription,0,getStatusBarHeight(getMainActivity()),0,0);
        //就算无网络也可以加载出站位图
        ImageUtil.showImage(getMainActivity(),userHead,"",0);
        ImageUtil.showImage(getMainActivity(),userHeadToolBar,"",0);
    }

    @Override
    public void initData() {
        super.initData();
        fetchData();
    }

    private void fetchData() {
        Api.getInstance().userDeatil(sp.getUserId())
                .subscribe(new HttpObserver<DetailResponse<User>>(getMainActivity(),false) {
                    @Override
                    public void onSucceeded(DetailResponse<User> data) {
                        next(data.getData());
                    }
                });
    }

    /**
     * 显示信息
     * @param data
     */
    private void next(User data){
        ImageUtil.showImage(getMainActivity(),userHead,data.getAvatar(),0);
        ImageUtil.showImage(getMainActivity(),userHeadToolBar,data.getAvatar(),0);
        mainNickname.setText(data.getNickname());
        mainDescription.setText(data.getDescriptionFormat());
    }

    @OnClick(R.id.userHeadToolBar)
    public void open(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @OnClick(R.id.mainClose)
    public void close(){
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @OnClick(R.id.mainMy)
    public void goMy(){
        ToastUtil.successShort(R.string.my);
    }

    @OnClick(R.id.mainMyFriends)
    public void goMyFriends(){
        ToastUtil.successShort(R.string.myFriends);
    }

    @OnClick(R.id.mainMessage)
    public void goMyMessage(){
        ToastUtil.successShort(R.string.myMessage);
    }

    @OnClick(R.id.mainIdentify)
    public void goIdentify(){
        ToastUtil.successShort(R.string.IdentifySong);
    }

    @OnClick(R.id.mainScan)
    public void goScan(){
        ToastUtil.successShort(R.string.scanIt);
    }

    @OnClick(R.id.mainTimingStop)
    public void goTimingStop(){
        ToastUtil.successShort(R.string.timingStop);
    }

    @OnClick(R.id.mainChangeBac)
    public void goChangeBac(){
        ToastUtil.successShort(R.string.changeBack);
    }

    @OnClick(R.id.mainAboutUs)
    public void goAboutUs(){
        ToastUtil.successShort(R.string.AboutUs);
    }

    @OnClick(R.id.mainSetting)
    public void goSetting(){
        drawerLayout.closeDrawer(GravityCompat.START);
        startActivity(SettingActivity.class);
    }

    @OnClick(R.id.mainLogOutApp)
    public void goLogOutApp(){
        ToastUtil.successShort(R.string.LogOutApp);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void close(CloseEvent event){
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
