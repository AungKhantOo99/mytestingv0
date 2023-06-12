package com.example.pushnoti.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.pushnoti.R


object Notification {
    var notificationManager: NotificationManager? = null

    fun showNotification(context: Context, cls: Class<*>?, title: String?, content: String?) {
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationIntent = Intent(context, cls)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val stackBuilder = androidx.core.app.TaskStackBuilder.create(context)
        stackBuilder.addParentStack(cls!!)
        stackBuilder.addNextIntent(notificationIntent)

        val pendingIntent = stackBuilder.getPendingIntent(
            100,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationManager = context.applicationContext.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "notischeduler"
        val channelname = "notiname"
        val builder = NotificationCompat.Builder(context.applicationContext, channelId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelname, NotificationManager.IMPORTANCE_HIGH)
            channel.enableVibration(true)
            channel.lockscreenVisibility = NotificationCompat.PRIORITY_HIGH
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            channel.setSound(alarmSound, audioAttributes)
            channel.vibrationPattern = longArrayOf(2000)
            notificationManager?.createNotificationChannel(
                channel
            )
        }
        val notification = builder
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setSound(alarmSound)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(pendingIntent).build();
        //hide the notification after it's selected
        //  notification.flags = Notification.FLAG_AUTO_CANCEL
        notificationManager?.notify(
            100,
            notification
        )
    }
}