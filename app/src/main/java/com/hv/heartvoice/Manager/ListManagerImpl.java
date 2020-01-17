package com.hv.heartvoice.Manager;

import android.content.Context;

import com.hv.heartvoice.Domain.Song;
import com.hv.heartvoice.Service.MusicPlayerService;
import com.hv.heartvoice.Util.ResourceUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.hv.heartvoice.Util.Constant.MODEL_LOOP_LIST;
import static com.hv.heartvoice.Util.Constant.MODEL_LOOP_ONE;
import static com.hv.heartvoice.Util.Constant.MODEL_LOOP_RANDOM;

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

    private ListManagerImpl(Context context) {
        this.context = context.getApplicationContext();

        //初始化音乐播放管理器
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(this.context);
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
        model++;
        //判断循环模式
        if(model > MODEL_LOOP_RANDOM){
            model = MODEL_LOOP_LIST;
        }
        if(model == MODEL_LOOP_ONE){
            //设置单曲循环模式
            musicPlayerManager.setLooping(true);
        }else{
            musicPlayerManager.setLooping(false);
        }
        return model;
    }

    @Override
    public int getLoopModel() {
        return model;
    }

}
