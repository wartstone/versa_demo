package com.hill.versa;

import android.content.res.AssetManager;

/**
 * Created by hill on 17/2/15.
 */

public class NdkFunc {
    static {
        System.loadLibrary("versa_native");
    }

    public native long lua_init(AssetManager manager, String libdir);
}
