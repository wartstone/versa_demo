package com.versa.lib;

import android.content.Context;
import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naman on 12/05/16.
 */
public class VersaBuilder {

    private Context context;

    public String inputImage;
    public String outputImage;
    public String model;
    public String backend;
    public int gpu;
    public int useCudnn;
    public int imageSize;
    public int cudnnBenchmark;
    public int timing;
    public int medianFilter;

    public List<String> preload_models = new ArrayList<>();

    public VersaBuilder(Context context) {
        this.context = context;
        this.gpu = -1;
        this.inputImage = Environment.getExternalStorageDirectory() + "/qianzu/download/input.png";
        this.outputImage = Environment.getExternalStorageDirectory() + "/qianzu/download/output.png";
        this.model = Environment.getExternalStorageDirectory() + "/qianzu/download/the_scream.t7";
        this.backend = "opencl";
        this.imageSize = 512;
        this.medianFilter = 3;
        this.timing = 0;
        this.useCudnn = 0;
        this.cudnnBenchmark = 0;

        this.preload_models.add(Environment.getExternalStorageDirectory() + "/qianzu/download/2.t7");
        this.preload_models.add(Environment.getExternalStorageDirectory() + "/qianzu/download/1.t7");
    }



    public void setInputImage(String inputImage) {
        this.inputImage = inputImage;
    }

    public void setOutputImage(String outputImage) {
        this.outputImage = outputImage;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Versa build() {
        return new Versa(context, this);
    }
}
