package com.hill.versa;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hill.versa.utils.VersaUtils;
import com.versa.lib.Versa;
import com.versa.lib.VersaBuilder;
import com.versa.lib.listeners.CompletionListener;
import com.versa.lib.listeners.ImageSavedListener;
import com.versa.lib.listeners.ProgressListener;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

public class MainActivity extends AppCompatActivity {
    private String TAG = "hill/MainActivity";

    private HandlerThread mHandlerThread;
    private StylizeHandler mHandler;

    private Thread mCheckThread;

    private AtomicBoolean mCheckLock= new AtomicBoolean(true);

    private TextView tvStylize, tvStylize2, tvTest;
    private TextView tvResult, tvPreload, tvStopCpuCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationInfo info = getApplicationInfo();

        mHandlerThread = new HandlerThread("stylizethread");
        mHandlerThread.start();
        mHandler = new StylizeHandler(mHandlerThread.getLooper(), this);

        mCheckThread = new Thread("checkcputhread") {
            @Override
            public void run() {
                synchronized (mCheckLock) {
                    while (mCheckLock.get()) {
                        float usage = VersaUtils.readCpuUsage();
                        Log.d(TAG, "[CheckCPUThread] cpu usage = " + usage);

                        try {
                            Thread.sleep(600);
                        } catch (InterruptedException ex) {
                            Log.e(TAG, "[CheckCPUThread] get cpu usage error");
                        }
                    }
                }
            }
        };
        mCheckThread.start();

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

        tvTest = (TextView) findViewById(R.id.test);
        tvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });

        tvStopCpuCheck = (TextView) findViewById(R.id.stopcpucheck);
        tvStopCpuCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopCheckCpuThread();
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

    private void test() {
        Message msg = Message.obtain();
        msg.what = StylizeHandler.MSG_TEST;
        mHandler.sendMessage(msg);
    }

    private void stopCheckCpuThread() {
        mCheckLock.set(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandlerThread.quit();//停止Looper的循环
    }
}
