package com.ingic.caristocrat.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.ActivityRegistrationBinding;
import com.ingic.caristocrat.dialogs.CountryPickerDialog;
import com.ingic.caristocrat.fragments.MainDetailPageFragment;
import com.ingic.caristocrat.fragments.SigninFragment;
import com.ingic.caristocrat.fragments.SignupFragment;
import com.ingic.caristocrat.fragments.WalkthroughFragment;
import com.ingic.caristocrat.helpers.SocialLoginHelper;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;
import com.mukesh.countrypicker.CountryPickerListener;

public class RegistrationActivity extends BaseActivity {
    ActivityRegistrationBinding binding;
    public boolean fromGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration);

        fromGuest = getIntent().getBooleanExtra(AppConstants.FROM_GUEST, false);

        setMainFrameLayoutID(binding.mainFrame.getId());
        setMainFrameLayout(binding.mainFrame);
//        replaceFragment(WalkthroughFragment.Instance(), WalkthroughFragment.class.getName(), false, false);

        replaceFragment(SigninFragment.Instance(), SigninFragment.class.getName(), true, false);
        boolean signup = getIntent().getBooleanExtra("signup", false);
        if (signup) {
            replaceFragment(SignupFragment.Instance(true), SignupFragment.class.getName(), true, true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SocialLoginHelper.FB_REQ_CODE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    SocialLoginHelper.Instance().getFaceBookonActivityResult(requestCode, resultCode, data);
                }
                break;
            case SocialLoginHelper.G_REQ_CODE:
                if (data != null) {
                    SocialLoginHelper.Instance().getGoogleonActivityResult(requestCode, resultCode, data);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        UIHelper.hideSoftKeyboard(this);
        if (WebApiRequest.Instance(this).getCallObject() != null) {
            WebApiRequest.Instance(this).getCallObject().cancel();
        } else if (WebApiRequest.Instance(this).getCallArray() != null) {
            WebApiRequest.Instance(this).getCallArray().cancel();
        }
        hideLoader();
        if (getSupportFragmentManager().getBackStackEntryCount() > 1)
            getSupportFragmentManager().popBackStack();
        else {
            finish();
        }
    }

    public Titlebar getTitlebar() {
        return binding.titlebar;
    }

    public boolean showLoader() {
        UIHelper.hideSoftKeyboard(this);
        if (!Utils.isNetworkAvailable(this)) {
            UIHelper.showSnackbar(getMainFrameLayout(), getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG);
            return false;
        } else {
            binding.progressBarContainer.setVisibility(View.VISIBLE);
            return true;
        }
    }

    public boolean internetConnected() {
        UIHelper.hideSoftKeyboard(this);
        if (!Utils.isNetworkAvailable(this)) {
            UIHelper.showSnackbar(getMainFrameLayout(), getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG);
            return false;
        } else {
            return true;
        }
    }

    public void hideLoader() {
        binding.progressBarContainer.setVisibility(View.GONE);
    }

    public void pickCountry(final CountryPickerDialog.OnCountrySelectedListener listener) {
        final CountryPickerDialog dialog = new CountryPickerDialog(getResources().getString(R.string.select_country), new CountryPickerDialog.OnDestroyListener() {
            @Override
            public void onDestroy() {
                UIHelper.hideKeyboard(binding.getRoot(), RegistrationActivity.this);
            }
        });
        dialog.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                dialog.dismiss();
                listener.onCountrySelected(name, code, dialCode, flagDrawableResID);
            }
        });
        dialog.show(getSupportFragmentManager(), CountryPickerDialog.class.getSimpleName());
    }
}
