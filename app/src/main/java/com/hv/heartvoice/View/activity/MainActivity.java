package com.hv.heartvoice.View.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.hv.heartvoice.Base.BaseTitleActivity;
import com.hv.heartvoice.Domain.User;
import com.hv.heartvoice.Model.Api;
import com.hv.heartvoice.Model.MyObserver.HttpObserver;
import com.hv.heartvoice.Model.Response.DetailResponse;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Util.Constant;
import com.hv.heartvoice.Util.ImageUtil;
import com.hv.heartvoice.Util.LogUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initViews() {
        super.initViews();
        lightStatusBarAndBAR(Constant.Transparent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setMargins(toolbar,0,getStatusBarHeight(getMainActivity()),0,0);
        setMargins(mainClose,0,getStatusBarHeight(getMainActivity()),0,0);
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

}
