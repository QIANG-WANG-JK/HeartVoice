package com.hv.heartvoice.Manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Listener.Consume;
import com.hv.heartvoice.Listener.MusicPlayerListener;
import com.hv.heartvoice.Util.ListUtil;
import com.hv.heartvoice.Util.LogUtil;
import com.hv.heartvoice.Util.ToastUtil;
import com.hv.player.AudioPlayer;
import com.hv.player.Listener.OnCompleteListener;
import com.hv.player.Listener.OnPreparedListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.hv.heartvoice.Util.Constant.DEFAULT_TIME;
import static com.hv.heartvoice.Util.Constant.MAIN_THREAD_FINSH;
import static com.hv.heartvoice.Util.Constant.MAIN_THREAD_TRANSCATION;
import static com.hv.heartvoice.Util.Constant.MESSAGE_PROGRESS;

/**
 * 实现类
 */
public class MusicPlayerManagerImpl implements MusicPlayerManager {

    private static MusicPlayerManagerImpl manager;

    private final Context context;

    /**
     * 音乐播放器对象
     */
//    private final MediaPlayer mediaPlayer;
    private final AudioPlayer audioPlayer;

    /**
     * 当前播放的音乐对象
     */
    private Song data;

    /**
     * 播放器状态监听器
     */
    private List<MusicPlayerListener> listeners = new ArrayList<>();

    /**
     * 定时器
     */
    private TimerTask timerTask;

    private Timer timer;

    private boolean firstPlayer;

    private InterHandler handler = new InterHandler(this);

    private MusicPlayerManagerImpl(Context context){
        this.context = context.getApplicationContext();

        //初始化播放器
        //mediaPlayer = new MediaPlayer();
        audioPlayer = new AudioPlayer();

        firstPlayer = true;

        //设置播放器监听
        initListeners();
    }

    private void initListeners() {
//        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//
//            /**
//             * 播放器准备开始播放时
//             * @param mp
//             */
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                //播放准备时回调
//                //将总进度保存至音乐对象
//                data.setDuration(mp.getDuration());
//
//                ListUtil.eachListener(listeners, new Consume<MusicPlayerListener>() {
//                    @Override
//                    public void accept(MusicPlayerListener listener) {
//                        listener.onPrepared(mp,data);
//                    }
//                });
//
//            }
//        });

        audioPlayer.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                //准备完成播放
                audioPlayer.play();

                //设置总进度
                data.setDuration(audioPlayer.getDuration());

                /**
                 * 播放状态定时器
                 */
                startPublishProgress();
                //子线程
                /**
                 * 通知播放状态
                 */
                handler.obtainMessage(MAIN_THREAD_TRANSCATION).sendToTarget();
            }
        });

//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//            /**
//             * 播放完毕回调
//             * @param mp
//             */
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                ListUtil.eachListener(listeners, new Consume<MusicPlayerListener>() {
//                    @Override
//                    public void accept(MusicPlayerListener listener) {
//                        listener.onCompletion(mp);
//                    }
//                });
//            }
//        });

        /**
         * 播放完成监听
         */
        audioPlayer.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete() {
                handler.obtainMessage(MAIN_THREAD_FINSH).sendToTarget();
            }
        });

    }

    public static synchronized MusicPlayerManager getInstance(Context context){
        if(manager == null){
            manager = new MusicPlayerManagerImpl(context);
        }

        return manager;
    }

    @Override
    public void play(String uri, Song data) {
        //保存音乐对象
        this.data = data;
        //释放播放器
        //mediaPlayer.reset();
        //audioPlayer.stop();

        //设置数据源
        //mediaPlayer.setDataSource(uri);
        audioPlayer.setSource(uri);

        //准备
        //mediaPlayer.prepare();
        audioPlayer.prepare();

        firstPlayer = false;

        //开始播放
        //mediaPlayer.start();
    }

    @Override
    public boolean isPlaying() {
        return audioPlayer.isPlaying();
    }

    @Override
    public void pause() {
        if(isPlaying()){
            audioPlayer.pause();
            ListUtil.eachListener(listeners, new Consume<MusicPlayerListener>() {
                @Override
                public void accept(MusicPlayerListener listener) {
                    listener.onPaused(data);
                }
            });
            //停止进度
            stopPublishProgress();
        }
    }

    @Override
    public void resume() {
        if(!isPlaying()){
            audioPlayer.resume();
            //回调监听器
            publishPlayingStatus();
            //启动进度通知
            startPublishProgress();
        }
    }

    @Override
    public void addMusicPlayerListener(MusicPlayerListener listener) {
        if(!listeners.contains(listener)){
            listeners.add(listener);
        }
        //启动进度通知
        startPublishProgress();
    }

    @Override
    public void removeMusicPlayerListener(MusicPlayerListener listener) {
        listeners.remove(listener);
    }

    @Override
    public Song getData() {
        return this.data;
    }

    @Override
    public void seekTo(int progress) {
        audioPlayer.seek(progress);
    }

    @Override
    public boolean getFistPlayer() {
        return firstPlayer;
    }

    @Override
    public AudioPlayer getAudioPlayer() {
        return this.audioPlayer;
    }

    @Override
    public void stop() {
        audioPlayer.stop();
    }

    @Override
    public void stop_nocall() {
        audioPlayer.stop_complete();
    }

//    @Override
//    public void setLooping(boolean looping) {
//        //mediaPlayer.setLooping(looping);
//    }

    /**
     * 发布播放中状态
     */
    private void publishPlayingStatus(){
        ListUtil.eachListener(listeners, new Consume<MusicPlayerListener>() {
            @Override
            public void accept(MusicPlayerListener listener) {
                listener.onPlaying(data);
            }
        });
    }

    /**
     * 启动播放进度通知
     */
    private void startPublishProgress(){
        if (listeners.size() == 0){
            return;
        }

        if(!isPlaying()){
            //没有播放音乐就不启动
            return;
        }

        if(timerTask != null){
            //已经启动
            return;
        }
        //创建一个任务
        timerTask = new TimerTask() {

            @Override
            public void run() {
                //如果没有监听器了就停止定时器
                if(listeners.size() == 0){
                    stopPublishProgress();
                    return;
                }
                handler.obtainMessage(MESSAGE_PROGRESS).sendToTarget();
            }
        };

        //创建一个定时器
        timer = new Timer();

        //启动一个持续的任务
        timer.schedule(timerTask,0,DEFAULT_TIME);
    }

    /**
     * 停止播放进度通知
     */
    private void stopPublishProgress() {
        if(timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }

        if (timer !=null){
            timer.cancel();
            timer = null;
        }
    }

    private static final class InterHandler extends Handler{
        private WeakReference<MusicPlayerManagerImpl> managerHandler;
        public InterHandler(MusicPlayerManagerImpl managerHandler){
            this.managerHandler = new WeakReference<>(managerHandler);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Song data = managerHandler.get().getData();
            AudioPlayer audioPlayer = managerHandler.get().audioPlayer;
            List<MusicPlayerListener> listeners = managerHandler.get().listeners;
            if(data != null){
                switch (msg.what){
                    case MESSAGE_PROGRESS:
                        data.setProgress(audioPlayer.getCurrent());
                        //回调监听
                        ListUtil.eachListener(listeners, new Consume<MusicPlayerListener>() {
                            @Override
                            public void accept(MusicPlayerListener listener) {
                                listener.onProgress(data);
                            }
                        });
                        break;
                    case MAIN_THREAD_TRANSCATION:
                        ListUtil.eachListener(listeners, new Consume<MusicPlayerListener>() {
                            @Override
                            public void accept(MusicPlayerListener listener) {
                                listener.onPrepared(audioPlayer,data);
                            }
                        });
                        //回调监听器
                        managerHandler.get().publishPlayingStatus();
                        break;
                    case MAIN_THREAD_FINSH:
                        ListUtil.eachListener(listeners, new Consume<MusicPlayerListener>() {
                            @Override
                            public void accept(MusicPlayerListener listener) {
                                listener.onCompletion(audioPlayer);
                            }
                        });
                        break;
                }
            }
        }
    }

}
