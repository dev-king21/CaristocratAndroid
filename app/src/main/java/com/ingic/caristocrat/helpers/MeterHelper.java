package com.ingic.caristocrat.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ingic.caristocrat.R;

import de.nitri.gauge.Gauge;

public class MeterHelper extends Gauge{

    public MeterHelper(Context context) {
        super(context);
    }

    public MeterHelper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeterHelper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}