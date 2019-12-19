package com.hv.heartvoice.View.activity;

import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

import com.hv.heartvoice.Base.BaseTitleActivity;
import com.hv.heartvoice.R;

import butterknife.BindView;

public class MainActivity extends BaseTitleActivity {

    /**
     * 侧滑控件
     */
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initViews() {
        super.initViews();
        //侧滑配置
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getMainActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //添加侧滑监听器
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //同步状态
        actionBarDrawerToggle.syncState();
        //toolbar.setNavigationIcon(R.mipmap.email);设置小按钮图标
    }

}
