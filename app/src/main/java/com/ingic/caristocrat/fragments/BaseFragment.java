package com.ingic.caristocrat.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.BaseActivity;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.activities.RegistrationActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.helpers.BasePreferenceHelper;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.SimpleDialogActionListener;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.InteractionCar;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;


/**
 *
 */
public abstract class BaseFragment<T> extends Fragment {
    MainActivity mainActivityContext;
    RegistrationActivity registrationActivityContext;
    BasePreferenceHelper preferenceHelper;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivityContext = (MainActivity) context;
            preferenceHelper = mainActivityContext.getPreferenceHelper();
        } else if (context instanceof RegistrationActivity) {
            registrationActivityContext = (RegistrationActivity) context;
            preferenceHelper = registrationActivityContext.getPreferenceHelper();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (registrationActivityContext != null)
            setTitlebar(registrationActivityContext.getTitlebar());
        if (mainActivityContext != null) {
            setTitlebar(mainActivityContext.getTitlebar());
            mainActivityContext.getBinding().nestedscroll.scrollTo(0, 0);
        }
    }

    /*
        @Override
        public void onDestroyView() {
            stopCalls();
            super.onDestroyView();
        }
    */
    public abstract void setTitlebar(Titlebar titlebar);

    public void stopCalls() {
        if (registrationActivityContext != null) {
            if (WebApiRequest.Instance(registrationActivityContext).getCallObject() != null) {
                WebApiRequest.Instance(registrationActivityContext).getCallObject().cancel();
            } else if (WebApiRequest.Instance(registrationActivityContext).getCallArray() != null) {
                WebApiRequest.Instance(registrationActivityContext).getCallArray().cancel();
            }
            registrationActivityContext.hideLoader();
        } else if (mainActivityContext != null) {
            if (WebApiRequest.Instance(mainActivityContext).getCallObject() != null) {
                WebApiRequest.Instance(mainActivityContext).getCallObject().cancel();
            } else if (WebApiRequest.Instance(mainActivityContext).getCallArray() != null) {
                WebApiRequest.Instance(mainActivityContext).getCallArray().cancel();
            }
            mainActivityContext.hideLoader();
        }
    }

    public void disableViewsForSomeSeconds(View view) {
        view.setEnabled(false);
        view.setAlpha((float) 0.75);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
                view.setAlpha((float) 1.0);
            }
        }, AppConstants.SPLASH_DURATION);
    }

    public void IntrectionCall(int id, int type) {
        InteractionCar interactionCar = new InteractionCar();
        interactionCar.setCar_id(id);
        interactionCar.setType(type);
        if (preferenceHelper.getLoginStatus() && mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(
                    AppConstants.WebServicesKeys.CAR_INTRECTION, null,
                    interactionCar,
                    null,
                    new WebApiRequest.WebServiceObjectResponse() {
                        @Override
                        public void onSuccess(ApiResponse apiResponse) {
                            mainActivityContext.hideLoader();
                        }

                        @Override
                        public void onError() {
                            mainActivityContext.hideLoader();
                        }
                    },
                    null);
        }
    }

    public void launchSignin(BaseActivity activityContext) {
        MainDetailPageFragment.login = true;
        Intent intent = new Intent(activityContext, RegistrationActivity.class);
        intent.putExtra(AppConstants.FROM_GUEST, true);
        activityContext.startActivity(intent);
    }

    public void launchSigninRequirement(BaseActivity activityContext, String message) {
        UIHelper.showSimpleDialog(
                activityContext,
                0,
                activityContext.getResources().getString(R.string.require_signin),
                message,
                activityContext.getResources().getString(R.string.sign_in),
                activityContext.getResources().getString(R.string.cancel),
                false,
                false,
                new SimpleDialogActionListener() {
                    @Override
                    public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                        if (positive) {
                            dialog.dismiss();
                            Intent intent = new Intent(activityContext, RegistrationActivity.class);
                            intent.putExtra(AppConstants.FROM_GUEST, true);
                            activityContext.startActivity(intent);
//                            activityContext.startActivity(RegistrationActivity.class, false);
                        } else {
                            dialog.dismiss();
                        }
                    }
                }
        );
    }

    public void launchSigninDailog(BaseActivity activityContext) {
        String message = mainActivityContext.getResources().getString(R.string.sign_in_required);
        UIHelper.showSimpleDialog(
                activityContext,
                0,
                activityContext.getResources().getString(R.string.signin_required),
                message,
                activityContext.getResources().getString(R.string.create_an_account),
                activityContext.getResources().getString(R.string.login),
                true,
                false,
                new SimpleDialogActionListener() {
                    @Override
                    public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                        if (positive) {
                            dialog.dismiss();
                            Intent intent = new Intent(activityContext, RegistrationActivity.class);
                            intent.putExtra("signup", true);
                            activityContext.startActivity(intent);
                        } else {
                            dialog.dismiss();
                            Intent intent = new Intent(activityContext, RegistrationActivity.class);
                            intent.putExtra(AppConstants.FROM_GUEST, true);
                            activityContext.startActivity(intent);
                        }
                    }
                }
        );
    }
}
