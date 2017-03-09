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

extern "C" {

lua_State *L;
JNIEnv *globalEnv;

void pre_load(char* modelFile);
void post_stylize(int index);

JNIEXPORT jlong JNICALL
Java_com_hill_versa_NdkFunc_lua_1init(JNIEnv *env, jobject thiz, jobject assetManager,
                                      jstring nativeLibraryDir_) {
    AAssetManager *manager = AAssetManager_fromJava(env, assetManager);
    assert(NULL != manager);

    const char *nativeLibraryDir = env->GetStringUTFChars(nativeLibraryDir_, 0);

    L = inittorch(manager, nativeLibraryDir); // create a lua_State
    assert(NULL != manager);
    globalEnv = env;

    char file[] = "stylizer.lua";
    const char *modelFileNative = "/storage/emulated/legacy/qianzu/download/2.t7";
    const char *input_image = "/storage/emulated/legacy/qianzu/download/input.png";
    const char *output_image = "/storage/emulated/legacy/qianzu/download/output.png";

    if (FILE *file = fopen(modelFileNative, "r")) {
        fclose(file);
        D("----file exist");
    } else {
        D("----file not exist");
    }

    int ret;
    long size = android_asset_get_size(file);
    if (size != -1) {
        char *filebytes = android_asset_get_bytes(file);
        ret = luaL_loadbuffer(L, filebytes, size, "stylizer");
        lua_pcall(L, 0, 0, 0);

        pre_load("/storage/emulated/legacy/qianzu/download/1.t7");
        pre_load("/storage/emulated/legacy/qianzu/download/2.t7");

        sleep(2);
        D("sleep done");

        post_stylize(2);
//        post_stylize(1);

        lua_close(L);
    } else {
        D("lua init 4.0.");
        D(lua_tostring(L, -1));
    }

    jlong jret = 0;
    return jret;
}

void pre_load(char* modelFile) {
    lua_getglobal(L, "pre_load");
    lua_newtable(L);                            /* Push empty table onto stack table now at -1 */

    lua_pushstring(L, "model");
    lua_pushstring(L, modelFile);
    lua_settable(L, -3);

    int ret = lua_pcall(L, 1, 0, 0);
    D("[[lua_pcall]] ret = %d \n", ret);
}

void post_stylize(int index) {
    const char *input_image = "/storage/emulated/legacy/qianzu/download/input.png";
    const char *output_image = "/storage/emulated/legacy/qianzu/download/output.png";

    lua_getglobal(L, "post_stylize");
    lua_newtable(L);

    lua_pushstring(L, "input_image");                /* Push a key onto the stack, table now at -2 */
    lua_pushstring(L, input_image);               /* Push a value onto the stack, table now at -3 */
    lua_settable(L, -3);

    lua_pushstring(L, "output_image");
    lua_pushstring(L, output_image);
    lua_settable(L, -3);

    lua_pushstring(L, "input_dir");
    lua_pushstring(L, "");
    lua_settable(L, -3);

    lua_pushstring(L, "backend");
    lua_pushstring(L, "opencl");
    lua_settable(L, -3);

    lua_pushstring(L, "use_cudnn");
    lua_pushinteger(L, 0);
    lua_settable(L, -3);

    lua_pushstring(L, "cudnn_benchmark");
    lua_pushinteger(L, 0);
    lua_settable(L, -3);

    lua_pushstring(L, "image_size");
    lua_pushinteger(L, 512);
    lua_settable(L, -3);

    lua_pushstring(L, "timing");
    lua_pushinteger(L, 0);
    lua_settable(L, -3);

    lua_pushstring(L, "median_filter");
    lua_pushinteger(L, 3);
    lua_settable(L, -3);

    lua_pushstring(L, "gpu");
    lua_pushinteger(L, -1);
    lua_settable(L, -3);

    lua_pushstring(L, "index");
    lua_pushinteger(L, index);
    lua_settable(L, -3);

    int ret = lua_pcall(L, 1, 0, 0);
    D("[[lua_pcall]] ret = %d \n", ret);
    D("ret = %s", lua_tostring(L, -1));
}

}