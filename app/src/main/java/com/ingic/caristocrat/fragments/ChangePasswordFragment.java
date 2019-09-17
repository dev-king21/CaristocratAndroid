package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentChangePasswordBinding;
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
public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener {
    FragmentChangePasswordBinding binding;

    public ChangePasswordFragment() {
    }

    public static ChangePasswordFragment Instance() {
        return new ChangePasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    @Override
    public void onClick(View view) {
        UIHelper.hideSoftKeyboard(mainActivityContext, view);
        switch (view.getId()) {
            case R.id.btnUpdate:
                update();
                break;
            case R.id.ivShowNewPassword:
                Utils.hideShowPassword(binding.ivShowNewPassword, binding.etNewPassword, R.drawable.eye, R.drawable.eye1);
                break;
            case R.id.ivShowCurrentPassword:
                Utils.hideShowPassword(binding.ivShowCurrentPassword, binding.etCurrentPassword, R.drawable.eye, R.drawable.eye1);
                break;
            case R.id.ivShowConfirmNewPassword:
                Utils.hideShowPassword(binding.ivShowConfirmNewPassword, binding.etConfirmNewPassword, R.drawable.eye, R.drawable.eye1);
                break;
        }
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.showTitlebar(mainActivityContext);
        titlebar.setTitle(mainActivityContext.getResources().getString(R.string.change_password));
        titlebar.showBackButton(mainActivityContext, false);
    }

    private void setListeners() {
        binding.btnUpdate.setOnClickListener(this);
        binding.ivShowCurrentPassword.setOnClickListener(this);
        binding.ivShowNewPassword.setOnClickListener(this);
        binding.ivShowConfirmNewPassword.setOnClickListener(this);
        binding.ilCurrentPassword.setErrorEnabled();
        binding.ilNewPassword.setErrorEnabled();
        binding.ilConfirmNewPassword.setErrorEnabled();


    }

     private void update() {
        if (!CustomValidation.validateLength(binding.etCurrentPassword, binding.ilCurrentPassword, mainActivityContext.getResources().getString(R.string.err_password), "1", "20"))
            return;
        if (!CustomValidation.validateLength(binding.etNewPassword, binding.ilNewPassword, mainActivityContext.getResources().getString(R.string.err_password), "1", "20"))
            return;
        if (!CustomValidation.validateLength(binding.etConfirmNewPassword, binding.ilConfirmNewPassword, mainActivityContext.getResources().getString(R.string.err_password), "1", "20"))
            return;
        if (!CustomValidation.validateNewConfirmPassword(binding.etNewPassword, binding.etConfirmNewPassword, binding.ilConfirmNewPassword, mainActivityContext)) {
            return;
        }
        if(binding.etCurrentPassword.getText().toString().trim().equals(binding.etNewPassword.getText().toString().trim())){
            UIHelper.showSnackbar(mainActivityContext.getMainFrameLayout(), mainActivityContext.getResources().getString(R.string.old_pass_new_pass_cannot), Snackbar.LENGTH_LONG);
            return;
        }
        changePasswordCall();
    }

    private void changePasswordCall() {
        Map<String, Object> params = new HashMap<>();
        params.put("old_password", binding.etCurrentPassword.getText().toString().trim());
        params.put("password", binding.etNewPassword.getText().toString().trim());
        params.put("password_confirmation", binding.etConfirmNewPassword.getText().toString().trim());

        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(
                    AppConstants.WebServicesKeys.CHANGE_PASSWORD, null,
                    null,
                    params,
                    new WebApiRequest.WebServiceObjectResponse() {
                        @Override
                        public void onSuccess(ApiResponse apiResponse) {
                            if (!apiResponse.isSuccess()) {
                                Utils.showDetailedErrors(mainActivityContext.getMainFrameLayout(), apiResponse, AppConstants.ErrorsKeys.PASSWORDS);
                                mainActivityContext.hideLoader();
                            } else {
                                UIHelper.showToast(mainActivityContext, apiResponse.getMessage(), Toast.LENGTH_LONG);
                                mainActivityContext.onBackPressed();
                                mainActivityContext.hideLoader();
                            }
                        }

                        @Override
                        public void onError() {
                            mainActivityContext.hideLoader();
                        }
                    }, null
            );
        }
    }
}
