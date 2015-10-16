package com.anoki;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.anoki.common.DBManager;
import com.anoki.common.Util;
import com.anoki.pojo.AlarmSetting;
import com.google.android.gms.gcm.GcmListenerService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015-10-14.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private Map<Integer,String> map = new HashMap<Integer, String>(){{
        put(3,"RQST");
        put(2,"RQS");
        put(1,"RQ");
        put(0,"");
    }};

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String name1 = data.getString("name1");
        String name2 = data.getString("name2");

        String type = data.getString("type");
        Log.d(TAG, "From: " + from);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */

        final DBManager dbManager = new DBManager(getApplicationContext());
        AlarmSetting setting = dbManager.getAlarmSetting();
        String r = map.get(setting.level);
        if(r.contains(type)) {
            sendNotification(Util.alarmText(type, name1, name2));
        }
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        final DBManager dbManager = new DBManager(getApplicationContext());
        AlarmSetting setting = dbManager.getAlarmSetting();


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_flag)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if(setting.sound == 1){
            notificationBuilder.setSound(defaultSoundUri);
        }
        if(setting.vibe == 1){
            notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000 });
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}