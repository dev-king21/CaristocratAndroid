package com.ingic.caristocrat.fragments;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.RegistrationActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentSettingsBinding;
import com.ingic.caristocrat.helpers.DialogFactory;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.SimpleDialogActionListener;
import com.ingic.caristocrat.models.Region;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.User;
import com.ingic.caristocrat.webhelpers.models.UserWrapper;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class SettingsFragment extends BaseFragment implements View.OnClickListener {
    FragmentSettingsBinding binding;
    User user;
    ArrayList<Region> regions;
    Region selectedRegion;

    public SettingsFragment() {

    }

    public static SettingsFragment Instance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = preferenceHelper.getUser();
        setData();
        setListeners();
    }

    private void setData() {
        if (user != null && user.getDetails() != null) {
            if (user.getDetails().getSocialLogin()) {
                binding.tvChangePassword.setVisibility(View.GONE);
                binding.viewChangePwd.setVisibility(View.GONE);
            } else {
                binding.tvChangePassword.setVisibility(View.VISIBLE);
                binding.viewChangePwd.setVisibility(View.VISIBLE);
            }
            if (user.getPush_notification() == 0) {
                binding.cbpushNotification.setChecked(false);
            }
            if (user.getDetails().getUserRegionDetail() != null) {
                binding.tvRegion.setText(user.getDetails().getUserRegionDetail().getName());
            } else {
                binding.tvRegion.setText(mainActivityContext.getResources().getString(R.string.select_your_region));
            }
        }
        try {
            PackageInfo pInfo = mainActivityContext.getPackageManager().getPackageInfo(mainActivityContext.getPackageName(), 0);
            String version = pInfo.versionName;
            binding.tvVersionCode.setText(version.trim());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        binding.cbpushNotification.setChecked(preferenceHelper.getBooleanPrefrence(BasePreferenceHelper.KEY_ENABLE_NOTIFICATIONS));
    }

    @Override
    public void onClick(View view) {
        UIHelper.hideSoftKeyboard(mainActivityContext, view);
        switch (view.getId()) {
            case R.id.tvNotification:
                binding.cbpushNotification.performClick();
                break;
            case R.id.cbpushNotification:
                updatePushNotification(user.getPush_notification() == 0 ? 1 : 0);
                break;
            case R.id.tvChangePassword:
                mainActivityContext.replaceFragment(ChangePasswordFragment.Instance(), ChangePasswordFragment.class.getName(), true, true);
                break;
            case R.id.tvEditProfile:
                editProfile();
                break;
            case R.id.tvLogout:
                logout();
                break;
            case R.id.tvDeleteAccount:
//                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.implemented_later), Toast.LENGTH_SHORT);
                delete();
                break;
            case R.id.llUpdateRegion:
                getRegions();
            case R.id.mySubscription:
                mainActivityContext.replaceFragment(MySubscription.Instance(), MySubscription.class.getName(), true, false);
                break;
        }
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.showTitlebar(mainActivityContext);
        titlebar.setTitle(mainActivityContext.getResources().getString(R.string.settings));
        titlebar.showBackButton(mainActivityContext, false);
    }

    private void setListeners() {
        binding.tvNotification.setOnClickListener(this);
        binding.cbpushNotification.setOnClickListener(this);
        binding.tvChangePassword.setOnClickListener(this);
        binding.tvEditProfile.setOnClickListener(this);
        binding.tvLogout.setOnClickListener(this);
        binding.tvDeleteAccount.setOnClickListener(this);
        binding.llUpdateRegion.setOnClickListener(this);
        binding.mySubscription.setOnClickListener(this);
    }

    private void editProfile() {
        mainActivityContext.replaceFragment(ProfileEditFragment.Instance(), ProfileEditFragment.class.getSimpleName(), true, true);
    }

    private void logout() {
        UIHelper.showSimpleDialog(
                mainActivityContext, 0, mainActivityContext.getResources().getString(R.string.logout), mainActivityContext.getResources().getString(R.string.logout_ques), mainActivityContext.getResources().getString(R.string.no), mainActivityContext.getResources().getString(R.string.yes), false, true,
                new SimpleDialogActionListener() {
                    @Override
                    public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                        if (!positive) {
                            dialog.dismiss();
                            logOutCall();
                        } else {
                            dialog.dismiss();
                        }
                    }
                }
        );
    }

    private void delete() {
        UIHelper.showSimpleDialog(
                mainActivityContext,
                0,
                mainActivityContext.getResources().getString(R.string.delete_account),
                mainActivityContext.getResources().getString(R.string.delete_account_confirmation),
                mainActivityContext.getResources().getString(R.string.no),
                mainActivityContext.getResources().getString(R.string.yes),
                false,
                false,
                new SimpleDialogActionListener() {
                    @Override
                    public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                        if (!positive) {
                            deleteUserCall();
                        }
                    }
                }

        );
    }

    private void openRegionPicker() {
        if (regions != null && regions.size() > 0) {
            final ArrayList<String> regionsNames = new ArrayList<>();

            for (int i = 0; i < regions.size(); i++) {
                regionsNames.add(regions.get(i).getName());
            }
            DialogFactory.listDialog(mainActivityContext, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    selectedRegion = regions.get(i);
                    if (selectedRegion != null) {
                        updateProfileCall(selectedRegion.getId());
                    }
                }
            }, mainActivityContext.getResources().getString(R.string.select_your_new_region), regionsNames, true);
        }
    }

    private void logOutCall() {
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.LOGOUT, null, null, null, new WebApiRequest.WebServiceObjectResponse() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                mainActivityContext.getPreferenceHelper().removeLoginPreference();
                mainActivityContext.clearBackStack();
                mainActivityContext.startActivity(RegistrationActivity.class, true);
                mainActivityContext.hideLoader();
            }

            @Override
            public void onError() {

            }
        }, null);
    }

    private void deleteUserCall() {
        Map<String, Object> params = new HashMap<>();
        params.put("image", null);
        params.put("status", 0);
        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.UPDATE_PROFILE, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    UIHelper.showSimpleDialog(
                            mainActivityContext,
                            0,
                            mainActivityContext.getResources().getString(R.string.account_deleted),
                            mainActivityContext.getResources().getString(R.string.account_registered_with) + " " + preferenceHelper.getUser().getEmail() + " " + mainActivityContext.getResources().getString(R.string.email_address_has_been_deleted),
                            mainActivityContext.getResources().getString(R.string.ok),
                            null,
                            false,
                            false,
                            new SimpleDialogActionListener() {
                                @Override
                                public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                                    mainActivityContext.getPreferenceHelper().removeLoginPreference();
                                    mainActivityContext.clearBackStack();
                                    mainActivityContext.startActivity(RegistrationActivity.class, true);
                                    mainActivityContext.hideLoader();
                                }
                            }
                    );
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            }, null);
        }
    }

    private void updatePushNotification(int pushValue) {
        if (mainActivityContext.showLoader()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("push_notification", pushValue);
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.UPDATE_PUSH_NOTIFICATIION, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.notifications_settings_updated), Toast.LENGTH_SHORT);
                    /*
                    if (((Double) ((LinkedTreeMap) apiResponse.getData()).get("push_notification")).intValue() == 1)
                        preferenceHelper.setBooleanPrefrence(BasePreferenceHelper.KEY_ENABLE_NOTIFICATIONS, true);
                    else
                        preferenceHelper.setBooleanPrefrence(BasePreferenceHelper.KEY_ENABLE_NOTIFICATIONS, false);
                    */
                    PushNotification pushNotification = (PushNotification) JsonHelpers.convertToModelClass(apiResponse.getData(), PushNotification.class);
                    user.setPush_notification(pushNotification.getPushNotification());
                    preferenceHelper.putUser(user);
                    mainActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            }, null);
        }
    }

    private void getRegions() {
        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.REGIONS, null, null, null, null, new WebApiRequest.WebServiceArrayResponse() {
                @Override
                public void onSuccess(ApiArrayResponse apiArrayResponse) {
                    regions = (ArrayList<Region>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Region.class);
                    openRegionPicker();
                    mainActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            });
        }
    }

    private void updateProfileCall(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("image", null);
        params.put("region_id", id);

        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.UPDATE_PROFILE, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    UserWrapper userWrapper = (UserWrapper) JsonHelpers.convertToModelClass(apiResponse.getData(), UserWrapper.class);
//                    preferenceHelper.putUser(userWrapper.getUser());
                    if (userWrapper.getUser().getDetails().getUserRegionDetail() != null) {
                        binding.tvRegion.setText(userWrapper.getUser().getDetails().getUserRegionDetail().getName());
                    }
                    UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.region_updated_successfully), Toast.LENGTH_SHORT);
                    mainActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            }, null);
        }
    }

    public class PushNotification {
        @SerializedName("push_notification")
        @Expose
        int pushNotification;

        public int getPushNotification() {
            return pushNotification;
        }

        public void setPushNotification(int pushNotification) {
            this.pushNotification = pushNotification;
        }
    }
}
