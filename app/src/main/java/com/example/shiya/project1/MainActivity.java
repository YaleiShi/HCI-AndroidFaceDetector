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

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements Detector.ImageListener, CameraDetector.CameraEventListener {
    //1
    TextView tv1;
    TextView tv2;
    TextView tv3;
    GifImageView gif;
    SurfaceView cameraDetectorSurfaceView;
    CameraDetector cameraDetector;
    ArrayList<JoyPoint> joyList = new ArrayList<JoyPoint>();
    ArrayList<JoyPoint> joyListTime = new ArrayList<JoyPoint>();

    //2
    int maxProcessingRate = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //3
        gif = (GifImageView) findViewById(R.id.gif);
        tv1 = (TextView) findViewById(R.id.textViewA);
        tv2 = (TextView) findViewById(R.id.textViewB);
        tv3 = (TextView) findViewById(R.id.textViewC);
        cameraDetectorSurfaceView = (SurfaceView) findViewById(R.id.cameraDetectorSurfaceView);

        //4
        cameraDetector = new CameraDetector(this, CameraDetector.CameraType.CAMERA_FRONT, cameraDetectorSurfaceView);

        //5
        cameraDetector.setMaxProcessRate(maxProcessingRate);

        //6
        cameraDetector.setImageListener(this);
        cameraDetector.setOnCameraEventListener(this);

        //7
        cameraDetector.setDetectAllEmotions(true);

        //8
        cameraDetector.start();
    }

    @Override
    public void onImageResults(List<Face> faces, Frame frame, float timeStamp) {
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

        JoyPoint jp = new JoyPoint(timeStamp, joy);

        if(joyList.size() < 100){
            joyList.add(jp);
        }else {
            joyList.remove(0);
            joyList.add(jp);

            float averageSMA = averageSMA(joyList);
            float averageWMA = averageWMA(joyList);
            if (averageSMA >= 20){
                tv1.setText("You have reached the threshold using SMA\n");
            }else{
                tv1.setText("Why not give me a smile?\n");
            }
            if(averageWMA >= 20){
                gif.setImageResource(R.drawable.timg);
                tv2.setText("You have reached the threshold using WMA\n");
            }else{
                gif.setImageResource(R.drawable.timg2);
                tv2.setText("Why not give me a smile?\n");
            }
        }

        if(joyListTime.isEmpty()){
            joyListTime.add(jp);
        }else {
            JoyPoint jp0 = joyListTime.get(0);
            float timeStamp0 = jp0.getTimeStamp();
            float dif = timeStamp - timeStamp0;
            if(dif < 10){
                joyListTime.add(jp);
            }else{
                joyListTime.remove(0);
                joyListTime.add(jp);
                float averageSMAT = averageSMA(joyListTime);
                if (averageSMAT >= 20){
                    tv3.setText("You have reached the threshold using SMA using timestamps\n");
                }else{
                    tv3.setText("Why not give me a smile?\n");
                }
            }
        }

        //5
        System.out.println("Joy: " + joy);
        System.out.println("Anger: " + anger);
        System.out.println("Surprise: " + surprise);
    }


    @Override
    public void onCameraSizeSelected(int cameraHeight, int cameraWidth, Frame.ROTATE rotation) {

        //1
        ViewGroup.LayoutParams params = cameraDetectorSurfaceView.getLayoutParams();

        //2
        params.height = cameraHeight;
        params.width = cameraWidth;

        //3
        cameraDetectorSurfaceView.setLayoutParams(params);
    }

    public float averageSMA(ArrayList<JoyPoint> al){
        float sum = 0;
        for(JoyPoint jp: al){
            float f = jp.getJoy();
            sum = sum + f;
        }
        float result = sum / al.size();
        return result;
    }

    public float averageWMA(ArrayList<JoyPoint> al){
        float sum = 0;
        int weight = 1;
        int weightSum = 0;
        for(JoyPoint jp: al){
            float f = jp.getJoy();
            sum = sum + f * weight;
            weightSum = weightSum + weight;
            weight++;
        }
        float result = sum / weightSum;
        return result;
    }

}
