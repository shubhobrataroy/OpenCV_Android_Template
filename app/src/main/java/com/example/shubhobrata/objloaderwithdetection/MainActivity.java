package com.example.shubhobrata.objloaderwithdetection;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    JavaCamera2View cameraViewer;
    Mat mRgba;
    int widthCameraFrame;
    int heightCameraFrame;

    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == BaseLoaderCallback.SUCCESS)
                cameraViewer.enableView();

            else super.onManagerConnected(status);
        }
    };

    static {
        System.loadLibrary("native-lib");
        String load= OpenCVLoader.initDebug()? "Loaded":"Not Loaded";
        Log.e("OpenCVLoad",load);
    }

    private final int REQUEST_PERMISSION=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION);
        }
        cameraViewer = findViewById(R.id.cameraViewer);
        cameraViewer.setCvCameraViewListener(this);
    }

    public native String stringFromJNI();

    public native String matchOpenCV(long rgbaMat);

    @Override
    public void onCameraViewStarted(int width, int height) {
        this.widthCameraFrame = width;
        this.heightCameraFrame = height;
        mRgba = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (cameraViewer != null)
            cameraViewer.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, baseLoaderCallback);
        } else {
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (cameraViewer != null)
            cameraViewer.disableView();
    }
}
