package com.samapp.renttrack.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.samapp.renttrack.data.local.model.PaymentHistory

@Dao
interface PaymentHistoryDao {

    @Insert
    suspend fun insertPaymentHistory(paymentHistory: PaymentHistory)

    @Query("SELECT * FROM payment_history_table WHERE tenantId = :tenantId ORDER BY paymentDate DESC")
    suspend fun getPaymentHistoryForTenant(tenantId: Int): List<PaymentHistory>

    @Query("SELECT * FROM payment_history_table ORDER BY paymentDate ASC")
    suspend fun getAllPaymentHistory(): List<PaymentHistory>
}