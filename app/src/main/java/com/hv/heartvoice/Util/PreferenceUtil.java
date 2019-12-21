package com.hv.heartvoice.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 偏好设置
 */
public class PreferenceUtil {

    private static final String NAME = "heartVoice";
    /**
     * 用户登录session key
     */
    private static final String SESSION = "SESSION";
    private static final String USER_ID = "USER_ID";

    private static PreferenceUtil instance;
    private final SharedPreferences preference;
    private Context context;

    public PreferenceUtil(Context context) {
        this.context = context.getApplicationContext();//保证内存不泄露
        preference = this.context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
    }

    public static PreferenceUtil getInstance(Context context) {
        if(instance == null){
            instance = new PreferenceUtil(context);//移动端并发量不大，不需要严格处理，性能消耗大
        }
        return instance;
    }

    public boolean isLogin(){
        //有session代表登录了
        return !TextUtils.isEmpty(getSession());
    }

    /**
     * 保存登录session
     * @param value
     */
    public void setSession(String value) {
        preference.edit().putString(SESSION,value).commit();
    }

    public String getSession(){
        return preference.getString(SESSION,null);
    }

    public void setUserId(String value) {
        preference.edit().putString(USER_ID,value).commit();
    }

    public String getUserId() {
        return preference.getString(USER_ID,null);
    }

    /**
     * 退出
     */
    public void logout(){
        delete(USER_ID);
        delete(SESSION);
    }

    private void delete(String key){
        preference.edit().remove(key).commit();
    }

}
