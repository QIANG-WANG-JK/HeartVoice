package com.hv.heartvoice.View.activity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hv.heartvoice.Base.BaseCommonActivity;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Util.Constant;
import com.hv.heartvoice.View.fragment.ServiceFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class ServiceApproveActivity extends BaseCommonActivity {

    @BindView(R.id.serviceRelative)
    RelativeLayout serviceRelative;
    @BindView(R.id.serviceFragment)
    FrameLayout serviceFragment;
    @BindView(R.id.serviceBack)
    ImageView serviceBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_approve);
    }

    @Override
    protected void initViews() {
        super.initViews();
        setMargins(serviceRelative,0,getStatusBarHeight(getMainActivity()),0,0);
        lightStatusBarAndBAR(Constant.serviceApproveActivityColor);
    }

    @Override
    public void initData() {
        super.initData();
        ServiceFragment serviceFragment=ServiceFragment.newInstance();
        setFragment(R.id.serviceFragment,serviceFragment);
    }

    @OnClick(R.id.serviceBack)
    public void back(){
        onBackPressed();
    }

}
