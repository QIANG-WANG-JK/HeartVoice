package com.hv.heartvoice.View.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.hv.heartvoice.Base.BaseTitleActivity;
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

    @OnClick(R.id.userHeadToolBar)
    public void open(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @OnClick(R.id.mainClose)
    public void close(){
        drawerLayout.closeDrawer(GravityCompat.START);
    }

}
