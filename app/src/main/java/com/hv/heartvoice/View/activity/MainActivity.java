package com.hv.heartvoice.View.activity;

import android.os.Bundle;

import com.hv.heartvoice.Base.BaseCommonActivity;
import com.hv.heartvoice.R;

public class MainActivity extends BaseCommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initViews() {
        super.initViews();
        hideStatusBar();
    }
}
