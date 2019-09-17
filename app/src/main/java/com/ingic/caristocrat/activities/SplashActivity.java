package com.ingic.caristocrat.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.databinding.ActivitySplashBinding;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;

/**
 */
public class SplashActivity extends BaseActivity {
    ActivitySplashBinding binding;
    int speedoMeterValue = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
/*
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Log.i("testing", "Bundle received: " + bundle.getInt("actionID"));
            Log.i("testing", "Bundle received: " + bundle.getInt("actionType"));
        }
*/
        setSplashAnimation();

        Utils.printHashKey(SplashActivity.this.getClass().getSimpleName(),this);
    }

    public void setSplashAnimation() {
//        QnUng5+8sZqOXoz2V83sVjSJIKU=
       /* RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setInterpolator(new LinearInterpolator());*/

//        binding.ivNeedle.startAnimation(rotate);

//        binding.shimmerViewContainer.startShimmer();

        new CountDownTimer(3000, 100) {

            public void onTick(long millisUntilFinished) {
                if (speedoMeterValue < 100) {
                    binding.tvSpeedometerValue.setText((speedoMeterValue += 10) + "%");
                }
//                binding.meter.setAngleRange(45,315);
//                CustomAnimationHelpers.customAnimation(Techniques.FadeIn,500,binding.tvSpeedometerValue);
            }

            public void onFinish() {

                if (getPreferenceHelper().getLoginStatus()) {
                    startActivity(MainActivity.class, true);
                } else {
                    if (!getPreferenceHelper().getWalkthrough()) {
                        startActivity(WalkthroughActivity.class, true);
                    } else {
                        startActivity(RegistrationActivity.class, true);
                    }
                }

            }

        }.start();
    }

}
