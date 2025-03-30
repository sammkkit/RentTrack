package com.samapp.renttrack.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.samapp.renttrack.data.repository.TenantRepository
import com.samapp.renttrack.util.RentDueNotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltWorker
class RentDueWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val tenantRepository: TenantRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("RentDueWorker", "Worker started")
        return try {
            val tenants = tenantRepository.getUpcomingDueTenants()
            Log.d("RentDueWorker", "Found ${tenants.size} tenants with due rent")

            if (tenants.isNotEmpty()) {
                val notificationHelper = RentDueNotificationHelper(context)
                tenants.forEach { tenant ->
                    tenant.rentDueDate?.let { dueDate ->
                        notificationHelper.showNotification(tenant, dueDate)
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("RentDueWorker", "Error fetching due tenants", e)
            Result.retry() // Retry the work if it fails
        }
    }
}
