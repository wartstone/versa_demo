package com.hill.versa;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.versa.lib.Versa;
import com.versa.lib.VersaBuilder;
import com.versa.lib.listeners.CompletionListener;
import com.versa.lib.listeners.ImageSavedListener;
import com.versa.lib.listeners.ProgressListener;

/**
 * Created by hill on 17/3/15.
 */

public class StylizeHandler extends Handler {
    private String TAG = "hill/StylizeHandler";

    private Context mContext;

    public final static int MSG_PRELOAD = 1;
    public final static int MSG_POSTSTYLIZE = 2;
    public final static int MSG_TEST = 3;

    public StylizeHandler(Looper looper, Context context) {
        super(looper);
        mContext = context;
    }

    @Override
    public void handleMessage(Message msg) {
        if(msg.what == MSG_PRELOAD){
            initialize(mContext);
        } else if (msg.what == MSG_POSTSTYLIZE) {
            postStylize((int)msg.obj);
        } else if(msg.what == MSG_TEST) {
            test();
        }
    }

    private Versa mVersa;

    private void initialize(Context context) {
        VersaBuilder builder = new VersaBuilder(context);
        mVersa = builder.build();
        mVersa.initialize();
        mVersa.setLogEnabled(true);
        mVersa.setProgressListener(new ProgressListener() {
            @Override
            public void onUpdateProgress(final String log, boolean important) {
                //Log.d(TAG, "onUpdateProgress");
                //tvResult.setText(log);
            }
        });
        mVersa.setImageSavedListener(new ImageSavedListener() {
            @Override
            public void onImageSaved(String path) {
                Log.d(TAG, "onImageSaved");
            }
        });
        mVersa.setCompletionListsner(new CompletionListener() {
            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        });
        mVersa.preload();
    }

    private void postStylize(int modelIndex) {
        mVersa.postStylize(modelIndex);
    }

    private void test() {
        mVersa.test();
    }
}
