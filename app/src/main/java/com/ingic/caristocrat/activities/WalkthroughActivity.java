package com.ingic.caristocrat.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.WalkthroughAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.ActivityWalkthroughBinding;
import com.ingic.caristocrat.fragments.SigninFragment;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.interfaces.WalkthroughActionListener;
import com.ingic.caristocrat.models.WalkthroughWrapper;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;

public class WalkthroughActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    ActivityWalkthroughBinding binding;
    ArrayList<WalkthroughWrapper> walkthroughWrappers;
    WalkthroughAdapter adapter;
    int slidePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_walkthrough);

        getPreferenceHelper().setWalkthrough(true);

        adapter = new WalkthroughAdapter(this);
        binding.viewPager.setOffscreenPageLimit(0);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOnPageChangeListener(this);
        binding.btnSignUp.setOnClickListener(this);
        binding.btnNext.setOnClickListener(this);
        binding.tvSkipTop.setOnClickListener(this);
        binding.tvSkipBottom.setOnClickListener(this);
        binding.tvSkipBottom.setOnClickListener(this);
        binding.tvClickHere.setOnClickListener(this);

        if (walkthroughWrappers == null) {
            getWalkthroughContent();
        }
    }

    private void getWalkthroughContent() {
        WebApiRequest.Instance(this).request(AppConstants.WebServicesKeys.WALKTHROUGH_DETAIIL, null, null, null, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                walkthroughWrappers = (ArrayList<WalkthroughWrapper>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), WalkthroughWrapper.class);
                if (walkthroughWrappers != null && walkthroughWrappers.size() > 0) {
//                    walkthroughWrappers.remove(walkthroughWrappers.size() - 1);
                    adapter.addAll(walkthroughWrappers);
                    adapter.notifyDataSetChanged();
                    if (walkthroughWrappers.size() == 1) {
                        binding.tvSkipBottom.setVisibility(View.GONE);
                        binding.tvSkipTop.setVisibility(View.VISIBLE);
                        binding.btnNext.setVisibility(View.GONE);
                        binding.btnSignUp.setVisibility(View.VISIBLE);
                        binding.llAlreadyMember.setVisibility(View.VISIBLE);
                    }
                } else {
                    RegistrationActivity registrationActivity = new RegistrationActivity();
                    startActivity(registrationActivity.getClass(), true);
                }
                binding.progressBarContainer.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                binding.progressBarContainer.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.slidePosition = position;
/*
        if (adapter.getViewPlayer() != null) {
            adapter.getViewPlayer().stopPlayback();
            adapter.getViewPlayer().destroyDrawingCache();
        }
*/
        if (position == (walkthroughWrappers.size() - 1)) {
            binding.tvSkipBottom.setVisibility(View.GONE);
            binding.tvSkipTop.setVisibility(View.VISIBLE);
            binding.btnNext.setVisibility(View.GONE);
            binding.btnSignUp.setVisibility(View.VISIBLE);
            binding.llAlreadyMember.setVisibility(View.VISIBLE);
        } else {
            binding.tvSkipBottom.setVisibility(View.VISIBLE);
            binding.tvSkipTop.setVisibility(View.GONE);
            binding.btnNext.setVisibility(View.VISIBLE);
            binding.btnSignUp.setVisibility(View.GONE);
            binding.llAlreadyMember.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                this.slidePosition++;
                binding.viewPager.setCurrentItem(this.slidePosition);
                break;

            case R.id.btnSignUp:
                Intent intent = new Intent(this, RegistrationActivity.class);
                intent.putExtra("signup", true);
                startActivity(intent);
                finish();
                break;

            case R.id.tvSkipTop:
            case R.id.tvSkipBottom:
            case R.id.tvClickHere:
                RegistrationActivity registrationActivity = new RegistrationActivity();
                startActivity(registrationActivity.getClass(), true);
                break;
        }
    }
}
