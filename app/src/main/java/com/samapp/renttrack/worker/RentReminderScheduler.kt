package com.samapp.renttrack.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object RentReminderScheduler {
    //TODO - one time is working fine but with periodic request it is not working
    fun scheduleDailyReminder(
        context: Context
    ){
//        val workRequest = OneTimeWorkRequestBuilder<RentDueWorker>()
//            .setInitialDelay(10,TimeUnit.SECONDS)
//            .build()
        val workRequest = PeriodicWorkRequestBuilder<RentDueWorker>(
            1, TimeUnit.DAYS,

        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .setRequiresDeviceIdle(false)
                    .setRequiresCharging(false)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "rent_due_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
//        WorkManager.getInstance(context).enqueue(workRequest)

    }
    private fun calculateInitialDelay(): Long {
        val now = java.util.Calendar.getInstance()
        val target = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 9)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }

        if (now.after(target)) {
            target.add(java.util.Calendar.DAY_OF_MONTH, 1)
        }

        return target.timeInMillis - now.timeInMillis
    }
}