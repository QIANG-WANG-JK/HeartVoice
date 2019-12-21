package com.hv.heartvoice.View.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hv.heartvoice.Base.BaseTitleActivity;
import com.hv.heartvoice.Domain.User;
import com.hv.heartvoice.Model.Api;
import com.hv.heartvoice.Model.MyObserver.HttpObserver;
import com.hv.heartvoice.Model.Response.DetailResponse;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Util.Constant;

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
        lightStatusBarAndBAR(Constant.Transparent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setMargins(toolbar,0,getStatusBarHeight(getMainActivity()),0,0);
        setMargins(mainClose,0,getStatusBarHeight(getMainActivity()),0,0);
        setMargins(userHead,0,getStatusBarHeight(getMainActivity()),0,0);
        setMargins(mainNickname,0,getStatusBarHeight(getMainActivity()),0,0);
        setMargins(mainDescription,0,getStatusBarHeight(getMainActivity()),0,0);
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
        if (TextUtils.isEmpty(data.getAvatar())){
            //没有头像,显示默认头像
            Glide.with(getMainActivity()).load(R.mipmap.user_head)
                    .placeholder(R.mipmap.place_holder)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(userHead);
            Glide.with(getMainActivity()).load(R.mipmap.user_head)
                    .placeholder(R.mipmap.place_holder)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(userHeadToolBar);
        }else{
            //有头像
            if(data.getAvatar().startsWith("http")){
                //绝对路径 第三方登录时
                Glide.with(getMainActivity()).load(data.getAvatar())
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .placeholder(R.mipmap.place_holder)
                        .into(userHead);
                Glide.with(getMainActivity()).load(data.getAvatar())
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .placeholder(R.mipmap.place_holder)
                        .into(userHeadToolBar);
            }else{
                //相对路径
                //自身服务器内的图片
                String uri = String.format(Constant.RESOURCE_ENDPOINT, data.getAvatar());
                Glide.with(getMainActivity())
                        .load(uri)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .placeholder(R.mipmap.place_holder)
                        .into(userHead);
                Glide.with(getMainActivity())
                        .load(uri)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .placeholder(R.mipmap.place_holder)
                        .into(userHeadToolBar);
            }
        }
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
