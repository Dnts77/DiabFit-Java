package com.example.diabfit;

import android.Manifest;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String taskDescription = intent.getStringExtra("TASK_DESCRIPTION");
        if (taskDescription == null) {
            taskDescription = "Você tem um novo lembrete!";
        }


        Notification notification = new NotificationCompat.Builder(context, "REMINDER_CHANNEL_ID")
                .setSmallIcon(R.drawable.logo2)
                .setContentTitle("Lembrete: Daqui a 5 minutos")
                .setContentText(taskDescription)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

            notificationManager.notify((int) System.currentTimeMillis(), notification);
        }
    }
}
