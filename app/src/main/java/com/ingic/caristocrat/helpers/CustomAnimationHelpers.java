package com.ingic.caristocrat.helpers;

import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;



public class CustomAnimationHelpers {


    public static void customAnimation(Techniques techniques, int duration, View view){
        YoYo.with(techniques)
                .duration(duration)
                .repeat(0)
                .playOn(view);
    }
    public static void customSplashAnimation(Techniques techniques, int duration, View view){
        YoYo.with(techniques)
                .duration(duration)
                .repeat(100)
                .playOn(view);
    }

}
