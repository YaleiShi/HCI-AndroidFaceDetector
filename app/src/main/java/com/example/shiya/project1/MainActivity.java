package com.example.shiya.project1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements Detector.ImageListener, CameraDetector.CameraEventListener {
    //1
    TextView tv;
    GifImageView gif;
    SurfaceView cameraDetectorSurfaceViewFront;
    SurfaceView cameraDetectorSurfaceViewBack;
    CameraDetector cameraDetectorFront;
    CameraDetector cameraDetectorBack;

    //2
    int maxProcessingRate = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //3
        cameraDetectorSurfaceViewFront = (SurfaceView) findViewById(R.id.cameraDetectorSurfaceView);
        cameraDetectorSurfaceViewBack = (SurfaceView) findViewById(R.id.cameraDetectorSurfaceView2);
        tv = (TextView) findViewById(R.id.textView);

        //4
        cameraDetectorFront = new CameraDetector(this, CameraDetector.CameraType.CAMERA_FRONT, cameraDetectorSurfaceViewFront);
        cameraDetectorBack = new CameraDetector(this, CameraDetector.CameraType.CAMERA_BACK, cameraDetectorSurfaceViewBack);

        //5
        cameraDetectorFront.setMaxProcessRate(maxProcessingRate);
        cameraDetectorBack.setMaxProcessRate(maxProcessingRate);

        //6
        cameraDetectorFront.setImageListener(this);
        cameraDetectorFront.setOnCameraEventListener(this);
        cameraDetectorBack.setImageListener(this);
        cameraDetectorBack.setOnCameraEventListener(this);

        //7
        cameraDetectorFront.setDetectAllEmotions(true);
        cameraDetectorFront.setDetectAllExpressions(true);
        cameraDetectorBack.setDetectAllEmotions(true);
        cameraDetectorBack.setDetectAllExpressions(true);
        //8
//        cameraDetectorFront.start();
        cameraDetectorBack.start();
    }

    @Override
    public void onImageResults(List<Face> faces, Frame frame, float timeStamp) {
        tv.setText("There are " + faces.size() + " faces!");
        //1
        if (faces == null)
            return; //frame was not processed

        //2
        if (faces.size() == 0)
            return; //no face found

        //3
        Face face = faces.get(0);

        //4
        float joy = face.emotions.getJoy();
        float anger = face.emotions.getAnger();
        float surprise = face.emotions.getSurprise();

        //5
        System.out.println("Joy: " + joy);
        System.out.println("Anger: " + anger);
        System.out.println("Surprise: " + surprise);
    }


    @Override
    public void onCameraSizeSelected(int cameraHeight, int cameraWidth, Frame.ROTATE rotation) {

        //1
        ViewGroup.LayoutParams params = cameraDetectorSurfaceViewFront.getLayoutParams();
        ViewGroup.LayoutParams params2 = cameraDetectorSurfaceViewBack.getLayoutParams();

        //2
        params.height = cameraHeight;
        params.width = cameraWidth;
        params2.height = cameraHeight;
        params2.width = cameraWidth;

        //3
        cameraDetectorSurfaceViewFront.setLayoutParams(params);
        cameraDetectorSurfaceViewBack.setLayoutParams(params2);
    }

}
