package com.ingic.caristocrat.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentSigninBinding;
import com.ingic.caristocrat.helpers.BasePreferenceHelper;
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
import com.ingic.caristocrat.webhelpers.models.UserSignin;
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
public class SigninFragment extends BaseFragment implements View.OnClickListener, SocialLoginHelper.FbLoginInfoFetcher, SocialLoginHelper.GoogleLoginInfoFetcher, ImageDownloadListener {
    FragmentSigninBinding binding;
    String socialID = "";
    String socialName = "";
    String socialEmail = "";
    String socialAccessToken = "";
    boolean isFacebook = false;
    boolean isGoogle = false;
    private String filePath;
    private MultipartBody.Part imageBody;
    ArrayList<Region> regions;
    Region selectedRegion;

    public SigninFragment() {
    }

    public static SigninFragment Instance() {
        return new SigninFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signin, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
        if (registrationActivityContext.fromGuest) {
            binding.tvSkip.setVisibility(View.GONE);
        }
//        getRegions();
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(registrationActivityContext);
        titlebar.showTitlebar(registrationActivityContext);

    }

    @Override
    public void onClick(View view) {
        UIHelper.hideSoftKeyboard(registrationActivityContext, view);
        switch (view.getId()) {
            case R.id.btnSignIn:
                signin();
                break;
            case R.id.tvForgetPwd:
                forgetPassword();
                break;
            case R.id.tvSignUp:
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

            case R.id.tvSkip:
                registrationActivityContext.startActivity(MainActivity.class, true);
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
                    if (social && isGoogle) {
                        googleLogin();
                    } else if (social) {
                        fbLogin();
                    }
                }
            }, registrationActivityContext.getResources().getString(R.string.select_your_region), regionsNames, false);
        } else {
            getRegions(social, isGoogle);
//            UIHelper.showToast(registrationActivityContext, registrationActivityContext.getResources().getString(R.string.no_regions_try_later), Toast.LENGTH_SHORT);
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
            SocialLoginHelper.Instance().setGoogleInfoFetcher(this);
            SocialLoginHelper.Instance().loginWithGoogle(registrationActivityContext);
        }
    }

    private void setListeners() {
        binding.ibFb.setOnClickListener(this);
        binding.ibGoogle.setOnClickListener(this);
        binding.tvForgetPwd.setOnClickListener(this);
        binding.tvSignUp.setOnClickListener(this);
        binding.btnSignIn.setOnClickListener(this);
        binding.ivShowPassword.setOnClickListener(this);
        binding.tvSkip.setOnClickListener(this);
        binding.ilEmail.setErrorEnabled();
        binding.ilPassword.setErrorEnabled();
    }

    private void signin() {
        if (!CustomValidation.validateEmail(binding.etEmail, binding.ilEmail, registrationActivityContext.getResources().getString(R.string.err_email)))
            return;

        if (!CustomValidation.validateLength(binding.etPassword, binding.ilPassword, registrationActivityContext.getResources().getString(R.string.err_password), "1", "20"))
            return;

        signinCall();
    }

    private void signup() {
        registrationActivityContext.replaceFragment(SignupFragment.Instance(true), SignupFragment.class.getName(), true, true);
    }

    private void forgetPassword() {
        registrationActivityContext.replaceFragment(ForgotPasswordFragment.Instance(), ForgotPasswordFragment.class.getName(), true, true);
    }

    private void signinCall() {
        UserSignin userSignin = new UserSignin();
        userSignin.setEmail(binding.etEmail.getText().toString().trim());
        userSignin.setPassword(binding.etPassword.getText().toString().trim());
        userSignin.setDeviceToken(preferenceHelper.getDeviceToken());
        userSignin.setDeviceType(AppConstants.OS_TYPE);

        if (registrationActivityContext.showLoader()) {
            disableViewsForSomeSeconds(binding.btnSignIn);
            WebApiRequest.Instance(registrationActivityContext).request(AppConstants.WebServicesKeys.LOGIN, null, userSignin, null, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    if (!apiResponse.isSuccess()) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("email", binding.etEmail.getText().toString().trim());
                        disableViewsForSomeSeconds(binding.btnSignIn);
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
                    } else {
                        UserWrapper userWrapper = (UserWrapper) JsonHelpers.convertToModelClass(apiResponse.getData(), UserWrapper.class);
                        preferenceHelper.putUserToken(userWrapper.getUser().getAccessToken());
                        registrationActivityContext.getPreferenceHelper().setLoginStatus(true);
                        if (registrationActivityContext.internetConnected()) {
                            WebApiRequest.Instance(registrationActivityContext).request(AppConstants.WebServicesKeys.REFRESH, null, null, null, new WebApiRequest.WebServiceObjectResponse() {
                                @Override
                                public void onSuccess(ApiResponse apiResponse) {
                                    UserWrapper userWrapper = (UserWrapper) JsonHelpers.convertToModelClass(apiResponse.getData(), UserWrapper.class);
                                    preferenceHelper.putUser(userWrapper.getUser());
                                    preferenceHelper.setBooleanPrefrence(BasePreferenceHelper.KEY_ENABLE_NOTIFICATIONS, userWrapper.getUser().getPush_notification() == 1 ? true : false);
                                    preferenceHelper.putUserToken(userWrapper.getUser().getAccessToken());
//                            UIHelper.showToast(registrationActivityContext, apiResponse.getMessage(), Toast.LENGTH_LONG);
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
        params.put("device_token", preferenceHelper.getDeviceToken());
        params.put("device_type", AppConstants.OS_TYPE);
//        params.put("region_id", selectedRegion.getId());
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
                            preferenceHelper.setBooleanPrefrence(BasePreferenceHelper.KEY_ENABLE_NOTIFICATIONS, userWrapper.getUser().getPush_notification() == 1 ? true : false);
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
