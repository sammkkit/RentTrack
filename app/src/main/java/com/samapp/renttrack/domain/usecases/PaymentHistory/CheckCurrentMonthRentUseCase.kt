package com.samapp.renttrack.domain.usecases.PaymentHistory

import com.samapp.renttrack.data.repository.PaymentHistoryRepository
import java.time.YearMonth

class CheckCurrentMonthRentUseCase(
    private val paymentHistoryRepository: PaymentHistoryRepository
) {
    suspend fun checkCurrentMonth(tenantId: Int): Boolean {
        val currentMonth = YearMonth.now()

        val existingPayment = paymentHistoryRepository.getPaymentForMonth(tenantId, currentMonth)

        if(existingPayment != null){
            return true
        }else{
            return false
        }
    }
}