package com.ingic.caristocrat.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentEditProfileBinding;
import com.ingic.caristocrat.dialogs.CountryPickerDialog;
import com.ingic.caristocrat.dialogs.NationalityDialog;
import com.ingic.caristocrat.helpers.CustomValidation;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.MediaTypePicker;
import com.ingic.caristocrat.models.MyImageFile;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.User;
import com.ingic.caristocrat.webhelpers.models.UserWrapper;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 */
public class ProfileEditFragment extends BaseFragment implements View.OnClickListener, MediaTypePicker {
    FragmentEditProfileBinding binding;
    User user;
    private String filePath;
    private MultipartBody.Part imageBody;
    private int genderType = AppConstants.Gender.NOGENDER;

    public ProfileEditFragment() {
    }

    public static ProfileEditFragment Instance() {
        return new ProfileEditFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
        setProfile();
//        mainActivityContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.setTitle(mainActivityContext.getResources().getString(R.string.edit_profile));
        titlebar.showBackButton(mainActivityContext, false);
    }

    @Override
    public void onClick(View view) {
        UIHelper.hideSoftKeyboard(mainActivityContext, view);
        switch (view.getId()) {
            case R.id.btnAddCar:
                update();
                break;

            case R.id.tvCodePicker:
                openCodePicker();
                break;

            case R.id.ivProfile:
                mainActivityContext.openImagePicker(ProfileEditFragment.this, AppConstants.SELECT_IMAGE_COUNT, 0);
                break;

            case R.id.tvAge:
                getDateOfBirth();
                break;

            case R.id.etNationality:
                selectNationality();
                break;
        }
    }

    @Override
    public void onPhotoClicked(ArrayList<MyImageFile> file) {
        if (file != null && file.size() > 0) {
            binding.ivProfile.setBackground(null);
            filePath = file.get(0).getAbsolutePath();
            UIHelper.setUserImageWithGlide(mainActivityContext, binding.ivProfile, file.get(0).getAbsolutePath());
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        binding.tvCodePicker.setOnClickListener(this);
        binding.btnAddCar.setOnClickListener(this);
        binding.ivProfile.setOnClickListener(this);
        binding.tvAge.setOnClickListener(this);
        binding.etNationality.setOnClickListener(this);
        binding.ilEmail.setErrorEnabled();
        binding.ilName.setErrorEnabled();
        binding.ilPhoneNumber.setErrorEnabled();
        binding.ilNationality.setErrorEnabled();
        binding.ilProfession.setErrorEnabled();
        binding.ilAbout.setErrorEnabled();
        binding.etAbout.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent event) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
//                if (view.getId() ==R.id.DwEdit) {
//                }
                return false;
            }
        });

        binding.etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    binding.tvCodePicker.setText("");
                }
            }
        });

        binding.radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                switch (checkedRadioButton.getId()) {
                    case R.id.radioMale:
                        male();
                        break;

                    case R.id.radioFemale:
                        female();
                        break;
                }
            }
        });
    }

    private void update() {
        if (!CustomValidation.validateLength(binding.etName, binding.ilName, mainActivityContext.getResources().getString(R.string.err_full_name), "3", "50"))
            return;

        if (!CustomValidation.validateEmail(binding.etEmail, binding.ilEmail, mainActivityContext.getResources().getString(R.string.err_email)))
            return;

        if (binding.etNumber.getText().toString().length() > 0) {
            if(binding.tvCodePicker.getText().toString().length() == 0){
                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.err_code), Toast.LENGTH_SHORT);
                return;
            }

            if (!CustomValidation.validateLength(binding.etNumber, binding.ilPhoneNumber, mainActivityContext.getResources().getString(R.string.err_phone), "7", "20"))
                return;
        }
//        if (binding.etNumber.getText().toString().length() > 0 || binding.tvCodePicker.getText().toString().length() > 0) {
//            if (!CustomValidation.validateLength(binding.etNumber, binding.ilPhoneNumber, mainActivityContext.getResources().getString(R.string.err_phone), "7", "20"))
//                return;
//        }
//
//        if (UIHelper.isEmptyOrNull(binding.tvCodePicker.getText().toString()) && binding.etNumber.getText().toString().length() >= 5) {
//            // UIHelper.showToast(registrationActivityContext, registrationActivityContext.getResources().getString(R.string.err_code), Toast.LENGTH_SHORT);
//            UIHelper.showSnackbar(getView(), mainActivityContext.getResources().getString(R.string.err_code), Toast.LENGTH_SHORT);
////            Toasty.error(mainActivityContext, mainActivityContext.getResources().getString(R.string.err_code)).show();
//            return;
//        }

//        if (binding.tvAge.getText().toString().length() == 0) {
//            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.select_date_of_birth), Toast.LENGTH_SHORT);
//            return;
//        }
//
//        if (binding.etNationality.getText().toString().length() == 0) {
//            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.select_nationality), Toast.LENGTH_SHORT);
//            return;
//        }
//
//        if (!CustomValidation.validateLength(binding.etProfession, binding.ilProfession, mainActivityContext.getResources().getString(R.string.enter_profession), "3", "255")) {
//            return;
//        }
//
//        if (genderType == 0) {
//            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.select_gender), Toast.LENGTH_SHORT);
//            return;
//        }
        /*
        if (binding.etAbout.getText().toString().length() > 0) {
            if (!CustomValidation.validateLength(binding.etAbout, binding.ilAbout, mainActivityContext.getResources().getString(R.string.err_empty_field), "1", "250"))
                return;
        }
        */
        updateProfileCall();
    }

    private void updateProfileCall() {

        if (filePath != null && !filePath.equals("")) {
            imageBody = MultipartBody.Part.createFormData("image", new File(filePath).getName(), convertFile(filePath));
        }
        Map<String, Object> params = new HashMap<>();
        params.put("name", binding.etName.getText().toString().trim());
        params.put("country_code", binding.tvCodePicker.getText().toString().trim());
        params.put("phone", binding.etNumber.getText().toString().trim());
        params.put("about", binding.etAbout.getText().toString().trim());
        params.put("gender", genderType);
        params.put("dob", binding.tvAge.getText().toString().trim());
        params.put("profession", binding.etProfession.getText().toString().trim());
        params.put("nationality", binding.etNationality.getText().toString().trim());
        params.put("image", imageBody);

        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.UPDATE_PROFILE, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    UserWrapper responseUser = (UserWrapper) JsonHelpers.convertToModelClass(apiResponse.getData(), UserWrapper.class);
                    UIHelper.showSnackbar(mainActivityContext.getMainFrameLayout(), apiResponse.getMessage(), Toast.LENGTH_SHORT);
                    User user = preferenceHelper.getUser();
                    user.setDetails(responseUser.getUser().getDetails());

                    preferenceHelper.putUser(user);
                    mainActivityContext.hideLoader();
                    mainActivityContext.onBackPressed();
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            }, null);
        }
    }

    private void openCodePicker() {
        mainActivityContext.pickCountry(new CountryPickerDialog.OnCountrySelectedListener() {
            @Override
            public void onCountrySelected(String name, String code, String dialCode, int flagDrawableResID) {
                binding.tvCodePicker.setText(dialCode);
            }
        });
    }

    private void setProfile() {
        user = preferenceHelper.getUser();
        if (user != null) {
            binding.etEmail.setText((user.getEmail() == null) ? "" : user.getEmail());
            binding.etEmail.setEnabled(false);
            if (user.getDetails() != null) {
                binding.etName.setText((user.getDetails().getFirstName() == null) ? "" : user.getDetails().getFirstName());
                UIHelper.setUserImageWithGlide(mainActivityContext, binding.ivProfile, user.getDetails().getImageUrl() == null ? "" : user.getDetails().getImageUrl());
                binding.etAbout.setText((user.getDetails().getAbout() == null) ? "" : user.getDetails().getAbout());
                if (user.getDetails().getCountryCode() != null) {
                    binding.tvCodePicker.setText(user.getDetails().getCountryCode().equals("0") ? AppConstants.defaultCountrycode : user.getDetails().getCountryCode());
                } else {
                    binding.tvCodePicker.setHint(mainActivityContext.getResources().getString(R.string.code));
                }
                if (user.getDetails().getPhone() != null) {
                    binding.etNumber.setText(user.getDetails().getPhone());
                }
                if (user.getDetails().getDob() != null) {
                    binding.tvAge.setText(user.getDetails().getDob());
                }
                if (user.getDetails().getNationality() != null) {
                    binding.etNationality.setText(user.getDetails().getNationality());
                }
                if (user.getDetails().getProfession() != null) {
                    binding.etProfession.setText(user.getDetails().getProfession());
                }

                switch (user.getDetails().getGender()) {
                    case AppConstants.Gender.MALE:
                        binding.radioMale.setChecked(true);
                        break;
                    case AppConstants.Gender.FEMALE:
                        binding.radioFemale.setChecked(true);
                        break;
                }
            }
        }
    }

    private void getDateOfBirth() {
        SelectBirthDate selectBirthDate = new SelectBirthDate(binding.tvAge);
        selectBirthDate.show(mainActivityContext.getSupportFragmentManager(), SelectBirthDate.class.getSimpleName());
    }

    private void male() {
        genderType = AppConstants.Gender.MALE;
    }

    private void female() {
        genderType = AppConstants.Gender.FEMALE;
    }

    private void selectNationality() {
        NationalityDialog Nationalitydialog = NationalityDialog.newInstance(mainActivityContext.getResources().getString(R.string.select_nationality));
        Nationalitydialog.setCancelable(true);
        Nationalitydialog.setOKButtonClicked(new NationalityDialog.buttonClicked() {
            @Override
            public void onClicked(String value) {
                binding.etNationality.setText(value);
            }
        });
        Nationalitydialog.show(mainActivityContext.getSupportFragmentManager(), null);
    }

    @SuppressLint("ValidFragment")
    public static class SelectBirthDate extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        TextView textView;
        int yy;
        int mm;
        int dd;

        String fromDate, toDate;

        SelectBirthDate(TextView textView) {
            this.textView = textView;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            Locale locale = getResources().getConfiguration().locale;
            Locale.setDefault(locale);
            yy = calendar.get(Calendar.YEAR);
            mm = calendar.get(Calendar.MONTH);
            dd = calendar.get(Calendar.DAY_OF_MONTH);

            fromDate = yy + "-" + mm + "-" + dd;

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            int m = month + 1;
            if (m < 10) {
                toDate = year + "-0" + (month + 1) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
            } else {
                toDate = year + "-" + (month + 1) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
            }

            SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.DOB_FORMAT);
            try {
                Date dateFrom = sdf.parse(fromDate);
                Date dateTo = sdf.parse(toDate);
                if (dateFrom.compareTo(dateTo) > -1) {
                    textView.setText(toDate);
                } else {
                    UIHelper.showToast(getActivity(), getActivity().getResources().getString(R.string.minimum_age_error), Toast.LENGTH_SHORT);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            /*
            if (age >= AppConstants.MINIMUM_AGE) {
                this.textView.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            } else {
                UIHelper.showToast(getActivity(), getActivity().getResources().getString(R.string.minimum_age_error), Toast.LENGTH_SHORT);
            }
            */
        }
    }

    public RequestBody convertFile(String filePath) {
        return RequestBody.create(MediaType.parse("image/*"), new File(filePath));
    }
}
