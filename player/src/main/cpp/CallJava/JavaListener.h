//
// Created by 王强 on 2019/8/31.
//

#ifndef AUDIOPLAYER_JAVALISTENER_H
#define AUDIOPLAYER_JAVALISTENER_H

#include <jni.h>
#include <android/log.h>

class JavaListener {

public:
    JavaVM *vm=NULL;
    JNIEnv *env=NULL;
    jobject jobj;
    jmethodID prepare_mid;
    jmethodID load_mid;
    jmethodID time_mid;
    jmethodID error_mid;
    jmethodID complete_mid;

public:
    JavaListener(JavaVM *vm,JNIEnv *env,jobject obj);
    ~JavaListener();
    /**
     * 0主线程
     * 1子线程
     * @param type
     * @param code
     */
    void onCallPrepared(int type);
    void onCallLoad(int type,bool load);
    void onCallTime(int type,int cur,int total);
    void onCallError(int type,int code,char *msg);
    void onCallComlete(int type);
};


#endif //AUDIOPLAYER_JAVALISTENER_H
