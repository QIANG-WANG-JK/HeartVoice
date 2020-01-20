//
// Created by 王强 on 2019/9/1.
//

#ifndef AUDIOPLAYER_DECODER_H
#define AUDIOPLAYER_DECODER_H
#include<pthread.h>
#include "../CallJava/JavaListener.h"
#include "../Audio/Audio.h"
#include "../Status/Status.h"

extern "C"{
#include <libavformat/avformat.h>
#include <libavutil/avutil.h>
#include <libavcodec/avcodec.h>
#include <libavutil/time.h>
};

class Decoder {

public:
    JavaListener *javaListener=NULL;
    const char* url=NULL;
    pthread_t decodeThread;
    AVFormatContext *formatContext=NULL;
    Audio *audio=NULL;
    Status *status=NULL;
    pthread_mutex_t init;
    bool decode_exit=false;
    int duration;
    pthread_mutex_t seek_mutex;

public:
    Decoder(JavaListener *javaListener,const char* url,Status *status);
    ~Decoder();
    void prepareDecode();
    void beginDecode();
    void startPlay();
    void release();
    void seek(int64_t secds);
};


#endif //AUDIOPLAYER_DECODER_H
