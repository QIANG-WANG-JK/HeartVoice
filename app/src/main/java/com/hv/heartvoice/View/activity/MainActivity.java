package com.hv.heartvoice.View.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.hv.heartvoice.Base.BaseCommonActivity;
import com.hv.heartvoice.R;

import butterknife.BindView;

public class MainActivity extends BaseCommonActivity {

    @BindView(R.id.text)
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initViews() {
        super.initViews();
        lightStatusBar();
        setMargins(text,0,getStatusBarHeight(getMainActivity()),0,0);
    }
}
