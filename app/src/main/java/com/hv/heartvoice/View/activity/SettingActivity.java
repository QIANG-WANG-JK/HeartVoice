package com.hv.heartvoice.View.activity;

import android.os.Bundle;
import android.widget.Button;

import com.hv.heartvoice.Base.BaseTitleActivity;
import com.hv.heartvoice.Domain.event.CloseEvent;
import com.hv.heartvoice.MyApplication;
import com.hv.heartvoice.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseTitleActivity {

    @BindView(R.id.settingLogOut)
    Button settingLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void initViews() {
        super.initViews();
        EventBus.getDefault().register(this);
    }

    @OnClick(R.id.settingLogOut)
    public void logout(){
        MyApplication.getInstance().logout(sp);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void close(CloseEvent event){
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
