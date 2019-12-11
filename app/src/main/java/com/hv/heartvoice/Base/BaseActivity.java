package com.hv.heartvoice.Base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    /**
     * 初始化View
     */
    protected void initViews(){
        getApplication();
    }

    /**
     * 初始化数据
     */
    public void initData(){

    }

    /**
     * 初始化监听器
     */
    public void initListeners(){

    }

    /**
     * 在onCreate后面调用
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initViews();
        initData();
        initListeners();
    }

}
