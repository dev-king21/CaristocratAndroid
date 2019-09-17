package com.ingic.caristocrat.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.adapters.ContactUsAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.LayoutRequestConsultancyBinding;
import com.ingic.caristocrat.helpers.CustomValidation;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.OnRequestConsultancy;
import com.ingic.caristocrat.interfaces.SimpleDialogActionListener;
import com.ingic.caristocrat.models.ContactForm;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.RequestWrapper;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestConsultancyDialogFragment extends DialogFragment implements View.OnClickListener, OnRequestConsultancy {
    MainActivity activityContext;
    LayoutRequestConsultancyBinding binding;
    OnRequestConsultancy requestConsultancyListener;
    Titlebar titlebar;
    int type, bankId;
    ArrayList<RequestWrapper> requestWrappers;
    TradeCar tradeCar;

    ContactUsAdapter contactUsAdapter;
    private ArrayList<ContactForm> arrayList;

    public static RequestConsultancyDialogFragment Instance(MainActivity activityContext, OnRequestConsultancy requestConsultancyListener) {
        return new RequestConsultancyDialogFragment(activityContext, requestConsultancyListener);
    }

    public RequestConsultancyDialogFragment() {
    }

    @SuppressLint("ValidFragment")
    public RequestConsultancyDialogFragment(MainActivity activityContext, OnRequestConsultancy requestConsultancyListener) {
        this.activityContext = activityContext;
        this.requestConsultancyListener = requestConsultancyListener;
        this.titlebar = activityContext.getTitlebar();
        this.arrayList = new ArrayList<>();
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTradeCar(TradeCar tradeCar) {
        this.tradeCar = tradeCar;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_request_consultancy, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        binding.tvConsultancyFormTitle.setText(type == AppConstants.ContactType.MY_SHOPPER ? "Personal Shopper" : "Ask For Consultancy");
        getRequestConsultancyDetail();
        setTitlebar(titlebar);
        setListeners();

        if (activityContext.getPreferenceHelper().getLoginStatus()) {
            if (activityContext.getPreferenceHelper().getUser() != null) {
                binding.etName.setText(activityContext.getPreferenceHelper().getUser().getName());
                binding.etEmail.setText(activityContext.getPreferenceHelper().getUser().getEmail());
                if (activityContext.getPreferenceHelper().getUser().getDetails() != null) {
                    binding.tvCodePicker.setText(activityContext.getPreferenceHelper().getUser().getDetails().getCountryCode());
                    binding.etNumber.setText(activityContext.getPreferenceHelper().getUser().getDetails().getPhone());
                }
            }
        }

        contactUsAdapter = new ContactUsAdapter(activityContext, this);
        binding.expandedMenu.setAdapter(contactUsAdapter);

        return binding.getRoot();
    }

    private void getRequestConsultancyDetail() {
        activityContext.showLoader();
        WebApiRequest.Instance(activityContext).request(AppConstants.WebServicesKeys.REQUEST_CONSULTANCY_DETAIL, binding.getRoot(), null, null, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                requestWrappers = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), RequestWrapper.class);
                /*
                if (type == AppConstants.ContactType.MY_SHOPPER) {
                    binding.tvConsultancyFormDescription.setText(requestWrappers.get(0).getPersonalShopper());
                } else if (type == AppConstants.ContactType.CONSULTANCY) {
                    binding.tvConsultancyFormDescription.setText(requestWrappers.get(0).getAskForConsultancy());
                }
                */
                ContactForm personalShopper = new ContactForm();
                personalShopper.setId(1);
                personalShopper.setTypeId(AppConstants.ContactType.MY_SHOPPER);
                personalShopper.setTitle(activityContext.getResources().getString(R.string.personal_shopper));
                if (requestWrappers != null && requestWrappers.size() > 0 && requestWrappers.get(0).getPersonalShopper() != null) {
                    personalShopper.setContent(requestWrappers.get(0).getPersonalShopper());
                }
                if (activityContext.getPreferenceHelper().getLoginStatus()) {
                    if (activityContext.getPreferenceHelper().getUser() != null) {
                        personalShopper.setUser(activityContext.getPreferenceHelper().getUser());
                    }
                }
                arrayList.add(personalShopper);

                ContactForm askForConsultancy = new ContactForm();
                askForConsultancy.setId(2);
                askForConsultancy.setTypeId(AppConstants.ContactType.CONSULTANCY);
                askForConsultancy.setTitle(activityContext.getResources().getString(R.string.ask_for_consultancy));
                if (requestWrappers != null && requestWrappers.size() > 0 && requestWrappers.get(0).getAskForConsultancy() != null) {
                    askForConsultancy.setContent(requestWrappers.get(0).getAskForConsultancy());
                }
                if (activityContext.getPreferenceHelper().getLoginStatus()) {
                    if (activityContext.getPreferenceHelper().getUser() != null) {
                        askForConsultancy.setUser(activityContext.getPreferenceHelper().getUser());
                    }
                }
                arrayList.add(askForConsultancy);

                contactUsAdapter.addAll(arrayList);
                contactUsAdapter.notifyDataSetChanged();
/*
                if (type == AppConstants.ContactType.MY_SHOPPER) {
                    binding.expandedMenu.expandGroup(0);
                } else {
                    binding.expandedMenu.expandGroup(1);
                }
*/
                binding.expandedMenu.expandGroup(0);
                binding.expandedMenu.expandGroup(1);
                activityContext.hideLoader();
            }

            @Override
            public void onError() {
                activityContext.hideLoader();
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibBackbtn:
                UIHelper.hideSoftKeyboard(activityContext);
                activityContext.onBackPressed();
                break;

            case R.id.btnSubmitConsultancyRequest:
                submitConsultancyRequest();
                break;

            case R.id.tvCodePicker:
                codePicker();
                break;
        }
    }

    private void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(activityContext);
        titlebar.showTitlebar(activityContext);
        titlebar.setTitle(activityContext.getResources().getString(R.string.ask_for_consultancy));
        titlebar.showBackButton(activityContext, false).setOnClickListener(this);
    }

    private void setListeners() {
        binding.btnSubmitConsultancyRequest.setOnClickListener(this);
        binding.tvCodePicker.setOnClickListener(this);
        binding.ilEmail.setErrorEnabled();
        binding.ilName.setErrorEnabled();
        binding.ilPhoneNumber.setErrorEnabled();
    }

    private void codePicker() {
        activityContext.pickCountry(new CountryPickerDialog.OnCountrySelectedListener() {
            @Override
            public void onCountrySelected(String name, String code, String dialCode, int flagDrawableResID) {
                binding.tvCodePicker.setText(dialCode.trim());
            }
        });
    }

    private void submitConsultancyRequest() {
        String name, email, code = null, number = null;
        if (!CustomValidation.validateLength(binding.etName, binding.ilName, activityContext.getResources().getString(R.string.err_full_name), "3", "50"))
            return;

        name = binding.etName.getText().toString().trim();

        if (!CustomValidation.validateEmail(binding.etEmail, binding.ilEmail, activityContext.getResources().getString(R.string.err_email)))
            return;

        email = binding.etEmail.getText().toString().trim();

        if (binding.etNumber.getText().toString().length() > 0) {
            if (binding.tvCodePicker.getText().toString().length() == 0) {
                UIHelper.showToast(activityContext, activityContext.getResources().getString(R.string.err_code), Toast.LENGTH_SHORT);
                return;
            }

            code = binding.tvCodePicker.getText().toString().trim();

            if (!CustomValidation.validateLength(binding.etNumber, binding.ilPhoneNumber, activityContext.getResources().getString(R.string.err_phone), "7", "20"))
                return;

            number = binding.etNumber.getText().toString().trim();
        }

        binding.btnSubmitConsultancyRequest.setEnabled(false);
        this.requestConsultancyListener.onRequested(email, name, code, number, 0, "");
    }

    @Override
    public void onRequested(String email, String name, String countryCode, String phone, int type, String message) {
        sendRequestConsultancy(email, name, countryCode, phone, type, message);
    }

    private void sendRequestConsultancy(String email, String name, String countryCode, String phone, int type, String message) {
        Map<String, Object> params = new HashMap<>();
        if (tradeCar != null) {
            params.put("car_id", tradeCar.getId());
        }
        if (bankId > 0) {
            params.put("bank_id", bankId);
        }
        params.put("email", email);
        params.put("name", name);
        if (countryCode != null) {
            params.put("country_code", countryCode);
        }
        if (phone != null) {
            params.put("phone", phone);
        }
        params.put("type", type);
        params.put("message", message);

        if (activityContext.showLoader()) {
            WebApiRequest.Instance(activityContext).request(AppConstants.WebServicesKeys.REQUEST_CONSULTANCY, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    activityContext.hideLoader();
                    UIHelper.showSimpleDialog(
                            activityContext,
                            0,
                            activityContext.getResources().getString(R.string.success_ex),
                            activityContext.getResources().getString(R.string.received_your_details),
                            activityContext.getResources().getString(R.string.ok_go_back),
                            null,
                            false,
                            false,
                            new SimpleDialogActionListener() {
                                @Override
                                public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                                    dialog.dismiss();
                                    activityContext.onBackPressed();
                                }
                            }
                    );
                }

                @Override
                public void onError() {
                    activityContext.hideLoader();
                }
            }, null);
        }

    }
}
