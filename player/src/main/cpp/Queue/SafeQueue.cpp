//
// Created by 王强 on 2019/9/1.
//
#include "SafeQueue.h"

SafeQueue::SafeQueue(Status *status) {
    this->status=status;
    pthread_mutex_init(&mutexPacket,NULL);
    pthread_cond_init(&condPacktet,NULL);
}

SafeQueue::~SafeQueue() {
    clearAvPacket();
    pthread_mutex_destroy(&mutexPacket);
    pthread_cond_destroy(&condPacktet);
}

int SafeQueue::pushAVPacket(AVPacket *packet) {
    pthread_mutex_lock(&mutexPacket);
    queuePacket.push(packet);
    pthread_cond_signal(&condPacktet);
    pthread_mutex_unlock(&mutexPacket);
    return 0;
}

int SafeQueue::popAVPacket(AVPacket *packet) {
    pthread_mutex_lock(&mutexPacket);
    while(status!=NULL&&!status->exit){
        if(queuePacket.size()>0){
            AVPacket *avPacket=queuePacket.front();
            if(av_packet_ref(packet,avPacket)==0){
                queuePacket.pop();
            }
            av_packet_free(&avPacket);
            av_free(avPacket);
            avPacket=NULL;
            break;
        }else{
            pthread_cond_wait(&condPacktet,&mutexPacket);
        }
    }
    pthread_mutex_unlock(&mutexPacket);
    return 0;
}

int SafeQueue::getQueueSize() {
    int size=0;
    pthread_mutex_lock(&mutexPacket);
    size = queuePacket.size();
    pthread_mutex_unlock(&mutexPacket);
    return size;
}

void SafeQueue::clearAvPacket() {
    //有线程锁
    pthread_cond_signal(&condPacktet);
    pthread_mutex_lock(&mutexPacket);
    while(!queuePacket.empty()){
        AVPacket *avPacket=queuePacket.front();
        queuePacket.pop();
        av_packet_free(&avPacket);
        av_free(avPacket);
        avPacket=NULL;
    }
    pthread_mutex_unlock(&mutexPacket);
}
