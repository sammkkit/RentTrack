package com.samapp.renttrack.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.samapp.renttrack.data.repository.TenantRepository
import com.samapp.renttrack.domain.usecases.PaymentHistory.CheckCurrentMonthRentUseCase
import com.samapp.renttrack.presentation.viewmodels.PaymentHistoryViewModel
import javax.inject.Inject

class RentDueReminderWorkerFactory @Inject constructor(
    private val tenantRepository: TenantRepository,
    private val checkCurrentMonthRentUseCase: CheckCurrentMonthRentUseCase
): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ) =
        RentDueWorker(appContext, workerParameters,tenantRepository,checkCurrentMonthRentUseCase)
}