package com.samapp.renttrack.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
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
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "rent_due_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
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

        return if (now.after(target)) {
            target.add(java.util.Calendar.DAY_OF_MONTH, 1)
            target.timeInMillis - now.timeInMillis
        } else {
            target.timeInMillis - now.timeInMillis
        }
    }
}