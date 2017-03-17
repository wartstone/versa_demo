#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include "torchandroid.h"
#include "include/lua.h"
#include "include/lua.hpp"
#include <assert.h>
#include <android/log.h>
#include <sys/stat.h>
#include <unistd.h>

#define  LOG_TAG    "Versa"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGW(...)  __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

extern "C" {

lua_State *L;
JNIEnv *globalEnv;

JNIEXPORT bool JNICALL
Java_com_versa_lib_Versa_initialize(JNIEnv *env,
                                    jobject thiz,
                                    jobject assetManager,
                                    jstring nativeLibraryDir_,
                                    jstring luaFile_) {
    LOGW("start to run initialize...");
    AAssetManager *manager = AAssetManager_fromJava(env, assetManager);
    if(manager == NULL) {
        LOGW("assert manager is null");
        return false;
    }

    LOGW("step...1");
    const char *nativeLibraryDir = env->GetStringUTFChars(nativeLibraryDir_, 0);
    LOGW("step...2");
    const char *file = env->GetStringUTFChars(luaFile_, 0);
    LOGW("step...3");

    L = inittorch(manager, nativeLibraryDir); // create a lua_State
    LOGW("step...4");

    globalEnv = env;

    int ret;
    long size = android_asset_get_size(file);
    if (size != -1) {
        char *filebytes = android_asset_get_bytes(file);
        ret = luaL_dobuffer(L, filebytes, size, "main");
        if (ret == 1) {
            D("Torch Error doing resource: %s\n", file);
            D(lua_tostring(L,-1));
            return false;
        } else {
            D("Torch script ran succesfully.");
        }
        LOGW("step...6");
    }

    LOGW("step...7");
    return true;
}

JNIEXPORT bool JNICALL
Java_com_versa_lib_Versa_preload(JNIEnv *env,
                                    jobject thiz,
                                    jstring modelFile) {
    lua_getglobal(L, "pre_load");
    lua_newtable(L);                            /* Push empty table onto stack table now at -1 */

    const char *model = env->GetStringUTFChars(modelFile, 0);

    lua_pushstring(L, "model");
    lua_pushstring(L, model);
    lua_settable(L, -3);

    int ret = lua_pcall(L, 1, 0, 0);
    D("preload ret = %d \n", ret);
}

JNIEXPORT void JNICALL
Java_com_versa_lib_Versa_test(JNIEnv *env,
                                 jobject thiz,
                                 jint num) {
    lua_getglobal(L, "test");

    int ret = lua_pcall(L, 0, 0, 0);
    D("preload ret = %d \n", ret);
}

JNIEXPORT bool JNICALL
Java_com_versa_lib_Versa_postStylize(
        JNIEnv *env, jobject thiz, jstring inputImage, jstring outputImage, jint modelIndex,
        jint gpu, jint useCudnn, jint imageSize, jint timing, jint medianFilter, jint cudnnBenchmark, jstring backend) {
    const char *inputImageNative = env->GetStringUTFChars(inputImage, 0);
    const char *outputImageNative = env->GetStringUTFChars(outputImage, 0);
    const char *backendNative = env->GetStringUTFChars(backend, 0);

    lua_getglobal(L, "post_stylize");
    lua_newtable(L);

    lua_pushstring(L, "input_image");                /* Push a key onto the stack, table now at -2 */
    lua_pushstring(L, inputImageNative);               /* Push a value onto the stack, table now at -3 */
    lua_settable(L, -3);

    lua_pushstring(L, "output_image");
    lua_pushstring(L, outputImageNative);
    lua_settable(L, -3);

    lua_pushstring(L, "input_dir");
    lua_pushstring(L, "");
    lua_settable(L, -3);

    lua_pushstring(L, "backend");
    lua_pushstring(L, backendNative);
    lua_settable(L, -3);

    lua_pushstring(L, "use_cudnn");
    lua_pushinteger(L, useCudnn);
    lua_settable(L, -3);

    lua_pushstring(L, "cudnn_benchmark");
    lua_pushinteger(L, cudnnBenchmark);
    lua_settable(L, -3);

    lua_pushstring(L, "image_size");
    lua_pushinteger(L, imageSize);
    lua_settable(L, -3);

    lua_pushstring(L, "timing");
    lua_pushinteger(L, timing);
    lua_settable(L, -3);

    lua_pushstring(L, "median_filter");
    lua_pushinteger(L, medianFilter);
    lua_settable(L, -3);

    lua_pushstring(L, "gpu");
    lua_pushinteger(L, gpu);
    lua_settable(L, -3);

    lua_pushstring(L, "index");
    lua_pushinteger(L, modelIndex);
    lua_settable(L, -3);

    int ret = lua_pcall(L, 1, 0, 0);
    D("postStylize ret = %d \n", ret);
    D("ret = %s", lua_tostring(L, -1));

    lua_pop(L, 1);
    lua_settop(L, 0);


    lua_gc(L, LUA_GCCOLLECT, 0);
}

//called from lua
static void onProgressUpdate(lua_State *L) {

    int n = lua_gettop(L);
    int i;

    size_t len;
    const char *log = lua_tolstring(L, 1, &len);

    jclass clazz = globalEnv->FindClass("com/versa/lib/Versa");
    jmethodID onProgressUpdate = globalEnv->GetStaticMethodID(clazz, "onProgressUpdate",
                                                              "(Ljava/lang/String;)V");

    jstring logString = globalEnv->NewStringUTF(log);
    globalEnv->CallStaticVoidMethod(clazz, onProgressUpdate, logString);
    globalEnv->DeleteLocalRef(logString);
    globalEnv->DeleteLocalRef(clazz);
}

//called from lua
static void onImageSaved(lua_State *L) {

    int n = lua_gettop(L);
    int i;

    size_t len;
    const char *path = lua_tolstring(L, 1, &len);

    jclass clazz = globalEnv->FindClass("com/versa/lib/Versa");
    jmethodID onImageSaved = globalEnv->GetStaticMethodID(clazz, "onImageSaved",
                                                          "(Ljava/lang/String;)V");

    jstring pathString = globalEnv->NewStringUTF(path);
    globalEnv->CallStaticVoidMethod(clazz, onImageSaved, pathString);
    globalEnv->DeleteLocalRef(pathString);
    globalEnv->DeleteLocalRef(clazz);

}

//called from lua
static void onIterationUpdate(lua_State *L) {
    int n = lua_gettop(L);
    int i;

    int currentIteration = lua_tonumber(L, 1);
    int totalIterations = lua_tonumber(L, 2);

    jclass clazz = globalEnv->FindClass("com/versa/lib/Versa");
    jmethodID onIterationUpdate = globalEnv->GetStaticMethodID(clazz, "onIterationUpdate",
                                                               "(II)V");

    globalEnv->CallStaticVoidMethod(clazz, onIterationUpdate, currentIteration, totalIterations);
    globalEnv->DeleteLocalRef(clazz);

}

//called from lua
static void onCompleted(lua_State *L) {

    jclass clazz = globalEnv->FindClass("com/versa/lib/Versa");
    jmethodID onCompleted = globalEnv->GetStaticMethodID(clazz, "onCompleted",
                                                         "()V");
    globalEnv->CallStaticVoidMethod(clazz, onCompleted);
    globalEnv->DeleteLocalRef(clazz);

}

JNIEXPORT jstring JNICALL
Java_com_versa_lib_Versa_destroy(JNIEnv *env, jobject thiz) {
    // destroy the Lua State
    lua_close(L);
}

JNIEXPORT void JNICALL
Java_com_versa_lib_Versa_setProgressListener(JNIEnv *env) {
    lua_CFunction function = (lua_CFunction) onProgressUpdate;
    lua_pushcfunction(L, function);
    lua_setglobal(L, "updateProgress");
}

JNIEXPORT void JNICALL
Java_com_versa_lib_Versa_setIterationListener(JNIEnv *env) {
    lua_CFunction function = (lua_CFunction) onIterationUpdate;
    lua_pushcfunction(L, function);
    lua_setglobal(L, "updateIteration");
}

JNIEXPORT void JNICALL
Java_com_versa_lib_Versa_setImageSavedListener(JNIEnv *env) {
    lua_CFunction function = (lua_CFunction) onImageSaved;
    lua_pushcfunction(L, function);
    lua_setglobal(L, "onImageSaved");
}

JNIEXPORT void JNICALL
Java_com_versa_lib_Versa_setCompletionListener(JNIEnv *env) {
    lua_CFunction function = (lua_CFunction) onCompleted;
    lua_pushcfunction(L, function);
    lua_setglobal(L, "onCompleted");
}

}