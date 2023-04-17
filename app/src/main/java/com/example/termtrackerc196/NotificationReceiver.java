package com.example.termtrackerc196;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    public static final String notificationFile = "notificationFile";
    public static final String notificationID = "next NotificationID";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String type = extras.getString("type");

        if(type == null || type.isEmpty()){
            type = "";
        }

        String notificationTitle = extras.getString("title");
        String notificationMessage = extras.getString("message");
        int nextNotifID = extras.getInt("nextNotifID", getAndIncrementNextNotificationID(context));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Term tracker")
                .setContentTitle(notificationTitle)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText(notificationMessage)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(nextNotifID, builder.build());

    }

    public static boolean scheduleNotification(Context context, int id, long date, String title, String text, String type, String notificationFile){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int nextNotifID = getNextNotificationID(context);
        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        notificationIntent.putExtra("id", id);
        notificationIntent.putExtra("title", title);
        notificationIntent.putExtra("message", text);
        notificationIntent.putExtra("type", type);
        notificationIntent.putExtra("nextNotifID", nextNotifID);

        alarmManager.set(AlarmManager.RTC_WAKEUP, date, PendingIntent.getBroadcast(context, nextNotifID, notificationIntent, PendingIntent.FLAG_IMMUTABLE));
        SharedPreferences sharedPreferences = context.getSharedPreferences(notificationFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Integer.toString(id), nextNotifID);
        editor.commit();
        incrementNextNotificationID(context);
        return true;
    }

    public static boolean scheduleCourseNotification(Context context, int courseID, long date, String notificationTitle, String message){
        Toast.makeText(context, "Course notification scheduled", Toast.LENGTH_LONG).show();
        return scheduleNotification(context, courseID, date, notificationTitle, message, "course", "courseNotifications");
    }

    public static boolean scheduleAssessmentNotification(Context context, int courseID, long date, String notificationTitle, String message){
        Toast.makeText(context, "Assessment Notification scheduled", Toast.LENGTH_LONG).show();
        return scheduleNotification(context, courseID, date, notificationTitle, message, "assessment", "assessmentNotification");
    }

    private static int getNextNotificationID(Context context){
        SharedPreferences notificationSP;
        notificationSP = context.getSharedPreferences(notificationFile, Context.MODE_PRIVATE);
        int nextNotifID = notificationSP.getInt(notificationID, 1);
        return nextNotifID;
    }

    private static void incrementNextNotificationID(Context context){
        SharedPreferences notificationSP;
        notificationSP = context.getSharedPreferences(notificationFile, Context.MODE_PRIVATE);
        int nextNotifID = notificationSP.getInt(notificationID, 1);
        SharedPreferences.Editor notificationEditor = notificationSP.edit();
        notificationEditor.putInt(notificationID, nextNotifID + 1);
        notificationEditor.commit();
    }

    private int getAndIncrementNextNotificationID(Context context) {
        int nextNotifiID = getNextNotificationID(context);
        incrementNextNotificationID(context);
        return nextNotifiID;
    }
}
