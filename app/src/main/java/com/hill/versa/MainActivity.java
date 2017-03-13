package com.hill.versa;

import android.content.pm.ApplicationInfo;
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

    private TextView tvResult, tvPreload, tvStylize, tvStylize2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationInfo info = getApplicationInfo();

        //String text = String.valueOf(new NdkFunc().lua_init(getAssets(), info.nativeLibraryDir));

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
    
    private Versa mVersa;

    private void initialize() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                VersaBuilder builder = new VersaBuilder(MainActivity.this);
                mVersa = builder.build();
                mVersa.initialize();
                mVersa.setLogEnabled(true);
                mVersa.setProgressListener(new ProgressListener() {
                    @Override
                    public void onUpdateProgress(final String log, boolean important) {
                        Log.d(TAG, "onUpdateProgress");
                        tvResult.setText(log);
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
        }).start();
    }

    private void post_stylize(final int modelIndex) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mVersa.postStylize(modelIndex);
            }
        }).start();
    }
}
