package com.hv.heartvoice.Util;

import android.graphics.Color;

import com.hv.heartvoice.BuildConfig;

public class Constant {

    /**
     * 端点
     */
    public static final String ENDPOINT = BuildConfig.ENDPOINT;

    /**
     * 资源端点
     */
    public static final String RESOURCE_ENDPOINT = BuildConfig.RESOURCE_ENDPOINT;

    /**
     * 状态栏透明
     */
    public static final int Transparent = Color.TRANSPARENT;

    /**
     * 状态栏灰色
     */
    public static final int serviceApproveActivityColor = 0xFFF5F5F5;

    /**
     * 用户详情昵称查询字段
     */
    public static final String NICKNAME = "nickname";

    /**
     * 标题
     */
    public static final int TYPE_TITLE = 0;

    /**
     * 歌单
     */
    public static final int TYPE_SHEET = 1;

    /**
     * 单曲
     */
    public static final int TYPE_SONG = 2;

    /**
     * 传递url key
     */
    public static final String URL = "URL";

    /**
     * 传递标题 key
     */
    public static final String TITLE = "TITLE";

    /**
     * Id常量
     */
    public static final String ID = "ID";

    /**
     * 网络缓存目录大小
     * 100M
     */
    public static final long NETWORK_CACHE_SIZE = 1024 * 1024 * 100;

    /**
     * 歌单ID
     */
    public static final String SHEET_ID = "SHEET_ID";


}
