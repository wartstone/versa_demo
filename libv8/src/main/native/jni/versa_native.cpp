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

JNIEXPORT jstring JNICALL
Java_com_hill_versa_NdkFunc_getString(JNIEnv *env, jobject instance) {

    // TODO


    return env->NewStringUTF("lingshan");
}

JNIEXPORT jstring JNICALL
Java_com_hill_versa_NdkFunc_call_1lua_1function(JNIEnv *env, jobject instance) {
    lua_State *L;

    L = luaL_newstate();
    luaL_openlibs(L);
    if (luaL_dofile(L, "counter_test.lua")) {
        printf("Could not load file: %s\n", lua_tostring(L, -1));
        lua_close(L);
        return env->NewStringUTF("fail");
    }

    lua_close(L);

    return env->NewStringUTF("sucess");
}

JNIEXPORT long JNICALL
Java_com_hill_versa_NdkFunc_call_1lua2_1function(JNIEnv *env, jobject thiz, jobject assetManager,
                                                 jstring nativeLibraryDir_) {
    // initialization
    lua_State * L = lua_open();
    luaL_openlibs(L);

    // execute script
    const char lua_script[] = "function sum(a, b) return a+b; end"; // a function that returns sum of two
    int load_stat = luaL_loadbuffer(L,lua_script,strlen(lua_script),lua_script);
    lua_pcall(L, 0, 0, 0);

    // load the function from global
    lua_getglobal(L,"sum");
    long sumval = 0;
    if(lua_isfunction(L, -1) )
    {
        // push function arguments into stack
        lua_pushnumber(L, 21.0);
        lua_pushnumber(L, 8.0);
        lua_pcall(L,2,1,0);
        if (!lua_isnil(L, -1))
        {
            sumval = lua_tonumber(L,-1);
            lua_pop(L,1);
        }
        D("sum = %d ", sumval);
    } else {
        D("not a function");
        D(lua_tostring(L, -1));
    }

    // cleanup
    lua_close(L);

    jlong ret = sumval;
    return ret;
}

JNIEXPORT jstring JNICALL
Java_com_hill_versa_NdkFunc_lua_1init(JNIEnv *env, jobject thiz, jobject assetManager,
                                                 jstring nativeLibraryDir_) {
    D("lua init.");

    //get native asset manager. This allows access to files stored in the assets folder
    AAssetManager *manager = AAssetManager_fromJava(env, assetManager);
    assert(NULL != manager);

    const char *nativeLibraryDir = env->GetStringUTFChars(nativeLibraryDir_, 0);
    char buffer[4096]; // buffer for textview output

    buffer[0] = 0;

    L = inittorch(manager, nativeLibraryDir); // create a lua_State
    assert(NULL != manager);

    globalEnv = env;

    // load file
    char file[] = "style_transfer.lua";
    int ret;
    long size = android_asset_get_size(file);
    if (size != -1) {
        char *filebytes = android_asset_get_bytes(file);
        ret = luaL_dobuffer(L, filebytes, size, "style_transfer");
        D("luaL_dobuffer ret = %d \n", ret);
        D(lua_tostring(L, -1));

//        ret = luaL_loadbuffer(L, filebytes, size, "style_transfer");
//        D("luaL_loadbuffer ret = %d \n", ret);
//        D(lua_tostring(L, -1));
//        ret = lua_pcall(L, 0, LUA_MULTRET, 0);
//        D("lua_pcall ret = %d \n", ret);

        /* push functions and arguments */
        //lua_getglobal(L, "main");  /* function to be called */

        /* do the call (2 arguments, 1 result) */
//        if (lua_pcall(L, 2, 1, 0) != 0)
//            error(L, "error running function `f': %s",
//                  lua_tostring(L, -1));

//        lua_newtable(L);
//        lua_pushinteger(L, 1);
//        lua_setfield(L, -2, "gpu");

//        ret = lua_pcall(L, 1, 1, 0);
//        D("lua_pcall ret = %d \n", ret);
//        D(lua_tostring(L, -1));








//        if (ret == 1) {
//            D("Torch Error doing resource: %s\n", file);
//            D(lua_tostring(L, -1));
//        } else {
//            D("Torch script ran succesfully.");
//        }
    }

    return env->NewStringUTF(buffer);
}

JNIEXPORT jlong JNICALL
Java_com_hill_versa_NdkFunc_lua_1init2(JNIEnv *env, jobject thiz, jobject assetManager,
                                      jstring nativeLibraryDir_) {
    //get native asset manager. This allows access to files stored in the assets folder
    AAssetManager *manager = AAssetManager_fromJava(env, assetManager);
    assert(NULL != manager);

    const char *nativeLibraryDir = env->GetStringUTFChars(nativeLibraryDir_, 0);
    char buffer[4096]; // buffer for textview output
    buffer[0] = 0;

    L = inittorch(manager, nativeLibraryDir); // create a lua_State
    assert(NULL != manager);
    globalEnv = env;

    // load file
    char file[] = "stylizer.lua";
    //char file[] = "counter_test.lua";
    const char *modelFileNative = "/storage/emulated/legacy/qianzu/download/the_scream.t7";
//    const char *input_image = "/storage/emulated/legacy/qianzu/download/chicago.jpg";
//    const char *output_image = "/storage/emulated/legacy/qianzu/download/output.jpg";
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
        D("lua init 4.1.");
        char *filebytes = android_asset_get_bytes(file);
        ret = luaL_loadbuffer(L, filebytes, size, "stylizer");
        lua_pcall(L, 0, 0, 0);

        lua_getglobal(L, "stylizer");
        //lua_getglobal(L, "testpng");
        lua_newtable(L);                            /* Push empty table onto stack table now at -1 */

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

        lua_pushstring(L, "model");
        lua_pushstring(L, modelFileNative);
        lua_settable(L, -3);

        lua_pushstring(L, "cudnn_benchmark");
        lua_pushinteger(L, 0);
        lua_settable(L, -3);

        lua_pushstring(L, "image_size");
        lua_pushinteger(L, 64);
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

        ret = lua_pcall(L, 1, 1, 0);
        D("[[lua_insert|lua_pcall]] ret = %d \n", ret);
        D(lua_tostring(L, -1));

        // cleanup
        lua_close(L);
    } else {
        D("lua init 4.0.");
        D(lua_tostring(L, -1));
    }

    jlong jret = 0;
    return jret;
}

}