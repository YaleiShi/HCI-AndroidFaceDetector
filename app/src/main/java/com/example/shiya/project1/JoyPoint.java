package com.example.shiya.project1;

/**
 * Created by shiya on 2018/2/8.
 */

public class JoyPoint {
    float timeStamp;
    float joy;

    public JoyPoint(float timeStamp, float joy){
        this.timeStamp = timeStamp;
        this.joy = joy;
    }

    public float getTimeStamp(){
        return timeStamp;
    }

    public float getJoy(){
        return  joy;
    }
}
