package com.ingic.caristocrat.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.LayoutStayUpdatedDialogBinding;
import com.ingic.caristocrat.fragments.SettingsFragment;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.interfaces.DialogCloseListener;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.User;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.HashMap;

/**
 */
public class EnableNotificationsDialog extends DialogFragment implements View.OnClickListener {
    MainActivity context;
    LayoutStayUpdatedDialogBinding binding;
    private DialogCloseListener dialogListener;


    public void setDialogListener(DialogCloseListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public static EnableNotificationsDialog newInstance(MainActivity context) {
        EnableNotificationsDialog fragment = new EnableNotificationsDialog();
        fragment.context = context;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme);
        setCancelable(false);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_stay_updated_dialog, container, false);
        return binding.getRoot();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    private void setListeners() {
        binding.btnEnableNotifications.setOnClickListener(this);
        binding.btnMaybeLater.setOnClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (null != dialogListener) {
            dialogListener.onDismiss();
        }
        if (context != null)
            context.setTheme(R.style.AppTheme);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEnableNotifications:
                updatePushNotification(1);
                dismiss();
                break;
            case R.id.btnMaybeLater:
                dismiss();
                break;

        }
    }

    private void updatePushNotification(int pushValue) {
        if (context.internetConnected()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("push_notification", pushValue);
            WebApiRequest.Instance(context).request(AppConstants.WebServicesKeys.UPDATE_PUSH_NOTIFICATIION, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
//                if (((Double)((LinkedTreeMap) apiResponse.getData()).get("push_notification")).intValue()==1)
//                    context.getPreferenceHelper().setBooleanPrefrence(BasePreferenceHelper.KEY_ENABLE_NOTIFICATIONS, true);

                    SettingsFragment.PushNotification pushNotification = (SettingsFragment.PushNotification) JsonHelpers.convertToModelClass(apiResponse.getData(), SettingsFragment.PushNotification.class);
                    User user = context.getPreferenceHelper().getUser();
                    user.setPush_notification(pushNotification.getPushNotification());
                    context.getPreferenceHelper().putUser(user);
                }

                @Override
                public void onError() {

                }
            }, null);
        }
    }

}
