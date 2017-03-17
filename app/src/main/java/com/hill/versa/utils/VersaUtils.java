package com.hill.versa.utils;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by hill on 17/3/9.
 */

public class VersaUtils {
    private static final String TAG = "hill/VersaUtils";

    private final static int kSystemRootStateUnknow=-1;
    private final static int kSystemRootStateDisable=0;
    private final static int kSystemRootStateEnable=1;
    private static int systemRootState=kSystemRootStateUnknow;

    // cpu usage for all
    public static float readCpuUsage() {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();

            String[] toks = load.split(" ");

            long idle1 = Long.parseLong(toks[5]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            try {
                Thread.sleep(360);
            } catch (Exception e) {}

            reader.seek(0);
            load = reader.readLine();
            reader.close();

            toks = load.split(" ");

            long idle2 = Long.parseLong(toks[5]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            return (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

        } catch (IOException ex) {
            Log.e(TAG, "readCpuUsage exception: " + ex);
        }

        return 0;
    }

    public static boolean isRootSystem()
    {
        if(systemRootState==kSystemRootStateEnable)
        {
            return true;
        }
        else if(systemRootState==kSystemRootStateDisable)
        {

            return false;
        }
        File f=null;
        final String kSuSearchPaths[]={"/system/bin/","/system/xbin/","/system/sbin/","/sbin/","/vendor/bin/"};
        try{
            for(int i=0;i<kSuSearchPaths.length;i++)
            {
                f=new File(kSuSearchPaths[i]+"su");
                if(f!=null&&f.exists())
                {
                    systemRootState=kSystemRootStateEnable;
                    return true;
                }
            }
        }catch(Exception e)
        {
        }
        systemRootState=kSystemRootStateDisable;
        return false;
    }
}
