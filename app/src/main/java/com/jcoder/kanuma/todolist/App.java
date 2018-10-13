package com.jcoder.kanuma.todolist;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String REMINDER_CHANNEL_ID="reminder channel 1";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();

    }

    private void createNotificationChannel() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    REMINDER_CHANNEL_ID,
                    "Do The Task",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription("Remind user about the task that needs to be done");
            NotificationManager manager =getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }



    }
}
