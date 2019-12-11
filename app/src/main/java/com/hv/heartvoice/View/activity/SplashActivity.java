package com.hv.heartvoice.View.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.hv.heartvoice.Base.BaseCommonActivity;
import com.hv.heartvoice.R;

import java.lang.ref.WeakReference;

public class SplashActivity extends BaseCommonActivity {

    private static final int MSG_NEXT = 1;
    private static final String TAG = "SplashActivity";
    private static final long DEFAULT_DELAY_TIME = 3000;
    private InterHandler handler = new InterHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void initViews() {
        super.initViews();
        fullScreen();
    }

    @Override
    public void initListeners() {
        super.initListeners();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(MSG_NEXT);
            }
        },DEFAULT_DELAY_TIME);
    }

    private void next() {
        if(sp.isLogin()){
            startActivityAfterFinsh(MainActivity.class);
        }else{
            startActivityAfterFinsh(LoginActivity.class);
        }
    }

    private static final class InterHandler extends Handler{
        private WeakReference<SplashActivity> mSplashActivity;
        public InterHandler(SplashActivity splashActivity){
            mSplashActivity = new WeakReference<>(splashActivity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SplashActivity splashActivity = mSplashActivity.get();
            if(splashActivity != null){
                switch (msg.what){
                    case MSG_NEXT:
                        splashActivity.next();
                        break;
                }
            }
        }
    }

}

