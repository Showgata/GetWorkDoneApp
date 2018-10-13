package com.jcoder.kanuma.todolist;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";
    private NotificationManagerCompat managerCompat;
    @Override
    public void onReceive(Context context, Intent i) {
        //show notification

        Log.i(TAG, "onReceive: "+i.getStringExtra("notification title"));

        managerCompat = NotificationManagerCompat.from(context);

        Notification notificationCompat = new NotificationCompat.Builder(context, App.REMINDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Friendly Reminder")
                .setContentText(i.getStringExtra(AddTodo.NOTIFICATION_TITLE))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        MediaPlayer mp = MediaPlayer.create(context.getApplicationContext(),uri);
        mp.start();

        managerCompat.notify(1,notificationCompat);

    }
}
