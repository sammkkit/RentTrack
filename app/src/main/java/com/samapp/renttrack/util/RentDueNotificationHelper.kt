package com.samapp.renttrack.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.samapp.renttrack.R
import com.samapp.renttrack.data.local.model.Tenant
import java.time.LocalDate

class RentDueNotificationHelper (
    private val context:Context
){
    private val channelId = "rent_due_Channel"
    private val channelName = "Rent Due Notifications"

    fun showNotification(
        tenant:Tenant,
        duedate:LocalDate
    ){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager?.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Rent Due Reminder")
            .setContentText("${tenant.name} is due on $duedate")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        notificationManager?.notify(System.currentTimeMillis().toInt(),notification)
    }
}