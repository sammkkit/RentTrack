package com.samapp.renttrack.domain.usecases.PaymentHistory

import com.samapp.renttrack.data.local.model.PaymentHistory
import com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.data.repository.PaymentHistoryRepository
import javax.inject.Inject

class GetPaymentHistoryForTenantUseCase @Inject constructor(
    private val repository: PaymentHistoryRepository
) {
    suspend operator fun invoke(tenantId: Int): Result<List<PaymentHistory>> {
        return repository.getPaymentHistoryForTenant(tenantId)
    }
}
