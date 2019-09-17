package com.ingic.caristocrat.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.models.FCMPayload;

public class FirebaseMessagingServiceHelper extends FirebaseMessagingService {
    public static int NOTIFICATION_ID = 1;
    //private NotificationManager mNotificationManager;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    @Override
    public void onNewToken(String s) {
//        super.onNewToken(s);
        Utils.setDeviceToken(getApplicationContext(), s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        sendNotifications(true,remoteMessage);
        if (Utils.loginStatus(getApplicationContext())) {
            sendPushNotification(remoteMessage);
        }
    }

    private void sendPushNotification(RemoteMessage remoteMessage) {
        FCMPayload fcmPayload = new FCMPayload();
        Bundle bundle = new Bundle();
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.favicon);
        if (remoteMessage.getData() != null) {
            if (remoteMessage.getData().get("title") != null) {
                mBuilder.setContentTitle(remoteMessage.getData().get("title"));
                fcmPayload.setTitle(remoteMessage.getData().get("title"));
            }
            if (remoteMessage.getData().get("body") != null) {
                mBuilder.setContentText(remoteMessage.getData().get("body"));
                fcmPayload.setMessage(remoteMessage.getData().get("body"));
            }

            if (remoteMessage.getData().get("ref") != null) {
                fcmPayload.setAction_id(Integer.parseInt(remoteMessage.getData().get("ref")));
            }

            if (remoteMessage.getData().get("type") != null) {
                fcmPayload.setAction_type(remoteMessage.getData().get("type"));
            }

        }
        mBuilder.setAutoCancel(true);

        bundle.putSerializable(AppConstants.FcmHelper.FCM_DATA_PAYLOAD, fcmPayload);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id = NOTIFICATION_CHANNEL_ID;
            // The user-visible name of the channel.
            CharSequence name = "Caristocrat";
            // The user-visible description of the channel.
            String description = "Caristocrat";
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(mChannel);
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.drawable.icon_notification);
        } else {
            mBuilder.setSmallIcon(R.drawable.icon_notification);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        if (notificationManager != null) {
            //  notificationManager.cancel(NOTIFICATION_ID);
            notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }


}
