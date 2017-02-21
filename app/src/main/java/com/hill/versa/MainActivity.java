package com.hill.versa;

import android.content.pm.ApplicationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationInfo info = getApplicationInfo();

        String text = String.valueOf(new NdkFunc().lua_init2(getAssets(), info.nativeLibraryDir));

        TextView textView = (TextView) findViewById(R.id.tv);
        //textView.setText(String.valueOf(new NdkFunc().call_lua2_function(getAssets(), info.nativeLibraryDir)));
        textView.setText(text);
    }
}
