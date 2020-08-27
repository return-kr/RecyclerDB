package com.example.recyclerdb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent activityIntent = new Intent(context, NotificationActivity.class);
        activityIntent.putExtra("name", intent.getStringExtra("name"));
        activityIntent.putExtra("_id", intent.getIntExtra("_id", 0));
        activityIntent.putExtra("title", intent.getStringExtra("title"));
        activityIntent.putExtra("date", intent.getStringExtra("date"));
        activityIntent.putExtra("time", intent.getStringExtra("time"));
        activityIntent.putExtra("detail", intent.getStringExtra("detail"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);
        int _id = intent.getIntExtra("_id", 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyChannel")
                .setSmallIcon(R.drawable.todo)
                .setContentIntent(pendingIntent).setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("detail"))
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVibrate(new long[]{1000, 1000, 1000})
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(_id, builder.build());
    }
}
