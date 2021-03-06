package com.hv.heartvoice.Manager;

import android.content.Context;

import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Service.MusicPlayerService;
import com.hv.heartvoice.Util.PreferenceUtil;
import com.hv.heartvoice.Util.ResourceUtil;
import com.hv.player.AudioPlayer;
import com.hv.player.Listener.OnNextListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.hv.heartvoice.Util.Constant.MODEL_LOOP_LIST;
import static com.hv.heartvoice.Util.Constant.MODEL_LOOP_RANDOM;
import static com.hv.heartvoice.Util.Constant.PLAY_MODEL;

/**
 * 列表管理器默认实现
 */
public class ListManagerImpl implements ListManager{

    private static ListManagerImpl instance;

    private final Context context;

    /**
     * 列表
     */
    private List<Song> datas = new LinkedList<>();

    /**
     * 当前音乐对象
     */
    private Song data;

    /**
     * 音乐播放管理器
     */
    private MusicPlayerManager musicPlayerManager;

    /**
     * 是否播放了
     */
    private boolean isPlay;

    /**
     * 循环模式 默认列表循环
     */
    private int model = MODEL_LOOP_LIST;

    /**
     * 缓存工具
     */
    private PreferenceUtil sp;

    private AudioPlayer audioPlayer;

    private ListManagerImpl(Context context) {
        this.context = context.getApplicationContext();
        sp = PreferenceUtil.getInstance(this.context);
        model = Integer.parseInt(sp.getPlayModel(PLAY_MODEL));
        //初始化音乐播放管理器
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(this.context);

        audioPlayer = musicPlayerManager.getAudioPlayer();
    }

    public static synchronized ListManagerImpl getInstance(Context context){
        if(instance == null){
            instance = new ListManagerImpl(context);
        }
        return instance;
    }

    @Override
    public void setDatas(List<Song> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
    }

    @Override
    public List<Song> getDatas() {
        return datas;
    }

    @Override
    public void play(Song data) {
        isPlay = true;
        this.data = data;
        //播放音乐

        musicPlayerManager.play(ResourceUtil.resourceUri(data.getUri()),data);
    }

    @Override
    public void pause() {
        musicPlayerManager.pause();
    }

    @Override
    public void resume() {
        if(isPlay){
            musicPlayerManager.resume();
        }else{
            play(data);
        }
    }

    @Override
    public Song next() {
        if(datas.size() == 0){
            return null;
        }
        int index = 0;
        //判断循环模式
        switch (model){
            case MODEL_LOOP_RANDOM:
                index = new Random().nextInt(datas.size());
                break;
            default:
                //找到当前音乐索引
                index = datas.indexOf(data);
                if(index != -1){
                    if(index == datas.size() - 1){
                        index = 0;
                    }else{
                        index++;
                    }
                }else{
                    throw new IllegalArgumentException("can not find song");
                }
                break;
        }
        //获取下一首音乐
        return datas.get(index);
    }

    @Override
    public Song previous(){
        if(datas.size() == 0){
            return null;
        }
        int index = 0;
        //判断循环模式
        switch (model){
            case MODEL_LOOP_RANDOM:
                index = new Random().nextInt(datas.size());
                break;
            default:
                //找到当前音乐索引
                index = datas.indexOf(data);
                if(index != -1){
                    if(index == 0){
                        index = datas.size() - 1;
                    }else{
                        index--;
                    }
                }else{
                    throw new IllegalArgumentException("can not find song");
                }
                break;
        }
        //获取下一首音乐
        return datas.get(index);
    }

    @Override
    public int changeLoopModel() {
        sp.delete(PLAY_MODEL);
        model++;
        //判断循环模式
        if(model > MODEL_LOOP_RANDOM){
            model = MODEL_LOOP_LIST;
        }
        sp.setPlayModel(String.valueOf(model));
        return model;
    }

    @Override
    public int getLoopModel() {
        return model;
    }

    @Override
    public Song getData() {
        return data;
    }

    @Override
    public void delete(int position) {

        Song song = datas.get(position);

        if(song.getId().equals(data.getId())){
            Song next = next();
            //判断循环模式
            if(next.getId().equals(data.getId())){
                data = null;
            }else{
                stop();
                audioPlayer.setOnNextListener(new OnNextListener() {
                    @Override
                    public void onNext() {
                        play(next);
                    }
                });
            }
        }
        datas.remove(song);
    }

    @Override
    public void deleteAll() {
        //如果在播放音乐就暂停
        if(musicPlayerManager.isPlaying()){
            stop_nocall();
        }

        datas.clear();
    }

    @Override
    public void stop() {
        musicPlayerManager.stop();
    }

    @Override
    public void stop_nocall() {
        musicPlayerManager.stop_nocall();
    }

}
