//
// Created by 王强 on 2019/8/31.
//
#include "JavaListener.h"

JavaListener::JavaListener(JavaVM *vm, JNIEnv *env, jobject obj) {
    this->vm=vm;
    this->env=env;
    this->jobj=obj;
    this->jobj=env->NewGlobalRef(jobj);
    jclass cls=env->GetObjectClass(jobj);
    if(!cls){
        return;
    }
    prepare_mid=env->GetMethodID(cls,"onCallPrepared","()V");
    load_mid=env->GetMethodID(cls,"onCallLoad","(Z)V");
    time_mid=env->GetMethodID(cls,"onCallTime","(II)V");
    error_mid=env->GetMethodID(cls,"onCallError", "(ILjava/lang/String;)V");
    complete_mid=env->GetMethodID(cls,"onCallComplete","()V");
}

JavaListener::~JavaListener() {
    //env->DeleteGlobalRef(jobj);
}

void JavaListener::onCallPrepared(int type) {
    if(type==0){
        //主线程
        env->CallVoidMethod(jobj,prepare_mid);
    }else if(type==1){
        JNIEnv *jniEnv;
        int result=vm->AttachCurrentThread(&jniEnv,0);
        if(result!=JNI_OK){
            return;
        }
        jniEnv->CallVoidMethod(jobj,prepare_mid);
        vm->DetachCurrentThread();
    }
}

void JavaListener::onCallLoad(int type, bool load) {

    if(type==0){
        env->CallVoidMethod(jobj,load_mid,load);
    }else if(type==1){
        JNIEnv *jniEnv;
        int result=vm->AttachCurrentThread(&jniEnv,0);
        if(result!=JNI_OK){
            return;
        }
        jniEnv->CallVoidMethod(jobj,load_mid,load);
        vm->DetachCurrentThread();
    }

}

void JavaListener::onCallTime(int type, int cur, int total) {
    if(type==0){
        env->CallVoidMethod(jobj,time_mid,cur,total);
    }else if(type==1){
        JNIEnv *jniEnv;
        int result=vm->AttachCurrentThread(&jniEnv,0);
        if(result!=JNI_OK){
            return;
        }
        jniEnv->CallVoidMethod(jobj,time_mid,cur,total);
        vm->DetachCurrentThread();
    }
}

void JavaListener::onCallError(int type, int code, char *msg) {
    if(type==0){
        jstring jmsg=env->NewStringUTF(msg);
        env->CallVoidMethod(jobj,error_mid,code,jmsg);
        env->DeleteLocalRef(jmsg);
    }else if(type==1){
        JNIEnv *jniEnv;
        int result=vm->AttachCurrentThread(&jniEnv,0);
        if(result!=JNI_OK){
            return;
        }
        jstring jmsg=jniEnv->NewStringUTF(msg);
        jniEnv->CallVoidMethod(jobj,time_mid,code,jmsg);
        jniEnv->DeleteLocalRef(jmsg);
        vm->DetachCurrentThread();
    }
}

void JavaListener::onCallComlete(int type) {
    if(type==0){
        env->CallVoidMethod(jobj,complete_mid);
    }else if(type==1){
        JNIEnv *jniEnv;
        int result=vm->AttachCurrentThread(&jniEnv,0);
        if(result!=JNI_OK){
            return;
        }
        jniEnv->CallVoidMethod(jobj,complete_mid);
        vm->DetachCurrentThread();
    }
}
