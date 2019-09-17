package com.ingic.caristocrat.fragments;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentPhoneVerificationBinding;
import com.ingic.caristocrat.helpers.BasePreferenceHelper;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.UserWrapper;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 */
public class PhoneVerificationFragment extends BaseFragment implements OnClickListener {
    FragmentPhoneVerificationBinding binding;
    CountDownTimer countDownTimer;
    String email, millisInFutureKey = "millisInFutureKey", countDownIntervalKey = "countDownIntervalKey";
    boolean forgotPassword;
    long millisInFuture = 31000, countDownInterval = 1000, millisUntilFinish;

    public PhoneVerificationFragment() {
    }

    @SuppressLint("ValidFragment")
    public PhoneVerificationFragment(String email) {
        this.email = email;
    }

    public static PhoneVerificationFragment Instance() {
        return new PhoneVerificationFragment();
    }

    public static PhoneVerificationFragment Instance(String email) {
        return new PhoneVerificationFragment(email);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_phone_verification, container, false);
        registrationActivityContext.getPreferenceHelper().setIntegerPrefrence(millisInFutureKey, (int) millisInFuture);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTimer();
        setListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            registrationActivityContext.getPreferenceHelper().setIntegerPrefrence(millisInFutureKey, (int) millisUntilFinish);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        setTimer();
        millisInFuture = registrationActivityContext.getPreferenceHelper().getIntegerPrefrence(millisInFutureKey);
        setTimer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null)
            countDownTimer.cancel();
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(registrationActivityContext);
        titlebar.showTitlebar(registrationActivityContext);
        titlebar.showBackButton(registrationActivityContext, false);
    }

    @Override
    public void onClick(View view) {
        UIHelper.hideSoftKeyboard(registrationActivityContext, view);
        switch (view.getId()) {
            case R.id.tvResend:
                resend();
                break;
            case R.id.btnVerify:
                verifyResetCode();
//                registrationActivityContext.replaceFragment(ResetPasswordFragment.Instance(), ResetPasswordFragment.class.getName(), true, true);
                break;

        }
    }

    private void verifyResetCode() {
        if (binding.pinView.getText().toString().trim().length() < AppConstants.PIN_CODE_LENGTH && binding.pinView.getText().toString().trim().length() < AppConstants.PIN_CODE_LENGTH) {
            UIHelper.showSnackbar(registrationActivityContext.getMainFrameLayout(), registrationActivityContext.getResources().getString(R.string.enter_pin_code), Snackbar.LENGTH_LONG);
            return;
        } else {
            verifyResetCodeCall();
        }
    }

    private void resend() {
        binding.tvResend.setVisibility(View.VISIBLE);
        countDownTimer.cancel();
        countDownTimer.start();
        binding.tvResend.setEnabled(false);
        if (email != null) {
            forgetPasswordCall();
        }
    }

    private void setListeners() {

        binding.btnVerify.setOnClickListener(this);
        binding.tvResend.setOnClickListener(this);

    }

    private void setTimer() {
        countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {

            public void onTick(long millisUntilFinished) {
                millisUntilFinish = millisUntilFinished;
                binding.tvTimer.setText("" + String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

            }

            public void onFinish() {
                millisInFuture = 30000;
                binding.tvTimer.setText(registrationActivityContext.getResources().getString(R.string._00_00));
                countDownTimer.cancel();
                binding.tvResend.setEnabled(true);
            }

        };
        countDownTimer.cancel();
        countDownTimer.start();
    }

    private void verifyResetCodeCall() {
        Map<String, Object> params = new HashMap<>();
        params.put("verification_code", binding.pinView.getText().toString().trim());
        if (email != null && !forgotPassword) {
            params.put("email", email);
        }
        if (registrationActivityContext.showLoader()) {
            WebApiRequest.Instance(registrationActivityContext).request(
                    AppConstants.WebServicesKeys.VERIFY_RESET_CODE, null,
                    null,
                    params,
                    new WebApiRequest.WebServiceObjectResponse() {
                        @Override
                        public void onSuccess(ApiResponse apiResponse) {
                            if (!forgotPassword) {
                                UserWrapper userWrapper = (UserWrapper) JsonHelpers.convertToModelClass(apiResponse.getData(), UserWrapper.class);
                                preferenceHelper.putUserToken(userWrapper.getUser().getAccessToken());
                                if (registrationActivityContext.internetConnected()) {
                                    WebApiRequest.Instance(registrationActivityContext).request(AppConstants.WebServicesKeys.REFRESH, null, null, null, new WebApiRequest.WebServiceObjectResponse() {
                                        @Override
                                        public void onSuccess(ApiResponse apiResponse) {
                                            UserWrapper userWrapper = (UserWrapper) JsonHelpers.convertToModelClass(apiResponse.getData(), UserWrapper.class);
                                            preferenceHelper.putUser(userWrapper.getUser());
                                            preferenceHelper.setBooleanPrefrence(BasePreferenceHelper.KEY_ENABLE_NOTIFICATIONS, userWrapper.getUser().getPush_notification() == 1 ? true : false);
                                            preferenceHelper.putUserToken(userWrapper.getUser().getAccessToken());
                                            registrationActivityContext.getPreferenceHelper().setLoginStatus(true);
//                            UIHelper.showToast(registrationActivityContext, apiResponse.getMessage(), Toast.LENGTH_LONG);
                                            registrationActivityContext.startActivity(MainActivity.class, true);
                                            registrationActivityContext.hideLoader();
                                        }

                                        @Override
                                        public void onError() {
                                            registrationActivityContext.hideLoader();
                                        }
                                    }, null);
                                }
                            } else {
                                UIHelper.showToast(registrationActivityContext, apiResponse.getMessage(), Snackbar.LENGTH_SHORT);
                                UserWrapper userWrapper = (UserWrapper) JsonHelpers.convertToModelClass(apiResponse.getData(), UserWrapper.class);
                                registrationActivityContext.replaceFragment(ResetPasswordFragment.Instance(userWrapper.getUser().getEmail(), binding.pinView.getText().toString().trim()), ResetPasswordFragment.class.getName(), true, true);
                                registrationActivityContext.hideLoader();
                            }
                        }

                        @Override
                        public void onError() {
                            registrationActivityContext.hideLoader();
                        }
                    }, null);
        }
    }

    private void forgetPasswordCall() {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        if (forgotPassword) {
            params.put("is_for_reset", 1);
        } else {
            params.put("is_for_reset", 0);
        }
        if (registrationActivityContext.showLoader()) {
            WebApiRequest.Instance(registrationActivityContext).request(
                    AppConstants.WebServicesKeys.FORGET_PASSWORD, null,
                    null,
                    params,
                    new WebApiRequest.WebServiceObjectResponse() {
                        @Override
                        public void onSuccess(ApiResponse apiResponse) {
                            UIHelper.showToast(registrationActivityContext, apiResponse.getMessage(), Toast.LENGTH_SHORT);
                            registrationActivityContext.hideLoader();
                            binding.tvResend.setEnabled(false);
                        }

                        @Override
                        public void onError() {
                            registrationActivityContext.hideLoader();
                        }
                    }, null);
        }
    }

    @Override
    public void onDestroy() {
        countDownTimer.cancel();
        super.onDestroy();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setForgotPassword(boolean forgotPassword) {
        this.forgotPassword = forgotPassword;
    }
}
