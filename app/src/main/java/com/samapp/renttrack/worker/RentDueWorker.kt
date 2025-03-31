package com.samapp.renttrack.worker

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.samapp.renttrack.data.repository.TenantRepository
import com.samapp.renttrack.domain.usecases.PaymentHistory.CheckCurrentMonthRentUseCase
import com.samapp.renttrack.presentation.viewmodels.PaymentHistoryViewModel
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
    private val tenantRepository: TenantRepository,
    private val checkCurrentMonthRentUseCase: CheckCurrentMonthRentUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("RentDueWorker", "Worker started")

        return try {
            val currentDate = LocalDate.now()
            val tenants = tenantRepository.getUpcomingDueTenants()

            Log.d("RentDueWorker", "Found ${tenants.size} tenants ")

            // If no tenants are due, stop execution early
            if (tenants.isEmpty()) {
                Log.d("RentDueWorker", "No tenants with upcoming due rent. Exiting.")
                return Result.success()
            }

            val notificationHelper = RentDueNotificationHelper(context)
            var shouldSendNotification = false

            tenants.forEach { tenant ->
                val currentMonthRentState = checkCurrentMonthRentUseCase.checkCurrentMonth(tenant.id)

                if (currentMonthRentState) return@forEach // Skip if already paid
                tenant.rentDueDate?.let { dueDate ->
                    if (currentDate.plusDays(3) < dueDate) return@forEach // Skip if due date is more than 3 days away

                    shouldSendNotification = true
                    notificationHelper.showNotification(tenant, dueDate)
                }
            }

            if (!shouldSendNotification) {
                Log.d("RentDueWorker", "No notifications needed. Exiting.")
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("RentDueWorker", "Error fetching due tenants", e)
            Result.failure(Data.Builder().putString("error", e.message).build()) // Retry the work if it fails
        }
    }

}
