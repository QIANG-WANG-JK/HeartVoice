package com.hv.heartvoice.View.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.hv.heartvoice.Base.BaseTitleActivity;
import com.hv.heartvoice.R;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hv.heartvoice.Util.Constant.Transparent;

/**
 * 用户详情
 */
public class UserDetailActivity extends BaseTitleActivity {

    @BindView(R.id.back)
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
    }

    @Override
    protected void initViews() {
        super.initViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //设置状态栏透明并且字体黑色
        lightStatusBarAndBAR(Transparent);

        //设置内容到状态栏下
        setMargins(toolbar,0,getStatusBarHeight(getMainActivity()),0,0);
    }

    @OnClick(R.id.back)
    public void back(){
        onBackPressed();
    }

}
