package com.samapp.renttrack.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object RentReminderScheduler {
    fun scheduleDailyReminder(
        context: Context
    ){
        val workRequest = PeriodicWorkRequestBuilder<RentDueWorker>(
            1,TimeUnit.DAYS
        )
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "rent_due_reminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

    }
    private fun calculateInitialDelay(): Long {
        val now = java.util.Calendar.getInstance()
        val target = now.apply {
            set(java.util.Calendar.HOUR_OF_DAY, 9)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
        }

        if (now.after(target)) {
            // If the current time is past 9 AM, schedule for the next day
            target.add(java.util.Calendar.DAY_OF_MONTH, 1)
        }

        return target.timeInMillis - now.timeInMillis
    }
}