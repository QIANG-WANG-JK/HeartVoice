package com.hv.heartvoice.View.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.hv.heartvoice.Adapter.MainAdapter;
import com.hv.heartvoice.Base.BaseMusicPlayerActivity;
import com.hv.heartvoice.Base.BaseTitleActivity;
import com.hv.heartvoice.Domain.User;
import com.hv.heartvoice.Domain.event.CloseEvent;
import com.hv.heartvoice.Model.Api;
import com.hv.heartvoice.Model.myObserver.HttpObserver;
import com.hv.heartvoice.Model.response.DetailResponse;
import com.hv.heartvoice.R;
import com.hv.heartvoice.Util.ImageUtil;
import com.hv.heartvoice.Util.ToastUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hv.heartvoice.Util.Constant.Transparent;

public class MainActivity extends BaseMusicPlayerActivity {

    public static final String TAG = "MainActivity";

    /**
     * 主界面适配器
     */
    private MainAdapter adapter;

    /**
     * viewPager数据
     */
    private ArrayList<Integer> datas;

    /**
     * 侧滑控件
     */
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    /**
     * 关闭侧滑图片
     */
    @BindView(R.id.mainClose)
    ImageView mainClose;

    /**
     * 侧滑头像
     */
    @BindView(R.id.userHead)
    ImageView userHead;

    /**
     * 昵称
     */
    @BindView(R.id.mainNickname)
    TextView mainNickname;

    /**
     * 描述
     */
    @BindView(R.id.mainDescription)
    TextView mainDescription;

    /**
     * 主界面头像
     */
    @BindView(R.id.userHeadToolBar)
    ImageView userHeadToolBar;

    /**
     * 我的
     */
    @BindView(R.id.mainMy)
    LinearLayout mainMy;

    /**
     * 我的朋友
     */
    @BindView(R.id.mainMyFriends)
    LinearLayout mainMyFriends;

    /**
     * 我的消息
     */
    @BindView(R.id.mainMessage)
    LinearLayout mainMessage;

    /**
     * 歌曲识别
     */
    @BindView(R.id.mainIdentify)
    LinearLayout mainIdentify;

    /**
     * 扫一扫
     */
    @BindView(R.id.mainScan)
    LinearLayout mainScan;

    /**
     * 设置停止时间
     */
    @BindView(R.id.mainTimingStop)
    LinearLayout mainTimingStop;

    /**
     * 背景更换
     */
    @BindView(R.id.mainChangeBac)
    LinearLayout mainChangeBac;

    /**
     * 关于我们
     */
    @BindView(R.id.mainAboutUs)
    LinearLayout mainAboutUs;

    /**
     * 设置
     */
    @BindView(R.id.mainSetting)
    RelativeLayout mainSetting;

    /**
     * 登出APP
     */
    @BindView(R.id.mainLogOutApp)
    RelativeLayout mainLogOutApp;

    /**
     * 滚动视图
     */
    @BindView(R.id.mainViewPager)
    ViewPager mainViewPager;//看一下懒加载

    /**
     * 指示器
     */
    @BindView(R.id.Indicator)
    MagicIndicator Indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initViews() {
        super.initViews();

        //注册EventBus
        EventBus.getDefault().register(this);

        //透明状态栏,黑色字体
        lightStatusBarAndBAR(Transparent);

        //禁用ToolBar按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //调整控件到状态栏以下
        setMargins(toolbar,0,getStatusBarHeight(getMainActivity()),0,0);
        setMargins(userHead,0,getStatusBarHeight(getMainActivity()),0,0);
        setMargins(mainNickname,0,getStatusBarHeight(getMainActivity()),0,0);
        setMargins(mainDescription,0,getStatusBarHeight(getMainActivity()),0,0);

        //就算无网络也可以加载出站位图
        ImageUtil.showImage(getMainActivity(),userHead,"",0);
        ImageUtil.showImage(getMainActivity(),userHeadToolBar,"",0);

        //缓存页面数量
        //默认是缓存一个
        mainViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void initData() {
        super.initData();
        fetchData();
        //创建adapter
        adapter = new MainAdapter(getSupportFragmentManager(), getMainActivity());
        mainViewPager.setAdapter(adapter);
        datas = new ArrayList<>();
        datas.add(0);
        datas.add(1);
        datas.add(2);
        adapter.setData(datas);

        //将指示器和ViewPager关联
        CommonNavigator commonNavigator = new CommonNavigator(getMainActivity());

        //设置适配器
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            /**
             * 指示器数量
             * @return
             */
            @Override
            public int getCount() {
                return datas.size();
            }

            /**
             * 返回当前位置的标题
             * @param context
             * @param index
             * @return
             */
            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                //创建简单的文本控件
                SimplePagerTitleView titleView = new SimplePagerTitleView(context);

                //默认颜色
                titleView.setNormalColor(getResources().getColor(R.color.light_grey,null));

                //选中后的颜色
                titleView.setSelectedColor(getResources().getColor(R.color.red,null));

                //显示的文本
                titleView.setText(adapter.getPageTitle(index));

                //设置回调监听
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //让viewPager跳转到当前的位置
                        mainViewPager.setCurrentItem(index);
                    }
                });

                return titleView;
            }

            /**
             * 获取下面的白线
             * @param context
             * @return
             */
            @Override
            public IPagerIndicator getIndicator(Context context) {
                //创建一条线
                LinePagerIndicator indicator = new LinePagerIndicator(context);

                //线的内容和宽度一样 MODE_WRAP_CONTENT内容有多宽就有多宽
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);

                //高亮颜色
                indicator.setColors(getResources().getColor(R.color.red,null));

                return indicator;
            }
        });

        commonNavigator.setAdjustMode(true);

        //设置到导航器
        Indicator.setNavigator(commonNavigator);

        //让指示器和ViewPager配合工作
        ViewPagerHelper.bind(Indicator,mainViewPager);

        //默认选中第二个界面
        mainViewPager.setCurrentItem(1);

    }

    private void fetchData() {
        Api.getInstance().userDeatil(sp.getUserId())
                .subscribe(new HttpObserver<DetailResponse<User>>(getMainActivity(),false) {
                    @Override
                    public void onSucceeded(DetailResponse<User> data) {
                        next(data.getData());
                    }
                });
    }

    /**
     * 显示信息
     * @param data
     */
    private void next(User data){
        ImageUtil.showImage(getMainActivity(),userHead,data.getAvatar(),0);
        ImageUtil.showImage(getMainActivity(),userHeadToolBar,data.getAvatar(),0);
        mainNickname.setText(data.getNickname());
        mainDescription.setText(data.getDescriptionFormat());
    }

    @OnClick(R.id.userHeadToolBar)
    public void open(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @OnClick(R.id.mainClose)
    public void close(){
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @OnClick(R.id.mainMy)
    public void goMy(){
        ToastUtil.successShort(R.string.my);
    }

    @OnClick(R.id.mainMyFriends)
    public void goMyFriends(){
        ToastUtil.successShort(R.string.myFriends);
    }

    @OnClick(R.id.mainMessage)
    public void goMyMessage(){
        ToastUtil.successShort(R.string.myMessage);
    }

    @OnClick(R.id.mainIdentify)
    public void goIdentify(){
        ToastUtil.successShort(R.string.IdentifySong);
    }

    @OnClick(R.id.mainScan)
    public void goScan(){
        ToastUtil.successShort(R.string.scanIt);
    }

    @OnClick(R.id.mainTimingStop)
    public void goTimingStop(){
        ToastUtil.successShort(R.string.timingStop);
    }

    @OnClick(R.id.mainChangeBac)
    public void goChangeBac(){
        ToastUtil.successShort(R.string.changeBack);
    }

    @OnClick(R.id.mainAboutUs)
    public void goAboutUs(){
        ToastUtil.successShort(R.string.AboutUs);
    }

    @OnClick(R.id.mainSetting)
    public void goSetting(){
        drawerLayout.closeDrawer(GravityCompat.START);
        startActivity(SettingActivity.class);
    }

    @OnClick(R.id.mainLogOutApp)
    public void goLogOutApp(){
        ToastUtil.successShort(R.string.LogOutApp);
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
