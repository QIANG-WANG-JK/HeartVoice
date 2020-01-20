/**
 * 头文件
 */
#include <jni.h>
#include <string>
#include <pthread.h>
#include <android/log.h>
#include "CallJava/JavaListener.h"
#include "Decode/Decoder.h"
#include "Status/Status.h"

/**
 * 命名空间
 */
using namespace std;

/**
 * 变量
 */
JavaVM *jvm=NULL;
JavaListener *javaListener=NULL;
Decoder *decoder=NULL;
Status *status=NULL;
bool native_exit=true;
pthread_t p_paly;

extern "C"
JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm,void* reserved){
    JNIEnv *env;
    jvm=vm;
    if(vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) return -1;
    return JNI_VERSION_1_6;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hv_player_AudioPlayer_audio_1preapre(JNIEnv *env, jobject instance,jstring source_) {
    //TODO
    const char *source = env->GetStringUTFChars(source_, 0);
    if(!decoder){
        if(!javaListener){
            javaListener=new JavaListener(jvm,env,instance);
        }
        if(!status){
            status=new Status();
        }
        javaListener->onCallLoad(0,true);
        decoder=new Decoder(javaListener,source,status);
        decoder->prepareDecode();
    }
    env->ReleaseStringUTFChars(source_, source);
}

void *startPlayCallback(void *data){
    Decoder *decoder=static_cast<Decoder *>(data);
    decoder->startPlay();
    pthread_exit(&p_paly);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hv_player_AudioPlayer_audio_1play(JNIEnv *env, jobject instance) {
    // TODO
    if(!decoder->status->exit){
        pthread_create(&p_paly,NULL,startPlayCallback,decoder);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hv_player_AudioPlayer_audio_1resume(JNIEnv *env, jobject instance) {

    // TODO
    if(decoder!=NULL&&decoder->audio!=NULL){
        decoder->audio->resume();
    }

}

extern "C"
JNIEXPORT void JNICALL
Java_com_hv_player_AudioPlayer_audio_1pause(JNIEnv *env, jobject instance) {

    // TODO
    if(decoder!=NULL&&decoder->audio!=NULL){
        decoder->audio->pause();
    }

}

extern "C"
JNIEXPORT void JNICALL
Java_com_hv_player_AudioPlayer_audio_1stop(JNIEnv *env, jobject instance) {

    // TODO
    if(!native_exit){
        return;
    }
    //__android_log_print(ANDROID_LOG_ERROR,"停止了","");
    native_exit=false;
    jclass cls=env->GetObjectClass(instance);
    jmethodID  next_mid=env->GetMethodID(cls,"onCallNext","()V");
    if(decoder!=NULL){
        decoder->release();
        delete(decoder);
        decoder=NULL;
        if(javaListener!=NULL){
            delete(javaListener);
            javaListener=NULL;
        }
        if(status!=NULL){
            delete(status);
            status=NULL;
        }
    }
    native_exit= true;
    env->CallVoidMethod(instance,next_mid);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_hv_player_AudioPlayer_native_1seek(JNIEnv *env, jobject instance,
                                                                jint secds) {
    // TODO
    if(decoder!=NULL){
        decoder->seek(secds);
    }

}

extern "C"
JNIEXPORT jint JNICALL
Java_com_hv_player_AudioPlayer_native_1duration(JNIEnv *env, jobject thiz) {
    // TODO: implement native_duration()
    if(decoder!=NULL){
        return decoder->duration;
    }
    return 0;
}