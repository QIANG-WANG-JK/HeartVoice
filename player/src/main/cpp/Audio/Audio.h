//
// Created by 王强 on 2019/9/1.
//

#ifndef AUDIOPLAYER_AUDIO_H
#define AUDIOPLAYER_AUDIO_H

#include "../Queue/SafeQueue.h"
#include "../Status/Status.h"
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <pthread.h>
#include "../CallJava/JavaListener.h"

extern "C"{
#include <libavcodec/avcodec.h>
#include <libswresample/swresample.h>
#include <libavutil/time.h>
};

class Audio {

public:
    int streamIndex=-1;//流的索引

    pthread_t playAudioThread;//线程参数

    AVCodecParameters* codecpar=NULL;
    AVCodecContext *avCodecContext=NULL;
    SafeQueue *audioQueue=NULL;
    Status *status=NULL;

    SLObjectItf openSLengine=NULL;//祖先引擎
    SLEngineItf slEngineItf=NULL;//引擎接口对象

    //混音器
    SLObjectItf outputMixObject=NULL;//混音器对象
    SLEnvironmentalReverbSettings reverbSettings=SL_I3DL2_ENVIRONMENT_PRESET_STONECORRIDOR;
    SLEnvironmentalReverbItf outputMixEnvironmentalReverb = NULL;

    //播放器
    SLObjectItf slPlayer=NULL;//播放器对象
    SLPlayItf outputPlayInterface;// 获取播放器接口

    //缓冲器队列接口
    SLAndroidSimpleBufferQueueItf outputBufferQueueInterface=NULL;//音频输出的BufferQueue

    //SLVolumeItf outVolumeInterface=NULL;
    //CallJava
    JavaListener *javaListener=NULL;

    AVPacket *avPacket=NULL;
    AVFrame *avFrame=NULL;
    u_int8_t *buffer=NULL;
    int dataSize=0;
    int sample_rate=0;
    int result=0;

    int duration=0;//总时长
    AVRational time_base;//时间基
    double now_time=0;//当前时间
    double clock=0;//记录当前时间
    double last_time=0;//因为1秒可以回调很多次,设个变量让其一秒回调一次

public:
    Audio(Status *status,int sample_rate,JavaListener *javaListener);
    ~Audio();
    void initOPenSLES();
    void audioPlay();
    int audioResample();//返回重采样的大小
    int getSampleRate(int sample_rate);
    void pause();
    void resume();
    void stop();
    void release();
};


#endif //AUDIOPLAYER_AUDIO_H
