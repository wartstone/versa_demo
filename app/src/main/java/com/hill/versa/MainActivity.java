package com.hill.versa;

import android.content.pm.ApplicationInfo;
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

public class MainActivity extends AppCompatActivity {
    private String TAG = "hill/MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationInfo info = getApplicationInfo();

        //String text = String.valueOf(new NdkFunc().lua_init(getAssets(), info.nativeLibraryDir));

        TextView textView = (TextView) findViewById(R.id.style);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stylize();
            }
        });
    }

    private void stylize() {
        if(VersaUtils.isRootSystem()) {
            System.out.println("system is rooted");
        } else {
            System.out.println("system is not rooted");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                VersaBuilder builder = new VersaBuilder(MainActivity.this);
                final Versa arcade = builder.build();
                arcade.initialize();
                arcade.setLogEnabled(true);
                arcade.setProgressListener(new ProgressListener() {
                    @Override
                    public void onUpdateProgress(final String log, boolean important) {
                        Log.d(TAG, "onUpdateProgress");
                    }
                });
                arcade.setImageSavedListener(new ImageSavedListener() {
                    @Override
                    public void onImageSaved(String path) {
                        Log.d(TAG, "onImageSaved");
                    }
                });
                arcade.setCompletionListsner(new CompletionListener() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
                arcade.preload();
                arcade.postStylize(1);
            }
        }).start();
    }
}
