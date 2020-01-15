package com.hv.heartvoice.Util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hv.heartvoice.R;

/**
 * 图片相关工具类
 */
public class ImageUtil {

    /**
     * 显示在线图片
     * @param activity
     * @param imageView
     * @param uri
     * @param select 0,1 0代表圆形
     */
    public static void showImage(Activity activity, ImageView imageView,String uri,int select){
        if (TextUtils.isEmpty(uri)){
            //没有图片,显示默认图片
            show(activity,imageView,select);
        }else{
            //有头像
            if(uri.startsWith("http")){
                //绝对路径 第三方登录时
                showFull(activity,imageView,uri,select);
            }else{
                //相对路径
                //自身服务器内的图片
                show(activity,imageView,uri,select);
            }
        }
    }

    /**
     *显示相对路径图片
     * @param activity
     * @param imageView
     * @param uri
     */
    public static void show(Activity activity, ImageView imageView, String uri,int select) {
        uri = ResourceUtil.resourceUri(uri);
        showFull(activity,imageView,uri,select);
    }

    /**
     * 显示绝对路径图片
     * @param activity
     * @param imageView
     * @param uri
     */
    public static void showFull(Activity activity, ImageView imageView, String uri,int select) {
            RequestOptions options;
            if(select == 0){
                options = getCircleRequestOptions();
            }else{
                options = getCommonRequestOptions();
            }
            Glide.with(activity).load(uri)
                    .apply(options)
                    .into(imageView);
    }

    /**
     * 显示资源目录图片
     * @param activity
     * @param imageView
     */
    public static void show(Activity activity, ImageView imageView,int select) {
        //获取公共配置
        RequestOptions options;
        if(select == 0){
            options = getCircleRequestOptions();
        }else{
            options = getCommonRequestOptions();
        }
        Glide.with(activity)
                .load(R.mipmap.place_holder)
                .apply(options)
                .into(imageView);
    }

    /**
     * 显示本地图片
     * @param context
     * @param id
     * @param imageView
     */
    public static void showLocal(Context context,Integer id,ImageView imageView){
        Glide.with(context).load(id).apply(getCommonRequestOptions()).into(imageView);
    }

    /**
     * 显示本地图片
     * @param context
     * @param id
     * @param imageView
     */
    public static void showLocalWithCicle(Context context,Integer id,ImageView imageView){
        Glide.with(context).load(id).apply(getCircleRequestOptions()).into(imageView);
    }

    /**
     * 获取Glide通用配置
     * @return
     */
    private static RequestOptions getCommonRequestOptions() {
        //创建配置选项
        RequestOptions options = new RequestOptions();
        //站位图
        options.placeholder(R.mipmap.place_holder);
        //出错后显示
        options.error(R.mipmap.place_holder);
        return options;
    }

    private static RequestOptions getCircleRequestOptions(){
        RequestOptions options = getCommonRequestOptions();
        options.circleCrop();//原型裁剪
        return options;
    }

}
