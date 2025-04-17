package com.samapp.renttrack.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.compose.runtime.remember
import androidx.room.Room
import androidx.work.Configuration
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessaging
import com.samapp.renttrack.data.local.database.AppDatabase
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.util.RentDueNotificationHelper
import com.samapp.renttrack.worker.RentDueReminderWorkerFactory
import com.samapp.renttrack.worker.RentDueWorker
import com.samapp.renttrack.worker.RentReminderScheduler
import dagger.hilt.android.HiltAndroidApp
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class App :Application(),Configuration.Provider{
    @Inject
    lateinit var rentDueReminderWorkerFactory: RentDueReminderWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(rentDueReminderWorkerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM", "Token: $token")
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id",
                "App Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        RentReminderScheduler.scheduleDailyReminder(this)
        logWorkManagerStatus()
    }

    private fun logWorkManagerStatus() {
        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData("rent_due_reminder")
            .observeForever { workInfos ->
                workInfos?.forEach { workInfo ->
                    Log.d("WorkManagerTest", "Work Status: ${workInfo.state}")
                }
            }
    }
}