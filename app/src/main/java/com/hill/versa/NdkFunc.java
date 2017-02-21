package com.hill.versa;

import android.content.res.AssetManager;

/**
 * Created by hill on 17/2/15.
 */

public class NdkFunc {
    static {
        System.loadLibrary("versa_native");
    }

    public native String getString();

    public native String call_lua_function();

    public native long call_lua2_function(AssetManager manager, String libdir);

    public native String lua_init(AssetManager manager, String libdir);

    public native long lua_init2(AssetManager manager, String libdir);
}
