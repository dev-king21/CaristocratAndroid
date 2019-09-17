package com.ingic.caristocrat.fragments;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentResetPasswordBinding;
import com.ingic.caristocrat.helpers.CustomValidation;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class ResetPasswordFragment extends BaseFragment implements View.OnClickListener {
    FragmentResetPasswordBinding binding;
    private String email, pincode;

    public ResetPasswordFragment() {
    }

    @SuppressLint("ValidFragment")
    public ResetPasswordFragment(String email, String pincode) {
        this.email = email;
        this.pincode = pincode;
    }

    public static ResetPasswordFragment Instance(String email, String pincode) {
        return new ResetPasswordFragment(email, pincode);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reset_password, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
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
            case R.id.btnSubmit:
                reset();
                break;
            case R.id.ivShowPassword:
                Utils.hideShowPassword(binding.ivShowPassword, binding.etPassword, R.drawable.eye, R.drawable.eye1);
                break;
            case R.id.ivShowConfirmPassword:
                Utils.hideShowPassword(binding.ivShowConfirmPassword, binding.etConfirmPassword, R.drawable.eye, R.drawable.eye1);

                break;
        }
    }

    private void setListeners() {
        binding.btnSubmit.setOnClickListener(this);
        binding.ivShowPassword.setOnClickListener(this);
        binding.ivShowConfirmPassword.setOnClickListener(this);
        binding.ilConfirmPassword.setErrorEnabled();
        binding.ilPassword.setErrorEnabled();
    }

    private void reset() {
        if (!CustomValidation.validateLength(binding.etPassword, binding.ilPassword, registrationActivityContext.getResources().getString(R.string.err_password), "1", "20"))
            return;

        if (!CustomValidation.validateLength(binding.etConfirmPassword, binding.ilConfirmPassword, registrationActivityContext.getResources().getString(R.string.err_password), "1", "20"))
            return;

        if (!CustomValidation.validateNewConfirmPassword(binding.etPassword, binding.etConfirmPassword, binding.ilConfirmPassword, registrationActivityContext))
            return;

        resetPasswordCall();

    }

    private void resetPasswordCall() {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("verification_code", pincode);
        params.put("password", binding.etPassword.getText().toString().trim());
        params.put("password_confirmation", binding.etConfirmPassword.getText().toString().trim());

        if (registrationActivityContext.showLoader()) {
            WebApiRequest.Instance(registrationActivityContext).request(
                    AppConstants.WebServicesKeys.RESET_PASSWORD, null,
                    null,
                    params,
                    new WebApiRequest.WebServiceObjectResponse() {
                        @Override
                        public void onSuccess(ApiResponse apiResponse) {
                            if (!apiResponse.isSuccess()) {
                                Utils.showDetailedErrors(registrationActivityContext.getMainFrameLayout(), apiResponse, AppConstants.ErrorsKeys.PASSWORDS);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.showDetailedErrors(registrationActivityContext.getMainFrameLayout(), apiResponse, AppConstants.ErrorsKeys.CONFIRM_PASSWORDS);
                                    }
                                }, AppConstants.SPLASH_DURATION);
                                registrationActivityContext.hideLoader();
                            } else {
                                UIHelper.showToast(registrationActivityContext, apiResponse.getMessage(), Toast.LENGTH_LONG);
                                registrationActivityContext.clearBackStack();
                                registrationActivityContext.replaceFragment(SigninFragment.Instance(), SigninFragment.class.getName(), true, false);
                                registrationActivityContext.hideLoader();
                            }
                        }

                        @Override
                        public void onError() {
                            registrationActivityContext.hideLoader();
                        }
                    }, null
            );
        }
    }
}
