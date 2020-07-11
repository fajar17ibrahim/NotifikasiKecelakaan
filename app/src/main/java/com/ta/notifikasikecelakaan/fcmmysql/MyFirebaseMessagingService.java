package com.ta.notifikasikecelakaan.fcmmysql;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ta.notifikasikecelakaan.MainActivity;
import com.ta.notifikasikecelakaan.R;
import com.ta.notifikasikecelakaan.utils.Constans;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("Notification Response ", remoteMessage.toString());
        showNotification(remoteMessage);

    }

    private void showNotification(RemoteMessage remoteMessage){

        SharedPreferences sharedPreferences = getSharedPreferences(Constans.MY_SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Constans.TAG_USER_STATUS, remoteMessage.getData().get("status"));

        if (remoteMessage.getData().get("status").equals("kecelakaan")) {
            editor.putInt(Constans.TAG_USER_ID, Integer.parseInt(remoteMessage.getData().get("user_id")));
            editor.putString(Constans.TAG_HISTORY_ID, remoteMessage.getData().get("history_id"));
            editor.putString(Constans.TAG_USER_LAT, remoteMessage.getData().get("latitude"));
            editor.putString(Constans.TAG_USER_LONG, remoteMessage.getData().get("longitude"));
        } else if (remoteMessage.getData().get("status").equals("Dibawa ke Rumah Sakit")) {
            editor.putInt(Constans.TAG_USER_ID, Integer.parseInt(remoteMessage.getData().get("user_id")));
            editor.putString(Constans.TAG_HISTORY_ID, remoteMessage.getData().get("history_id"));
            editor.putString(Constans.TAG_HELPER_ID, remoteMessage.getData().get("helper_id"));
            editor.putString(Constans.TAG_USER_LAT, remoteMessage.getData().get("user_lat"));
            editor.putString(Constans.TAG_USER_LONG, remoteMessage.getData().get("user_long"));
            editor.putString(Constans.TAG_HELPER_NAME, remoteMessage.getData().get("helper_name"));
            editor.putString(Constans.TAG_HELPER_PHONE, remoteMessage.getData().get("helper_phone"));
            editor.putString(Constans.TAG_HELPER_LAT, remoteMessage.getData().get("helper_lat"));
            editor.putString(Constans.TAG_HELPER_LONG, remoteMessage.getData().get("helper_long"));
            editor.putString(Constans.TAG_HOSPITAL_CHOOSE_NAME, remoteMessage.getData().get("hospital_name"));
            editor.putString(Constans.TAG_HOSPITAL_CHOOSE_LAT, remoteMessage.getData().get("hospital_lat"));
            editor.putString(Constans.TAG_HOSPITAL_CHOOSE_LONG, remoteMessage.getData().get("hospital_long"));
        } else {
            sharedPreferences.edit().remove(Constans.TAG_HISTORY_ID).commit();
            sharedPreferences.edit().remove(Constans.TAG_USER_ID).commit();
            sharedPreferences.edit().remove(Constans.TAG_USER_LAT).commit();
            sharedPreferences.edit().remove(Constans.TAG_USER_LONG).commit();
            sharedPreferences.edit().remove(Constans.TAG_USER_STATUS).commit();
            sharedPreferences.edit().remove(Constans.TAG_HELPER_ID).commit();
            sharedPreferences.edit().remove(Constans.TAG_HELPER_NAME).commit();
            sharedPreferences.edit().remove(Constans.TAG_HELPER_PHONE).commit();
            sharedPreferences.edit().remove(Constans.TAG_HELPER_LAT).commit();
            sharedPreferences.edit().remove(Constans.TAG_HELPER_LONG).commit();
            sharedPreferences.edit().remove(Constans.TAG_HELPER_LONG).commit();
            sharedPreferences.edit().remove(Constans.TAG_HOSPITAL_CHOOSE_NAME).commit();
            sharedPreferences.edit().remove(Constans.TAG_HOSPITAL_CHOOSE_LAT).commit();
            sharedPreferences.edit().remove(Constans.TAG_HOSPITAL_CHOOSE_LONG).commit();
        }

        editor.apply();

        long[] pattern = {500, 500, 500, 500};
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            @SuppressLint({"WrongConstanta", "WrongConstant"}) NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION CHANNEL", NotificationManager.IMPORTANCE_MAX);

            notificationChannel.setDescription("EDMTDev channel for app test FCM");
            notificationChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            notificationChannel.setLightColor(Color.GREEN);
            notificationManager.createNotificationChannel(notificationChannel);

        }

            Intent iMainActivity = new Intent(getApplicationContext(), MainActivity.class);
            iMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, iMainActivity, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(remoteMessage.getNotification().getTitle())
                            .setContentText(remoteMessage.getNotification().getBody())
                            .setAutoCancel(true)
                            .setChannelId(NOTIFICATION_CHANNEL_ID)
                            .setVibrate(pattern)
                            .setContentIntent(pendingIntent)
                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.FLAG_AUTO_CANCEL);

            notificationManager.notify(1, mBuilder.build());

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


    }

}
