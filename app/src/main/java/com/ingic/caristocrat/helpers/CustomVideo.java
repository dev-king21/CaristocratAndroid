package com.ingic.caristocrat.helpers;

import android.content.Context;
import android.util.AttributeSet;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;

public class CustomVideo extends FullscreenVideoLayout {
    public CustomVideo(Context context) {
        super(context);
    }

    public CustomVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void hideFullscreen(Boolean status) {
        if (status)
            imgfullscreen.setVisibility(INVISIBLE);
    }

    public void updateControler(){
        imgplay.setBackground(context.getResources().getDrawable(com.github.rtoshiro.view.video.R.drawable.fvl_selector_play));
    }
}
