package com.versa.lib;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.versa.lib.listeners.CompletionListener;
import com.versa.lib.listeners.ImageSavedListener;
import com.versa.lib.listeners.IterationListener;
import com.versa.lib.listeners.ProgressListener;

import java.io.File;

public class Versa {

    AssetManager assetManager;
    ApplicationInfo info;
    VersaBuilder builder;

    static boolean logEnabled = true;

    static ProgressListener progressListener;
    static IterationListener iterationListener;
    static ImageSavedListener imageSavedListener;
    static CompletionListener completionListener;

    static {
        System.loadLibrary("png16");
        System.loadLibrary("versa_native");
    }

    public Versa(Context context, VersaBuilder builder) {
        this.builder = builder;
        assetManager = context.getAssets();
        info = context.getApplicationInfo();
    }

    public void initialize() {
        initialize(assetManager, info.nativeLibraryDir, "stylizer.lua");
    }

    public void preload() {
        for(String modelFile : builder.preload_models) {
            preload(modelFile);
        }
    }

    public void postStylize(int modelIndex) {
        File myDir = new File(Environment.getExternalStorageDirectory() + "/Arcade/outputs");

        if (!myDir.exists())
            myDir.mkdirs();

        postStylize(builder.inputImage, builder.outputImage, modelIndex, builder.gpu, builder.useCudnn, builder.imageSize,
                builder.timing, builder.medianFilter, builder.cudnnBenchmark, builder.backend);
    }

    public void test() {
        test(4);
    }

    public void destroy(boolean b) {
        destroy();
    }

    public void destroyArcade() {
        destroy();
    }

    public void setProgressListener(ProgressListener progressListener) {
        setProgressListener();
        this.progressListener = progressListener;
    }

    public void setIterationListener(IterationListener iterationListener) {
        setProgressListener();
        this.iterationListener = iterationListener;
    }

    public void setImageSavedListener(ImageSavedListener listener) {
        setImageSavedListener();
        this.imageSavedListener = listener;
    }

    public void setCompletionListsner(CompletionListener listsner) {
        setCompletionListener();
        this.completionListener = listsner;
    }

    public void setLogEnabled(boolean enabled) {
        this.logEnabled = enabled;
    }

    //Called from C
    public static void onProgressUpdate(String log) {
        if (logEnabled) {
            Log.d("Arcade ", log);
        }
        if (progressListener != null) {
            progressListener.onUpdateProgress(log, true);
        }
    }

    //Called from C
    public static void onIterationUpdate(int current, int total) {
        if (iterationListener != null) {
            iterationListener.onIteration(current, total);
        }
    }

    //Called from C
    public static void onImageSaved(String path) {
        if (imageSavedListener != null) {
            imageSavedListener.onImageSaved(path);
        }
    }

    //Called from C
    public static void onCompleted() {
        if (completionListener != null) {
            completionListener.onComplete();
        }
    }

    // native method
    private native boolean initialize(AssetManager manager, String path, String luafile);

    private native boolean preload(String modelFile);

    private native void postStylize(String inputImage, String outputImage, int modelIndex, int gpu, int useCudnn, int imageSize,
                                     int timing, int medianFilter, int cudnnBenchmark, String backend);

    private native String destroy();

    private native void setProgressListener();

    private native void setIterationListener();

    private native void setImageSavedListener();

    private native void setCompletionListener();

    private native void test(int num);

}
