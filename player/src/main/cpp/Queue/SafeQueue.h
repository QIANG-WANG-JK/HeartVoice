//
// Created by 王强 on 2019/9/1.
//

#ifndef AUDIOPLAYER_SAFEQUEUE_H
#define AUDIOPLAYER_SAFEQUEUE_H
#include <queue>
#include <pthread.h>
#include <android/log.h>
#include "../Status/Status.h"

extern "C"{
#include <libavcodec/avcodec.h>
#include <libavutil/mem.h>
};

using namespace std;

class SafeQueue {

public:
    queue<AVPacket*> queuePacket;
    pthread_mutex_t mutexPacket;
    pthread_cond_t condPacktet;
    Status *status=NULL;

public:
    SafeQueue(Status *status);
    ~SafeQueue();

    int pushAVPacket(AVPacket *packet);
    int popAVPacket(AVPacket *packet);
    int getQueueSize();
    void clearAvPacket();
};


#endif //AUDIOPLAYER_SAFEQUEUE_H
