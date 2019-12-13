package com.hv.heartvoice.Base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.hv.heartvoice.Util.PreferenceUtil;

import butterknife.ButterKnife;

public class BaseCommonActivity extends BaseActivity {

    /**
     * 缓存工具类
     */
    protected PreferenceUtil sp;

    /**
     * 父类初始化缓存工具类
     */
    @Override
    public void initData() {
        super.initData();
        sp = PreferenceUtil.getInstance(getMainActivity().getApplicationContext());
    }

    /**
     * 绑定ButterKnife
     */
    @Override
    protected void initViews() {
        super.initViews();
        if(isBindView()){
            bindView();
        }
    }

    protected boolean isBindView() {
        return true;
    }

    protected void bindView() {
        ButterKnife.bind(getMainActivity());
    }

    public void startActivity(Class<?> cls){
        startActivity(new Intent(getMainActivity(),cls));
    }

    public void startActivityAfterFinsh(Class<?> cls){
        startActivity(new Intent(getMainActivity(),cls));
        finish();
    }

    public BaseCommonActivity getMainActivity(){
        return this;
    }

    /**
     * 全屏设置
     */
    protected void fullScreen(){
        View decorView = getWindow().getDecorView();
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            decorView.setSystemUiVisibility(View.GONE);
        }else if(Build.VERSION.SDK_INT >= 19){
            int options = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(options);
        }
    }

    /**
     * 隐藏状态栏
     */
    protected void hideStatusBar(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 将内容显示到状态栏
     * 状态栏显示白色
     */
    protected void lightStatusBar(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //获取Window
            Window window = getWindow();
            //设置状态栏背景颜色为透明
            window.setStatusBarColor(Color.TRANSPARENT);
            //去除半透明效果(如果有的话就去除)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：让内容显示到状态栏
            //SYSTEM_UI_FLAG_LAYOUT_STABLE：状态栏文字显示白色
            //SYSTEM_UI_FLAG_LIGHT_STATUS_BAR：状态栏文字显示黑色
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }
    }

    /**
     * 获取状态栏高度
     */
    public int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 兼容全面屏的状态栏高度
     */
    public void setMargins(View view, int l, int t, int r, int b) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(l, t, r, b);
            view.requestLayout();
        }
    }

}
