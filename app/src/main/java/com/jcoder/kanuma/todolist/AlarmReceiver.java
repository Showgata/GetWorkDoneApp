package com.jcoder.kanuma.todolist;

import android.app.Notification;
import android.app.PendingIntent;
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

/*Broadcasts the alarm and show a notification */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";
    private NotificationManagerCompat managerCompat;
    @Override
    public void onReceive(Context context, Intent i) {
        //show notification

        Log.i(TAG, "onReceive: "+i.getStringExtra("notification title"));
        int id = i.getIntExtra(AddTodo.NOTIFICATION_ID,0);

        Intent in =new Intent(context,TodoListActivity.class);
        PendingIntent pdi = PendingIntent.getActivity(context,0,in,0);

        managerCompat = NotificationManagerCompat.from(context);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Notification notificationCompat = new NotificationCompat.Builder(context, App.REMINDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Friendly Reminder")
                .setContentText(i.getStringExtra(AddTodo.NOTIFICATION_TITLE))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(pdi)
                .setSound(uri)
                .build();




        managerCompat.notify(id,notificationCompat);

    }
}
