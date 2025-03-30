package com.samapp.renttrack.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.samapp.renttrack.R
import com.samapp.renttrack.data.local.model.Tenant
import java.time.LocalDate

class RentDueNotificationHelper(private val context: Context) {
    private val channelId = "rent_due_Channel"
    private val channelName = "Rent Due Notifications"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for rent due reminders"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(tenant: Tenant, dueDate: LocalDate) {
        Log.d("RentDueWorker", "Entered showNotification")
        createNotificationChannel()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Rent Due Reminder")
            .setContentText("${tenant.name} is due on $dueDate")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .build()
        Log.d("RentDueWorker", "Showing notification: $notification")
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
