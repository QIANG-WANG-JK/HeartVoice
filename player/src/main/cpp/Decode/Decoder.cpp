//
// Created by 王强 on 2019/9/1.
//

#include "Decoder.h"

Decoder::Decoder(JavaListener *javaListener, const char *url,Status *status) {
    this->javaListener=javaListener;
    this->url=url;
    this->status=status;
    pthread_mutex_init(&init,NULL);
    pthread_mutex_init(&seek_mutex,NULL);
}

Decoder::~Decoder() {
    pthread_mutex_destroy(&init);
    pthread_mutex_destroy(&seek_mutex);
}

void *decode(void *data){
    Decoder *decoder= static_cast<Decoder *>(data);
    decoder->beginDecode();
    pthread_exit(&decoder->decodeThread);
}

void Decoder::prepareDecode() {
    pthread_create(&decodeThread,NULL,decode,this);
}

int avFormatCallback(void *context){
    Decoder *decoder= static_cast<Decoder *>(context);
    if(decoder->status->exit){
        return AVERROR_EOF;
    }
    return 0;
}

void Decoder::beginDecode() {
    pthread_mutex_lock(&init);
    int result=-1;
    av_register_all();
    avformat_network_init();
    formatContext=avformat_alloc_context();
    formatContext->interrupt_callback.callback=avFormatCallback;
    formatContext->interrupt_callback.opaque=this;
    result=avformat_open_input(&formatContext,url,NULL,NULL);
    if(result!=0){
        __android_log_print(ANDROID_LOG_ERROR,"打开上下文失败","%d",result);
        javaListener->onCallError(1,1001,"不能打开上下文");
        decode_exit=true;
        pthread_mutex_unlock(&init);
        return;
    }
    result=avformat_find_stream_info(formatContext,NULL);
    if(result<0){
        __android_log_print(ANDROID_LOG_ERROR,"没有找到流","%d",result);
        javaListener->onCallError(1,1002,"没有找到流");
        decode_exit=true;
        pthread_mutex_unlock(&init);
        return;
    }
    for(int i=0;i<formatContext->nb_streams;i++){
        if(formatContext->streams[i]->codecpar->codec_type==AVMEDIA_TYPE_AUDIO){
            //音频流
            if(audio==NULL){
                audio=new Audio(this->status,formatContext->streams[i]->codecpar->sample_rate,this->javaListener);
                audio->streamIndex=i;//在第几个i时拿到了Audio数据，记录下面好找nb_stream要
                audio->codecpar=formatContext->streams[i]->codecpar;
                audio->duration=formatContext->duration/AV_TIME_BASE;//得到总时长
                audio->time_base=formatContext->streams[i]->time_base;//时间基 代表流的每一帧的分数表达式
                duration=audio->duration;
            }
        }
    }
    AVCodec *codec=avcodec_find_decoder(audio->codecpar->codec_id);//拿到对应的解码器
    if(!codec){
        __android_log_print(ANDROID_LOG_ERROR,"没有获得解码器","%d");
        javaListener->onCallError(1,1003,"没有获得解码器");
        decode_exit=true;
        pthread_mutex_unlock(&init);
        return;
    }
    audio->avCodecContext=avcodec_alloc_context3(codec);
    if(!audio->avCodecContext){
        __android_log_print(ANDROID_LOG_ERROR,"获取解码器上下文失败","%d");
        javaListener->onCallError(1,1004,"获取解码器上下文失败");
        decode_exit=true;
        pthread_mutex_unlock(&init);
        return;
    }
    result=avcodec_parameters_to_context(audio->avCodecContext,audio->codecpar);//将信息复制到解码器上下文中 parameters
    if(result<0){
        __android_log_print(ANDROID_LOG_ERROR,"传参数给avCodecContext失败","%d");
        javaListener->onCallError(1,1005,"传参数给avCodecContext失败");
        decode_exit=true;
        pthread_mutex_unlock(&init);
        return;
    }
    result=avcodec_open2(audio->avCodecContext,codec,0);
    if(result!=0){
        __android_log_print(ANDROID_LOG_ERROR,"打开解码器失败","%d");
        javaListener->onCallError(1,1006,"打开解码器失败");
        decode_exit=true;
        pthread_mutex_unlock(&init);
        return;
    }
    if(javaListener!=NULL){
        if(status!=NULL&&!status->exit){
            javaListener->onCallPrepared(1);
        }else{
            decode_exit=true;
        }
    }
    pthread_mutex_unlock(&init);
}

void Decoder::startPlay() {
    if(!audio){
        __android_log_print(ANDROID_LOG_ERROR,"空音频对象","%d");
        javaListener->onCallError(1,1007,"空音频对象");
        return;
    }
    int result;
    audio->audioPlay();
    while(status!=NULL&&!status->exit){
        if(status->seek){
            av_usleep(1000 * 100);
            continue;//如果seek不执行
        }
        if(audio->audioQueue->getQueueSize()>100){
            av_usleep(1000 * 100);
            continue;//不会把所有的都解码到队列里
        }
        AVPacket *avPacket=av_packet_alloc();
        pthread_mutex_lock(&seek_mutex);
        result=av_read_frame(formatContext,avPacket);
        pthread_mutex_unlock(&seek_mutex);
        if(result==0){
            //开始入队
            if(avPacket->stream_index==audio->streamIndex){
                //找到对应的音频流了
                audio->audioQueue->pushAVPacket(avPacket);
            }else{
                av_packet_free(&avPacket);
                av_free(avPacket);
            }
        }else{
            av_packet_free(&avPacket);
            av_free(avPacket);
            while(status!=NULL&&!status->exit){
                if(audio->audioQueue->getQueueSize()>0){
                    av_usleep(1000 * 100);
                    continue;
                }else{
                    status->exit=true;
                    break;
                }
            }
            break;
        }
    }
    if(javaListener!=NULL){
        javaListener->onCallComlete(1);
    }
    decode_exit=true;
}

void Decoder::release() {
    //切歌时应该 将退出
    //比较麻烦，因为如果一开始时就释放，会导致正在初始化，并且有锁在，很麻烦
//    if(!status&&status->exit){
//        return;
//    }
    status->exit=true;
    pthread_mutex_lock(&init);
    //__android_log_print(ANDROID_LOG_ERROR,"进入了","");
    int sleep=0;
    while(!decode_exit){
        if(sleep>1000){
            decode_exit=true;
        }
        sleep++;
        av_usleep(1000*10);
    }
    if(audio!=NULL){
        audio->release();
        delete(audio);
        audio=NULL;
    }
    if(formatContext!=NULL){
        avformat_close_input(&formatContext);
        avformat_free_context(formatContext);
        formatContext=NULL;
    }
    if(status!=NULL){
        status=NULL;
    }
    if(javaListener!=NULL){
        javaListener=NULL;
    }
    pthread_mutex_unlock(&init);
}

void Decoder::seek(int64_t secds) {
    if(duration<=0){
        return;
    }
    if(secds>=0&&secds<=duration){
        if(audio!=NULL){
            status->seek=true;
            audio->audioQueue->clearAvPacket();
            audio->clock=0;
            audio->last_time=0;
            pthread_mutex_lock(&seek_mutex);
            int64_t rel=secds*AV_TIME_BASE;
            avformat_seek_file(formatContext,-1,INT64_MIN,rel,INT64_MAX,0);
            pthread_mutex_unlock(&seek_mutex);
            status->seek=false;
        }
    }
}
