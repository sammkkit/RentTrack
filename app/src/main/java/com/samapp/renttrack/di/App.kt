package com.samapp.renttrack.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.work.Configuration
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.samapp.renttrack.data.local.database.AppDatabase
import com.samapp.renttrack.worker.RentDueWorker
import com.samapp.renttrack.worker.RentReminderScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App :Application(){
    override fun onCreate() {
        super.onCreate()

        val workRequest = OneTimeWorkRequestBuilder<RentDueWorker>().build()
        WorkManager.getInstance(this).enqueueUniqueWork(
            "due_date_test",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
//        RentReminderScheduler.scheduleDailyReminder(this)
        logWorkManagerStatus()
    }

    private fun logWorkManagerStatus() {
        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData("due_date_test")
            .observeForever { workInfos ->
                workInfos?.forEach { workInfo ->
                    Log.d("WorkManagerTest", "Work Status: ${workInfo.state}")
                }
            }
    }
}