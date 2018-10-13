package com.jcoder.kanuma.todolist.Extras;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.jcoder.kanuma.todolist.App;
import com.jcoder.kanuma.todolist.R;

public class NotifyUser {


    public void createNotification(Context context)
    {
        Notification notificationCompat = new NotificationCompat.Builder(context, App.REMINDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Wake Up")
                .setContentText("Wake Up Buddy Its Already Morning")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .build();


        //managerCompat.notify(1,notificationCompat);
    }
}
