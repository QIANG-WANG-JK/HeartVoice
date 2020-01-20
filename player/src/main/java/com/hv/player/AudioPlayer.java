package com.hv.player;

import android.text.TextUtils;

import com.hv.player.Listener.OnCompleteListener;
import com.hv.player.Listener.OnErrorListener;
import com.hv.player.Listener.OnLoadListener;
import com.hv.player.Listener.OnNextListener;
import com.hv.player.Listener.OnPauseResumeListener;
import com.hv.player.Listener.OnPreparedListener;


public class AudioPlayer {

    /**
     * 静态初始化加载静态库
     */
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("avcodec-57");
        System.loadLibrary("avdevice-57");
        System.loadLibrary("avfilter-6");
        System.loadLibrary("avformat-57");
        System.loadLibrary("avutil-55");
        System.loadLibrary("postproc-54");
        System.loadLibrary("swresample-2");
        System.loadLibrary("swscale-4");
    }

    /**
     * 数据源
     */
    private static String source;

    /**
     * 是否播放下一首
     */
    private static boolean playNext=false;

    /**
     * 准备完成播放回调
     */
    private OnPreparedListener onPreparedListener;

    /**
     * 加载中回调
     */
    private OnLoadListener onLoadListener;

    /**
     * 播放或暂停回调
     */
    private OnPauseResumeListener onPauseResumeListener;

    /**
     * 时间监听回调
     */
    //private OnTimeListener onTimeListener;

    /**
     * 播放出错回调接口
     */
    private OnErrorListener onErrorListener;

    /**
     * 播放完成回调接口
     */
    private OnCompleteListener onCompleteListener;

    /**
     * 播放下一首
     */
    private OnNextListener onNextListener;

    /**
     * 时间基
     */
    //private static TimeBean timeBean;

    /**
     * 总时间
     */
    private static int duration;

    /**
     * 当前时间
     */
    private static int current;

    private boolean isPlaying;

    public AudioPlayer(){
        this.current = -1;
        this.duration = -1;
        isPlaying = false;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    public void setOnPauseResumeListener(OnPauseResumeListener onPauseResumeListener) {
        this.onPauseResumeListener = onPauseResumeListener;
    }

//    public void setOnTimeListener(OnTimeListener onTimeListener) {
//        this.onTimeListener = onTimeListener;
//    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public void setOnNextListener(OnNextListener onNextListener) {
        this.onNextListener = onNextListener;
    }

    /**
     * 准备资源
     */
    public void prepare(){
        if(TextUtils.isEmpty(source)){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                audio_preapre(getSource());
            }
        }).start();
    }

    /**
     * 将资源进行播放
     */
    public void play(){
        isPlaying = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                audio_play();
            }
        }).start();
    }

    /**
     * 暂停
     */
    public void pause(){
        isPlaying = false;
        audio_pause();
//        if(onPauseResumeListener!=null){
//            onPauseResumeListener.onPause(true);
//        }
    }

    /**
     * 播放
     */
    public void resume(){
        isPlaying = true;
        audio_resume();
//        if(onPauseResumeListener!=null){
//            onPauseResumeListener.onPause(false);
//        }
    }

    /**
     * 拖拽控制
     * @param secds
     */
    public void seek(int secds){
        native_seek(secds);
    }

    /**
     * 停止并回收资源
     */
    public void stop(){
        //timeBean=null;
        isPlaying = false;
        current = -1;
        duration = -1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                audio_stop();
            }
        }).start();
    }

    /**
     * FFmpeg解码器初始化成功
     */
    private void onCallPrepared(){
        if(onPreparedListener!=null){
            onPreparedListener.onPrepared();
        }
    }

    /**
     * 音乐是否在加载中回调
     * @param load
     */
    private void onCallLoad(boolean load){
        if(onLoadListener!=null){
            onLoadListener.onLoad(load);
        }
    }

    /**
     * 播放进度回调
     * @param currentTime
     * @param totalTime
     */
    public void onCallTime(int currentTime,int totalTime){
//        if(onTimeListener!=null){
//            if(timeBean==null){
//                timeBean=new TimeBean();
//            }
//            timeBean.setCurrentTime(currentTime);
//            timeBean.setTotalTime(totalTime);
//            onTimeListener.onTime(timeBean);
//        }
        setCurrent(currentTime);
    }

    /**
     * 播放出错native回调
     * @param code
     * @param msg
     */
    public void onCallError(int code, String msg){
        stop();
        if(onErrorListener!=null){
            onErrorListener.OnError(code,msg);
        }
    }

    /**
     * 播放完成native回调
     */
    public void onCallComplete(){
        stop();
        if(onCompleteListener!=null){
            onCompleteListener.onComplete();
        }
    }

    /**
     * native发出问候是否播放下一首
     */
    public void onCallNext(){
        if(onNextListener != null){
            onNextListener.onNext();
        }
    }

    @Deprecated
    public void playNextAudio(String url){
        source=url;
        playNext=true;
        stop();
    }

    /**
     * 获取当前时间
     * @return
     */
    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * 获取总时间
     * @return
     */
    public int getDuration(){
        if(duration<0){
            setDuration(native_duration());
        }
        return duration;
    }

    public boolean isPlaying(){
        return this.isPlaying;
    }

    private native void audio_preapre(String source);
    private native void audio_play();
    private native void audio_pause();
    private native void audio_resume();
    private native void audio_stop();
    private native void native_seek(int secds);
    private native int native_duration();

}
