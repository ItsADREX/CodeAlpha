package com.adam.localnotifications

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.PendingIntent


const val notificationID = 1
const val channelID = "channel1"
const val CHANNEL_ID = "channel1"  // This should match the channel ID used when creating the notification channel.
const val NOTIFICATION_ID = 1  // You can use any integer, but it must be unique for each notification type.

const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"



class Notification : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val notificationIntent = Intent(context, MainActivity::class.java) // Assuming MainActivity is what you want to open
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Use any existing icon in your drawable folder
            .setContentTitle(intent.getStringExtra("titleExtra") ?: "Default Title")
            .setContentText(intent.getStringExtra("messageExtra") ?: "Default Text")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Automatically removes the notification when tapped
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID, notification) // Make sure NOTIFICATION_ID is a unique integer
    }
}
