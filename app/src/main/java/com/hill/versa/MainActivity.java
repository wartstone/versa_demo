package com.hill.versa;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.versa.lib.Versa;
import com.versa.lib.VersaBuilder;
import com.versa.lib.listeners.CompletionListener;
import com.versa.lib.listeners.ImageSavedListener;
import com.versa.lib.listeners.ProgressListener;

public class MainActivity extends AppCompatActivity {
    private String TAG = "hill/MainActivity";

    private HandlerThread mHandlerThread;
    private StylizeHandler mHandler;

    private TextView tvResult, tvPreload, tvStylize, tvStylize2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationInfo info = getApplicationInfo();

        mHandlerThread = new HandlerThread("stylizethread");
        mHandlerThread.start();
        mHandler = new StylizeHandler(mHandlerThread.getLooper(), this);

        tvResult = (TextView) findViewById(R.id.result);

        tvPreload = (TextView) findViewById(R.id.preload);
        tvPreload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialize();
            }
        });

        tvStylize = (TextView) findViewById(R.id.style);
        tvStylize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_stylize(1);
            }
        });

        tvStylize2 = (TextView) findViewById(R.id.style2);
        tvStylize2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_stylize(2);
            }
        });
    }

    private void initialize() {
        Message msg = Message.obtain();
        msg.what = StylizeHandler.MSG_PRELOAD;
        mHandler.sendMessage(msg);
    }

    private void post_stylize(final int modelIndex) {
        Message msg = Message.obtain();
        msg.obj = modelIndex;
        msg.what = StylizeHandler.MSG_POSTSTYLIZE;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandlerThread.quit();//停止Looper的循环
    }
}
