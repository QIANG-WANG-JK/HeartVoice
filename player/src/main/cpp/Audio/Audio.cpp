//
// Created by 王强 on 2019/9/1.
//
#include "Audio.h"

Audio::Audio(Status *status,int sample_rate,JavaListener *javaListener) {
    this->status=status;
    this->sample_rate=sample_rate;
    this->audioQueue=new SafeQueue(this->status);
    this->buffer= static_cast<u_int8_t *>(av_malloc(sample_rate * 2 * 2));//每次采样的数据的大小
    this->javaListener=javaListener;
}

Audio::~Audio() {

}

void *audioPlayCallBack(void *data){
    Audio *audio= static_cast<Audio *>(data);
    audio->initOPenSLES();
    pthread_exit(&audio->playAudioThread);
}

void playCallBack(SLAndroidSimpleBufferQueueItf bq,void *context) {
    Audio *audio= static_cast<Audio *>(context);
    if(audio!=NULL){
        int buffersize=audio->audioResample();
        if(buffersize>0){
            audio->clock+=buffersize/((double)(audio->sample_rate*2*2));
            if(audio->clock-audio->last_time>=0.1){//一秒回调十次
                audio->last_time=audio->clock;
                audio->javaListener->onCallTime(1,audio->clock,audio->duration);
            }
            (*audio->outputBufferQueueInterface)->Enqueue(audio->outputBufferQueueInterface,(char *) audio->buffer,buffersize);
        }
    }
}

void Audio::initOPenSLES(){
    SLresult result;
    result=slCreateEngine(&openSLengine, NULL, NULL, NULL, NULL, NULL);
    result=(*openSLengine)->Realize(openSLengine,SL_BOOLEAN_FALSE);
    result=(*openSLengine)->GetInterface(openSLengine,SL_IID_ENGINE,&slEngineItf);

    const SLInterfaceID mids[1]={SL_IID_ENVIRONMENTALREVERB};
    const SLboolean mreq[1]={SL_BOOLEAN_FALSE};//环境ID 混音器的效果  对声音等进行控制
    //使用第一步创建的slEngineItf 创建音频输出 output mix
    result=(*slEngineItf)->CreateOutputMix(slEngineItf,&outputMixObject,1,mids,mreq);
    (void)result;
    //初始化 outputmix
    result=(*outputMixObject)->Realize(outputMixObject, SL_BOOLEAN_FALSE);
    (void)result;
    result=(*outputMixObject)->GetInterface(outputMixObject, SL_IID_ENVIRONMENTALREVERB,&outputMixEnvironmentalReverb);
    if(SL_RESULT_SUCCESS==result){
        result=(*outputMixEnvironmentalReverb)->SetEnvironmentalReverbProperties(
                outputMixEnvironmentalReverb, &reverbSettings);
        (void)result;
    }

    SLDataLocator_OutputMix outputMix={SL_DATALOCATOR_OUTPUTMIX, outputMixObject};
    SLDataSink audioSnk = {&outputMix,0};

    SLDataLocator_AndroidSimpleBufferQueue outputLocato={SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, 2};
    //播放数据的格式，音频基础
    SLDataFormat_PCM format_pcm={SL_DATAFORMAT_PCM/*播放数据类型*/, 2/*声道*/, getSampleRate(this->sample_rate),/*播放数据类型 44100HZ*/
                                 SL_PCMSAMPLEFORMAT_FIXED_16,/*量化位数*/ SL_PCMSAMPLEFORMAT_FIXED_16,/*量化位数*/
                                 SL_SPEAKER_FRONT_LEFT|SL_SPEAKER_FRONT_RIGHT/*双声道 前左前右*/ ,SL_BYTEORDER_LITTLEENDIAN};
    SLDataSource slDataSource = {&outputLocato, &format_pcm};//PCM数据

    const SLInterfaceID  outputInterfaces[2]={SL_IID_BUFFERQUEUE,SL_IID_VOLUME};//SL_IID_VOLUME音量控制
    const SLboolean  requireds[2]={SL_BOOLEAN_TRUE,SL_BOOLEAN_TRUE};

    (*slEngineItf)->CreateAudioPlayer(slEngineItf,&slPlayer,&slDataSource,&audioSnk,1,outputInterfaces,requireds);//实现队列控制等
    (*slPlayer)->Realize(slPlayer,SL_BOOLEAN_FALSE);
    (*slPlayer)->GetInterface(slPlayer,SL_IID_PLAY,&outputPlayInterface);


    //音量
    //(*slPlayer)->GetInterface(slPlayer,SL_IID_VOLUME,&outVolumeInterface);

    //播放
    (*slPlayer)->GetInterface(slPlayer,SL_IID_BUFFERQUEUE,&outputBufferQueueInterface);
    (*outputBufferQueueInterface)->RegisterCallback(outputBufferQueueInterface,playCallBack,this);
    (*outputPlayInterface)->SetPlayState(outputPlayInterface,SL_PLAYSTATE_PLAYING);//设置播放状态
    playCallBack(outputBufferQueueInterface,this);//回调
}

int Audio::audioResample() {
    while(status!=NULL &&!status->exit){
        if(status->seek){
            av_usleep(1000 * 100);
            continue;
        }
        if(audioQueue->getQueueSize()==0){
            //无帧 如果还在不在加载，就把状态设置为加载中
            if(!status&&!status->load){
                status->load=true;
                javaListener->onCallLoad(1,true);
            }
            av_usleep(1000 * 100);
            continue;//size为0不必向下执行
        }else{
            if(!status&&status->load){
                status->load=false;
                javaListener->onCallLoad(1, false);
            }
        }
        avPacket=av_packet_alloc();
        if (audioQueue->popAVPacket(avPacket)!=0){
            av_packet_free(&avPacket);
            av_free(avPacket);
            avPacket = NULL;
            continue;
        }
        result=avcodec_send_packet(avCodecContext,avPacket);
        if(result!=0){
            av_packet_free(&avPacket);
            av_free(avPacket);
            avPacket=NULL;
            continue;
        }
        avFrame=av_frame_alloc();
        result=avcodec_receive_frame(avCodecContext,avFrame);
        if(result==0){
            if(avFrame->channels>0&&avFrame->channel_layout==0){
                avFrame->channel_layout=av_get_default_channel_layout(avFrame->channels);//根据声道数返回声道布局
            } else if(avFrame->channels==0&&avFrame->channel_layout>0){
                avFrame->channels=av_get_channel_layout_nb_channels(avFrame->channel_layout);
            }
            //AV_CH_LAYOUT_STEREO 立体声声道布局
            SwrContext *swrContext=swr_alloc_set_opts(NULL,
                    AV_CH_LAYOUT_STEREO, AV_SAMPLE_FMT_S16,
                    avFrame->sample_rate,avFrame->channel_layout,
                    static_cast<AVSampleFormat>(avFrame->format),
                    avFrame->sample_rate,NULL,NULL);
            if(!swrContext||swr_init(swrContext)<0){
                av_packet_free(&avPacket);
                av_free(avPacket);
                avPacket=NULL;
                av_frame_free(&avFrame);
                av_free(avFrame);
                avFrame=NULL;
                swr_free(&swrContext);
                continue;
            }
            //nb是采样个数 nb_samples  每次采样多少  采样了多少个
            int nb=swr_convert(swrContext,&buffer,avFrame->nb_samples,
                               (const uint8_t **)avFrame->data,
                               avFrame->nb_samples);
            int out_channels=av_get_channel_layout_nb_channels(AV_CH_LAYOUT_STEREO);//拿到输出的立体声的布局 原布局不一定是立体声
            dataSize=nb*out_channels*av_get_bytes_per_sample(AV_SAMPLE_FMT_S16);//av_get_bytes_per_sample(AV_SAMPLE_FMT_S16)求16位字节数
            now_time=avFrame->pts*av_q2d(time_base);//多少帧*每一帧的时间，当前时间 如果pts为0 不管怎样都为0
            if(now_time<clock){
                now_time=clock;//如果pts为0 将nowtime设为上一帧时间
            }
            clock=now_time;
            av_packet_free(&avPacket);
            av_free(avPacket);
            avPacket=NULL;
            av_frame_free(&avFrame);
            av_free(avFrame);
            avFrame=NULL;
            swr_free(&swrContext);
            break;
        }else{
            av_packet_free(&avPacket);
            av_free(avPacket);
            avPacket = NULL;
            av_frame_free(&avFrame);
            av_free(avFrame);
            avFrame = NULL;
            continue;
        }
    }
    return dataSize;
}

void Audio::audioPlay(){
    pthread_create(&playAudioThread,NULL,audioPlayCallBack,this);
}

int Audio::getSampleRate(int sample_rate) {
    int rate = 0;
    switch (sample_rate){
        case 8000:
            rate = SL_SAMPLINGRATE_8;
            break;
        case 11025:
            rate = SL_SAMPLINGRATE_11_025;
            break;
        case 12000:
            rate = SL_SAMPLINGRATE_12;
            break;
        case 16000:
            rate = SL_SAMPLINGRATE_16;
            break;
        case 22050:
            rate = SL_SAMPLINGRATE_22_05;
            break;
        case 24000:
            rate = SL_SAMPLINGRATE_24;
            break;
        case 32000:
            rate = SL_SAMPLINGRATE_32;
            break;
        case 44100:
            rate = SL_SAMPLINGRATE_44_1;
            break;
        case 48000:
            rate = SL_SAMPLINGRATE_48;
            break;
        case 64000:
            rate = SL_SAMPLINGRATE_64;
            break;
        case 88200:
            rate = SL_SAMPLINGRATE_88_2;
            break;
        case 96000:
            rate = SL_SAMPLINGRATE_96;
            break;
        case 192000:
            rate = SL_SAMPLINGRATE_192;
            break;
        default:
            rate =  SL_SAMPLINGRATE_44_1;
    }
    return rate;
}

void Audio::pause() {
    if(outputPlayInterface!=NULL){
        (*outputPlayInterface)->SetPlayState(outputPlayInterface,SL_PLAYSTATE_PAUSED);
    }
}

void Audio::resume() {
    if(outputPlayInterface!=NULL){
        (*outputPlayInterface)->SetPlayState(outputPlayInterface,SL_PLAYSTATE_PLAYING);
    }//析构设停止
}

void Audio::stop() {
    if(outputPlayInterface!=NULL){
        (*outputPlayInterface)->SetPlayState(outputPlayInterface,SL_PLAYSTATE_STOPPED);
    }
}

void Audio::release() {
    stop();
    if(audioQueue!=NULL){
        delete(audioQueue);
        audioQueue=NULL;
    }
    if(slPlayer!=NULL){
        (*slPlayer)->Destroy(slPlayer);
        slPlayer=NULL;
        outputPlayInterface=NULL;
        outputBufferQueueInterface=NULL;
    }
    if(outputMixObject!=NULL){
        (*outputMixObject)->Destroy(outputMixObject);
        outputMixObject=NULL;
        outputMixEnvironmentalReverb=NULL;
    }
    if(openSLengine!=NULL){
        (*openSLengine)->Destroy(openSLengine);
        openSLengine=NULL;
        slEngineItf=NULL;
    }
    if(buffer!=NULL){
        free(buffer);
        buffer=NULL;
    }
    if(avCodecContext!=NULL){
        avcodec_close(avCodecContext);
        avcodec_free_context(&avCodecContext);
        avCodecContext=NULL;
    }
    if(status!=NULL){
        status=NULL;
    }
    if(javaListener!=NULL){
        javaListener=NULL;
    }
}
