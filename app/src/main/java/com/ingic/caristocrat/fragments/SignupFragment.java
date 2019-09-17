package com.ingic.caristocrat.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentSignupBinding;
import com.ingic.caristocrat.dialogs.CountryPickerDialog;
import com.ingic.caristocrat.helpers.CustomValidation;
import com.ingic.caristocrat.helpers.DialogFactory;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.SocialLoginHelper;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.interfaces.ImageDownloadListener;
import com.ingic.caristocrat.interfaces.SimpleDialogActionListener;
import com.ingic.caristocrat.models.Region;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.UserRegister;
import com.ingic.caristocrat.webhelpers.models.UserWrapper;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 */

public class SignupFragment extends BaseFragment implements View.OnClickListener, SocialLoginHelper.FbLoginInfoFetcher, SocialLoginHelper.GoogleLoginInfoFetcher, ImageDownloadListener {
    FragmentSignupBinding binding;
    String socialID = "";
    String socialName = "";
    String socialEmail = "";
    String socialAccessToken = "";
    boolean isFacebook = false;
    boolean isGoogle = false;
    boolean isFromSignin = false;
    private String filePath;
    private MultipartBody.Part imageBody;
    ArrayList<Region> regions;
    Region selectedRegion;

    public SignupFragment() {
    }

    @SuppressLint("ValidFragment")
    public SignupFragment(boolean isFromSignin) {
        this.isFromSignin = isFromSignin;
    }

    public static SignupFragment Instance(boolean isFromSignin) {
        SignupFragment signupFragment = new SignupFragment(isFromSignin);
        return signupFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
//        getRegions();
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(registrationActivityContext);
        titlebar.showTitlebar(registrationActivityContext);
        if (isFromSignin) {
            titlebar.showBackButton(registrationActivityContext, false);
        } else {
            titlebar.showBackButton(registrationActivityContext, false).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registrationActivityContext.replaceFragment(SigninFragment.Instance(), SigninFragment.class.getSimpleName(), true, false);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        UIHelper.hideSoftKeyboard(registrationActivityContext, view);
        switch (view.getId()) {

            case R.id.tvCodePicker:
                openCodePicker();
                break;

            case R.id.btnSignUp:
                signup();
                break;

            case R.id.ibFb:
                if (registrationActivityContext.internetConnected()) {
                    fbLogin();
                }
                break;

            case R.id.ibGoogle:
                if (registrationActivityContext.internetConnected()) {
                    googleLogin();
                }
                break;
            case R.id.ivShowPassword:
                Utils.hideShowPassword(binding.ivShowPassword, binding.etPassword, R.drawable.eye, R.drawable.eye1);
                break;
            case R.id.ivShowConfirmPassword:
                Utils.hideShowPassword(binding.ivShowConfirmPassword, binding.etConfirmPassword, R.drawable.eye, R.drawable.eye1);
                break;

            case R.id.tvRegion:
                openRegionPicker(false, false);
                break;
        }

    }

    @Override
    public void onFbInfoFetched(String id, String name, String email, String picture) {
        socialID = id;
        socialName = name;
        socialEmail = email;
        isFacebook = true;
        filePath = UIHelper.saveImage(registrationActivityContext, picture, this);
//        if(!picture.equals("")){
//        }else if(picture.equals("")){
//            socialSiginCall(AppConstants.SocialLogin.FACEBOOK, socialID, socialName, socialEmail, null);
//        }
    }

    @Override
    public void onGoogleInfoFetched(String id, String name, String email, String pictureUrl, String accessToken, String profileUrl) {
        socialID = id;
        socialName = name;
        socialEmail = email;
        isGoogle = true;
        socialAccessToken = accessToken;
        if (!pictureUrl.equals("")) {
            filePath = UIHelper.saveImage(registrationActivityContext, pictureUrl, this);
        } else if (pictureUrl.equals("")) {
            socialSiginCall(AppConstants.SocialLogin.GOOGLE, socialID, socialName, socialEmail, null);
        }
    }

    @Override
    public void isSaved(String image) {
        if (image != null && !image.equals("")) {
            imageBody = MultipartBody.Part.createFormData("image", new File(image).getName(), convertFile(image));
            if (isFacebook) {
                socialSiginCall(AppConstants.SocialLogin.FACEBOOK, socialID, socialName, socialEmail, imageBody);
            }
            if (isGoogle) {
                socialSiginCall(AppConstants.SocialLogin.GOOGLE, socialID, socialName, socialEmail, imageBody);
            }
        }

    }

    private void setListeners() {
        binding.tvCodePicker.setOnClickListener(this);
        binding.btnSignUp.setOnClickListener(this);
        binding.ibFb.setOnClickListener(this);
        binding.ibGoogle.setOnClickListener(this);
        binding.ivShowPassword.setOnClickListener(this);
        binding.ivShowConfirmPassword.setOnClickListener(this);
        binding.tvRegion.setOnClickListener(this);
        binding.ilEmail.setErrorEnabled();
        binding.ilName.setErrorEnabled();
        binding.ilPassword.setErrorEnabled();
        binding.ilPhoneNumber.setErrorEnabled();
        binding.ilConfirmPassword.setErrorEnabled();
        binding.etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    binding.tvCodePicker.setText("");
                }
            }
        });
    }

    private void openCodePicker() {
        registrationActivityContext.pickCountry(new CountryPickerDialog.OnCountrySelectedListener() {
            @Override
            public void onCountrySelected(String name, String code, String dialCode, int flagDrawableResID) {
                binding.tvCodePicker.setText(dialCode);
            }
        });
    }

    private void openRegionPicker(boolean social, boolean isGoogle) {
        if (regions != null && regions.size() > 0) {
            final ArrayList<String> regionsNames = new ArrayList<>();

            for (int i = 0; i < regions.size(); i++) {
                regionsNames.add(regions.get(i).getName());
            }
            DialogFactory.listDialog(registrationActivityContext, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    selectedRegion = regions.get(i);
                    if (!social) {
                        binding.tvRegion.setText(regionsNames.get(i));
                    }
                    if (social && isGoogle) {
                        googleLogin();
                    } else if (social) {
                        fbLogin();
                    }
                }
            }, registrationActivityContext.getResources().getString(R.string.select_your_region), regionsNames, false);
        } else {
            getRegions(social, isGoogle);
            UIHelper.showToast(registrationActivityContext, registrationActivityContext.getResources().getString(R.string.no_regions_try_later), Toast.LENGTH_SHORT);
        }
    }

    private void openRegionPicker() {
        if (regions != null && regions.size() > 0) {
            final ArrayList<String> regionsNames = new ArrayList<>();

            for (int i = 0; i < regions.size(); i++) {
                regionsNames.add(regions.get(i).getName());
            }
            DialogFactory.listDialog(registrationActivityContext, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    selectedRegion = regions.get(i);
                    if (selectedRegion != null) {
                        updateProfileCall(selectedRegion.getId());
                    }
                }
            }, registrationActivityContext.getResources().getString(R.string.select_your_region), regionsNames, false);
        }
    }

    private void signup() {
        if (!CustomValidation.validateLength(binding.etName, binding.ilName, registrationActivityContext.getResources().getString(R.string.err_full_name), "3", "50"))
            return;
        if (!CustomValidation.validateEmail(binding.etEmail, binding.ilEmail, registrationActivityContext.getResources().getString(R.string.err_email)))
            return;
//        if (binding.etNumber.getText().toString().length() > 0 || binding.tvCodePicker.getText().toString().length() > 0) {
//            if (!CustomValidation.validateLength(binding.etNumber, binding.ilPhoneNumber, registrationActivityContext.getResources().getString(R.string.err_phone), "7", "20"))
//                return;
//        }
        if (!CustomValidation.validateLength(binding.etPassword, binding.ilPassword, registrationActivityContext.getResources().getString(R.string.err_password), "1", "20"))
            return;
        if (!CustomValidation.validateNewConfirmPassword(binding.etPassword, binding.etConfirmPassword, binding.ilConfirmPassword, registrationActivityContext)) {
            return;
        } else {
            binding.ilConfirmPassword.setError("");
        }
//        if (UIHelper.isEmptyOrNull(binding.tvCodePicker.getText().toString()) && binding.etNumber.getText().toString().length() >= 5) {
//            UIHelper.showToast(registrationActivityContext, registrationActivityContext.getResources().getString(R.string.err_code), Toast.LENGTH_SHORT);
////            registrationActivityContext.replaceFragment(SigninFragment.Instance(), SigninFragment.class.getName(), false, true);
//            return;
//        }

        if (binding.etNumber.getText().toString().length() > 0) {
            if (binding.tvCodePicker.getText().toString().length() == 0) {
                UIHelper.showToast(registrationActivityContext, registrationActivityContext.getResources().getString(R.string.err_code), Toast.LENGTH_SHORT);
                return;
            }

            if (!CustomValidation.validateLength(binding.etNumber, binding.ilPhoneNumber, registrationActivityContext.getResources().getString(R.string.err_phone), "7", "20"))
                return;
        }

        if (selectedRegion == null) {
            UIHelper.showToast(registrationActivityContext, registrationActivityContext.getResources().getString(R.string.select_your_region), Toast.LENGTH_SHORT);
            return;
        }

        if (registrationActivityContext.showLoader()) {
            signupCall();
        }
    }

    private void fbLogin() {
        if (!TedPermission.isGranted(registrationActivityContext, AppConstants.AppPermissions())) {
            TedPermission.with(registrationActivityContext)
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .setPermissionListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            fbLogin();
                        }

                        @Override
                        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                            UIHelper.showToast(registrationActivityContext, registrationActivityContext.getString(R.string.permission_denied), Toast.LENGTH_SHORT);
                        }
                    }).check();
        } else {
//            if (selectedRegion == null) {
//                openRegionPicker(true, false);
//                return;
//            }
            SocialLoginHelper.Instance().setSocialInfoFetcher(this);
            SocialLoginHelper.Instance().loginWithFacebook(registrationActivityContext);
        }
    }

    private void googleLogin() {
        if (!TedPermission.isGranted(registrationActivityContext, AppConstants.AppPermissions())) {
            TedPermission.with(registrationActivityContext)
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .setPermissionListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            googleLogin();
                        }

                        @Override
                        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                            UIHelper.showToast(registrationActivityContext, registrationActivityContext.getString(R.string.permission_denied), Toast.LENGTH_SHORT);
                        }
                    }).check();
        } else {
//            if (selectedRegion == null) {
//                openRegionPicker(true, true);
//                return;
//            }
            SocialLoginHelper.Instance().setGoogleInfoFetcher(this);
            SocialLoginHelper.Instance().loginWithGoogle(registrationActivityContext);
        }
    }

    private void signupCall() {
        UserRegister userRegister = new UserRegister();
        userRegister.setName(binding.etName.getText().toString().trim());
        // userRegister.setAddress("xyz");
        userRegister.setEmail(binding.etEmail.getText().toString().trim());
        if (binding.tvCodePicker.getText().toString().length() > 0) {
            userRegister.setCountryCode(binding.tvCodePicker.getText().toString().trim());
        }
        if (binding.etNumber.getText().toString().length() > 0) {
            userRegister.setPhone(binding.etNumber.getText().toString().trim());
        }
        userRegister.setDeviceToken(preferenceHelper.getDeviceToken());
        userRegister.setDeviceType(AppConstants.OS_TYPE);
        userRegister.setPassword(binding.etPassword.getText().toString().trim());
        userRegister.setPasswordConfirmation(binding.etConfirmPassword.getText().toString().trim());
        userRegister.setGender(AppConstants.Gender.NOGENDER);
        userRegister.setRegionId(selectedRegion.getId());

        if (registrationActivityContext.showLoader()) {
            WebApiRequest.Instance(registrationActivityContext).request(AppConstants.WebServicesKeys.REGISTER, null, userRegister, null, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    if (!apiResponse.isSuccess()) {
                        Utils.showDetailedErrors(registrationActivityContext.getMainFrameLayout(), apiResponse, AppConstants.ErrorsKeys.EMAIL);
                        registrationActivityContext.hideLoader();
                    } else {
                        UserWrapper userWrapper = (UserWrapper) JsonHelpers.convertToModelClass(apiResponse.getData(), UserWrapper.class);
                        preferenceHelper.putUserToken(userWrapper.getUser().getAccessToken());
//                        registrationActivityContext.getPreferenceHelper().setLoginStatus(true);
                        Map<String, Object> params = new HashMap<>();
                        params.put("email", binding.etEmail.getText().toString().trim());
                        UIHelper.showSimpleDialog(
                                registrationActivityContext,
                                0,
                                registrationActivityContext.getResources().getString(R.string.email_verification_required),
                                registrationActivityContext.getResources().getString(R.string.email_verification_sub_heading),
                                registrationActivityContext.getResources().getString(R.string.ok),
                                null,
                                null,
                                false,
                                false,
                                new SimpleDialogActionListener() {
                                    @Override
                                    public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                                        registrationActivityContext.replaceFragment(PhoneVerificationFragment.Instance(binding.etEmail.getText().toString().trim()), PhoneVerificationFragment.class.getName(), true, true);
                                    }
                                }
                        );
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

    private void socialSiginCall(String platform, String client_id, String username, String email, MultipartBody.Part image) {
        Map<String, Object> params = new HashMap<>();
        params.put("platform", platform);
        params.put("client_id", client_id);
        params.put("username", username);
        params.put("email", email);
        params.put("image", image);
//        params.put("region_id", selectedRegion.getId());
        params.put("device_token", preferenceHelper.getDeviceToken());
        params.put("device_type", AppConstants.OS_TYPE);
        if (registrationActivityContext.showLoader()) {
            WebApiRequest.Instance(registrationActivityContext).request(AppConstants.WebServicesKeys.SOCIAL_LOGIN, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    UserWrapper userWrapper = (UserWrapper) JsonHelpers.convertToModelClass(apiResponse.getData(), UserWrapper.class);
                    preferenceHelper.putUserToken(userWrapper.getUser().getAccessToken());
                    registrationActivityContext.getPreferenceHelper().setLoginStatus(true);
                    WebApiRequest.Instance(registrationActivityContext).request(AppConstants.WebServicesKeys.REFRESH, null, null, null, new WebApiRequest.WebServiceObjectResponse() {
                        @Override
                        public void onSuccess(ApiResponse apiResponse) {
                            UserWrapper userWrapper = (UserWrapper) JsonHelpers.convertToModelClass(apiResponse.getData(), UserWrapper.class);
                            preferenceHelper.putUser(userWrapper.getUser());
                            preferenceHelper.putUserToken(userWrapper.getUser().getAccessToken());
                            if (preferenceHelper.getUser() != null) {
                                if (preferenceHelper.getUser().getDetails() != null) {
                                    if (preferenceHelper.getUser().getDetails().getUserRegionDetail() != null) {
                                        if (!registrationActivityContext.fromGuest) {
                                            registrationActivityContext.startActivity(MainActivity.class, true);
                                        } else {
                                            registrationActivityContext.finish();
                                        }
                                        registrationActivityContext.hideLoader();
                                    } else {
                                        getRegions();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError() {
                            registrationActivityContext.hideLoader();
                        }
                    }, null);
                }

                @Override
                public void onError() {
                    registrationActivityContext.hideLoader();
                }
            }, null);
        }
    }

    private void getRegions(boolean social, boolean isGoogle) {
        if (registrationActivityContext.showLoader()) {
            WebApiRequest.Instance(registrationActivityContext).request(AppConstants.WebServicesKeys.REGIONS, null, null, null, null, new WebApiRequest.WebServiceArrayResponse() {
                @Override
                public void onSuccess(ApiArrayResponse apiArrayResponse) {
                    regions = (ArrayList<Region>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Region.class);
                    openRegionPicker(social, isGoogle);
                    registrationActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    registrationActivityContext.hideLoader();
                }
            });
        }
    }

    private void getRegions() {
        if (registrationActivityContext.showLoader()) {
            WebApiRequest.Instance(registrationActivityContext).request(AppConstants.WebServicesKeys.REGIONS, null, null, null, null, new WebApiRequest.WebServiceArrayResponse() {
                @Override
                public void onSuccess(ApiArrayResponse apiArrayResponse) {
                    regions = (ArrayList<Region>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Region.class);
                    openRegionPicker();
                    registrationActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    registrationActivityContext.hideLoader();
                }
            });
        }
    }

    private void updateProfileCall(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("image", null);
        params.put("region_id", id);

        if (registrationActivityContext.showLoader()) {
            WebApiRequest.Instance(registrationActivityContext).request(AppConstants.WebServicesKeys.UPDATE_PROFILE, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    UserWrapper userWrapper = (UserWrapper) JsonHelpers.convertToModelClass(apiResponse.getData(), UserWrapper.class);
                    preferenceHelper.putUser(userWrapper.getUser());
//                    preferenceHelper.putUserToken(userWrapper.getUser().getAccessToken());
                    if (!registrationActivityContext.fromGuest) {
                        registrationActivityContext.startActivity(MainActivity.class, true);
                    } else {
                        registrationActivityContext.finish();
                    }
                    registrationActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    registrationActivityContext.hideLoader();
                }
            }, null);
        }
    }

    public RequestBody convertFile(String filePath) {
        return RequestBody.create(MediaType.parse("image/*"), new File(filePath));
    }
}
