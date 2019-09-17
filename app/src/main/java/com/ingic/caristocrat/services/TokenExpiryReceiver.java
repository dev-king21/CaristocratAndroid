package com.ingic.caristocrat.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ingic.caristocrat.helpers.UIHelper;

/**
 */
public class TokenExpiryReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        UIHelper.showToast(context, "Alarm Receiver.", Toast.LENGTH_SHORT);
    }
}
