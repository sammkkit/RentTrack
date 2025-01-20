package com.samapp.renttrack.data.repository

import com.samapp.renttrack.data.local.dao.PaymentHistoryDao
import com.samapp.renttrack.data.local.model.PaymentHistory
import javax.inject.Inject
import com.samapp.renttrack.data.local.model.Result
class PaymentHistoryRepository @Inject constructor(
    private val paymentHistoryDao: PaymentHistoryDao
) {
    suspend fun insertPaymentHistory(paymentHistory: PaymentHistory) {
        paymentHistoryDao.insertPaymentHistory(paymentHistory)
    }
    suspend fun getPaymentHistoryForTenant(tenantId: Int): Result<List<PaymentHistory>> {
        return try {
            val paymentHistory = paymentHistoryDao.getPaymentHistoryForTenant(tenantId)
            Result.Success(data = paymentHistory)
        }catch (e:Exception){
            Result.Error(message = e.message)
        }
    }
    suspend fun getAllPaymentHistory(): Result<List<PaymentHistory>> {
        return try {
            val paymentHistory = paymentHistoryDao.getAllPaymentHistory()
            Result.Success(data = paymentHistory)
        }catch (e:Exception){
            Result.Error(message = e.message)
        }
    }
}