package com.samapp.renttrack.domain.usecases.PaymentHistory

import com.samapp.renttrack.data.local.model.PaymentHistory
import com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.data.repository.PaymentHistoryRepository
import java.time.LocalDate
import java.time.YearMonth

class GetPaymentInfoForMonthUseCase (
    private val paymentHistoryRepository: PaymentHistoryRepository
){
    suspend fun collectRentForTenant(tenantId: Int, rentAmount: Double):Result<Unit> {
        return try {
            val currentMonth = YearMonth.now()

            val existingPayment = paymentHistoryRepository.getPaymentForMonth(tenantId, currentMonth)

            if (existingPayment != null) {
                return Result.Error("Rent for $currentMonth has already been collected.")
            }

            val newPayment = PaymentHistory(
                tenantId = tenantId,
                paymentAmount = rentAmount,
                paymentDate = LocalDate.now(),
                paymentForWhichMonth = currentMonth
            )

            paymentHistoryRepository.insertPaymentHistory(newPayment)

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "An error occurred")
        }
    }

}