package com.samapp.renttrack.domain.usecases.PaymentHistory

import com.samapp.renttrack.data.local.model.PaymentHistory
import com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.data.repository.PaymentHistoryRepository
import com.samapp.renttrack.data.repository.TenantRepository
import com.samapp.renttrack.domain.usecases.Tenants.GetTenantByIdUseCase
import com.samapp.renttrack.domain.usecases.Tenants.UpdateTenantUseCase
import javax.inject.Inject
import android.util.Log
import com.samapp.renttrack.data.local.model.PaymentType

class AddPaymentHistoryUseCase @Inject constructor(
    private val repository: PaymentHistoryRepository,
    private val getTenantByIdUseCase: GetTenantByIdUseCase,
    private val updateTenantUseCase: UpdateTenantUseCase
) {
    suspend operator fun invoke(payment: PaymentHistory): Result<Unit> {
        Log.d("AddPaymentHistoryUseCase", "Fetching tenant with ID: ${payment.tenantId}")

        val tenantResult = getTenantByIdUseCase.execute(payment.tenantId)
        if (tenantResult !is Result.Success) {
            Log.e("AddPaymentHistoryUseCase", "Tenant not found for ID: ${payment.tenantId}")
            return Result.Error("Tenant not found")
        }

        val tenant = tenantResult.data
        tenant?.let {
            Log.d("AddPaymentHistoryUseCase", "Tenant found: $tenant")

            val rent = it.monthlyRent ?: return Result.Error("Monthly rent not set")
            var updatedTenant = it
            updatedTenant = when (payment.paymentType) {
                PaymentType.RENT -> {
                    when {
                        payment.paymentAmount < rent -> {
                            val newDebt = (it.outstandingDebt ?: 0.0) + (rent - payment.paymentAmount)
                            Log.d("AddPaymentHistoryUseCase", "Updated outstandingDebt: $newDebt")
                            updatedTenant.copy(outstandingDebt = newDebt)
                        }
                        payment.paymentAmount > rent -> {
                            val newDeposit = (it.deposit ?: 0.0) + (payment.paymentAmount - rent)
                            Log.d("AddPaymentHistoryUseCase", "Updated deposit: $newDeposit")
                            updatedTenant.copy(deposit = newDeposit)
                        }
                        else -> updatedTenant
                    }
                }
                PaymentType.DEPOSIT -> {
                    val newDeposit = (it.deposit ?: 0.0 )+ payment.paymentAmount
                    Log.d("AddPaymentHistoryUseCase", "Updated deposit: $newDeposit")
                    updatedTenant.copy(deposit = newDeposit)
                }
                PaymentType.DEBT -> {
                    val newDebt = (it.outstandingDebt ?: 0.0) + payment.paymentAmount
                    Log.d("AddPaymentHistoryUseCase", "Updated outstandingDebt: $newDebt")
                    updatedTenant.copy(outstandingDebt = newDebt)
                }
            }


            updateTenantUseCase.execute(updatedTenant)
            Log.d("AddPaymentHistoryUseCase", "Tenant updated successfully")

            repository.insertPaymentHistory(payment)
            Log.d("AddPaymentHistoryUseCase", "Payment history added successfully")
            return Result.Success(Unit)
        }

        Log.e("AddPaymentHistoryUseCase", "Unexpected error occurred while processing payment")
        return Result.Error("Unexpected error occurred")
    }
}


