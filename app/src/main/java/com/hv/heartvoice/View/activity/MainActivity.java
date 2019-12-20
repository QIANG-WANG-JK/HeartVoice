package com.hv.heartvoice.View.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.hv.heartvoice.Base.BaseTitleActivity;
import com.hv.heartvoice.Domain.User;
import com.hv.heartvoice.Model.Api;
import com.hv.heartvoice.Model.MyObserver.HttpObserver;
import com.hv.heartvoice.Model.Response.DetailResponse;
import com.hv.heartvoice.R;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseTitleActivity {

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
        lightStatusBarAndBAR(Color.TRANSPARENT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setMargins(toolbar,0,getStatusBarHeight(getMainActivity()),0,0);
        setMargins(mainClose,0,getStatusBarHeight(getMainActivity()),0,0);
        setMargins(userHead,0,getStatusBarHeight(getMainActivity()),0,0);
        setMargins(mainNickname,0,getStatusBarHeight(getMainActivity()),0,0);
        setMargins(mainDescription,0,getStatusBarHeight(getMainActivity()),0,0);
        CropImage(R.mipmap.user_head,userHead);
        CropImage(R.mipmap.user_head,userHeadToolBar);
    }

    @Override
    public void initListeners() {
        super.initListeners();
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                fetchData();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
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
