package com.ingic.caristocrat.fragments;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentForgotPasswordBinding;
import com.ingic.caristocrat.helpers.CustomValidation;
import com.ingic.caristocrat.helpers.DialogFactory;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class ForgotPasswordFragment extends BaseFragment implements View.OnClickListener {
    FragmentForgotPasswordBinding binding;

    public ForgotPasswordFragment() {
    }

    public static ForgotPasswordFragment Instance() {
        return new ForgotPasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                forgetPassword();
                break;
        }
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(registrationActivityContext);
        titlebar.showTitlebar(registrationActivityContext);
        titlebar.showBackButton(registrationActivityContext, false);
    }

    private void forgetPassword() {
        if (!CustomValidation.validateEmail(binding.etEmail, binding.ilEmail, registrationActivityContext.getResources().getString(R.string.err_email)))
            return;
        forgetPasswordCall();
    }

    private void setListeners() {
        binding.btnSubmit.setOnClickListener(this);
        binding.ilEmail.setErrorEnabled();
    }

    private void showDialog() {
        DialogFactory.createSingleButtonDialog(registrationActivityContext, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PhoneVerificationFragment phoneVerificationFragment = new PhoneVerificationFragment();
                phoneVerificationFragment.setEmail(binding.etEmail.getText().toString().trim());
                phoneVerificationFragment.setForgotPassword(true);
                registrationActivityContext.replaceFragment(phoneVerificationFragment, PhoneVerificationFragment.class.getName(), true, true);
                dialogInterface.dismiss();
            }
        }, registrationActivityContext.getResources().getString(R.string.verification_message)).show();
    }

    private void forgetPasswordCall() {
        Map<String, Object> params = new HashMap<>();
        params.put("email", binding.etEmail.getText().toString().trim());
        params.put("is_for_reset", 1);
        disableViewsForSomeSeconds(binding.btnSubmit);
        if (registrationActivityContext.showLoader()) {
            WebApiRequest.Instance(registrationActivityContext).request(
                    AppConstants.WebServicesKeys.FORGET_PASSWORD, null,
                    null,
                    params,
                    new WebApiRequest.WebServiceObjectResponse() {
                        @Override
                        public void onSuccess(ApiResponse apiResponse) {
                            showDialog();
                            registrationActivityContext.hideLoader();
                        }

                        @Override
                        public void onError() {
                            registrationActivityContext.hideLoader();
                        }
                    }, null);
        }
    }

}
