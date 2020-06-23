package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    final int REAR_CAMERA = 0;
    final int FRONT_CAMERA =  1;
    final int MAX_WIDTH = 320;
    final int MAX_HEIGHT = 240;
    final Size sz = new Size(100, 100);

    static {
        if (!OpenCVLoader.initDebug()) {
            Log.d("TAG", "OpenCv not loaded.");
        } else {
            Log.d("TAG", "OpenCv loaded.");
        }
    }

    JavaCameraView cameraView;
    CascadeClassifier detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initClassifier();
        initCameraView();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initClassifier();
            initCameraView();
        } else {
            requestPermissions(new String[] { Manifest.permission.CAMERA }, 111111);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 111111) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initClassifier();
                initClassifier();
                initCameraView();
            } else {
                Toast.makeText(this, "Application needs camera permission to continue", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initClassifier() {
        try {
            InputStream is = getResources().openRawResource(R.raw.cascade);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File cascadeFile = new File(cascadeDir, "cascade.xml");
            FileOutputStream os = new FileOutputStream(cascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            detector = new CascadeClassifier(cascadeFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCameraView() {
        cameraView = findViewById(R.id.cameraView);
        cameraView.setCameraIndex(REAR_CAMERA);
        cameraView.setCvCameraViewListener(this);
        cameraView.enableView();
        cameraView.enableFpsMeter();
        cameraView.setMaxFrameSize(MAX_WIDTH, MAX_HEIGHT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraView != null) {
            cameraView.disableView();
            cameraView.disableFpsMeter();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) { }

    @Override
    public void onCameraViewStopped() { }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat org_img = inputFrame.rgba();

        MatOfRect objVectors = new MatOfRect();
        detector.detectMultiScale(org_img, objVectors, 1.8, 5);

        if (objVectors.toArray().length == 0) {
            return org_img;
        }

        for (Rect rect : objVectors.toArray()) {
            int x = rect.x;
            int y = rect.y;


            Imgproc.rectangle(org_img, new Point(x, y),
                    new Point(x + rect.width, y + rect.height),
                    new Scalar(0, 255, 0), 3);
        }

        return org_img;
    }
}