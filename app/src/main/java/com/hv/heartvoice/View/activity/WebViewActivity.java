package com.hv.heartvoice.View.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hv.heartvoice.Base.BaseTitleActivity;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Util.Constant;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hv.heartvoice.Util.Constant.Transparent;

public class WebViewActivity extends BaseTitleActivity {

    /**
     * WebView控件
     */
    @BindView(R.id.wv)
    WebView wv;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.webText)
    TextView webText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
    }

    @Override
    protected void initViews() {
        super.initViews();

        //禁用ToolBar按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //设置状态栏透明并且字体黑色
        lightStatusBarAndBAR(Transparent);

        //设置内容到状态栏下
        setMargins(toolbar,0,getStatusBarHeight(getMainActivity()),0,0);

        //获取webview设置
        WebSettings webSettings = wv.getSettings();

        //运行访问文件
        webSettings.setAllowFileAccess(true);

        //支持多窗口
        webSettings.setSupportMultipleWindows(true);

        //开启js支持
        webSettings.setJavaScriptEnabled(true);

        //显示图片
        webSettings.setBlockNetworkImage(false);

        //运行显示手机中的网页
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        //运行文件访问
        webSettings.setAllowFileAccessFromFileURLs(true);

        //运行dmo存储
        webSettings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //运行混合类型
            //也就说支持网页中有http/https
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    @Override
    public void initData() {
        super.initData();
        //获取传递的数据
        String title = extraString(Constant.TITLE);
        String url = extraString(Constant.URL);

        //设置标题
        webText.setText(title);

        //加载网址
        wv.loadUrl(url);
    }

    /**
     * 定义静态的启动方法
     * 好处是用户只要看到声明
     * 就知道该界面需要哪些参数
     *
     * @param activity
     * @param title
     * @param url
     */
    public static void start(Activity activity, String title, String url) {
        //创建Intent
        Intent intent = new Intent(activity, WebViewActivity.class);

        //添加标题
        intent.putExtra(Constant.TITLE, title);

        //添加url
        intent.putExtra(Constant.URL, url);

        //启动界面
        activity.startActivity(intent);
    }

    @OnClick(R.id.back)
    public void back(){
        onBackPressed();
    }


}
