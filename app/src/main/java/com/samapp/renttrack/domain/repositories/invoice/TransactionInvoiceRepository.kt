package com.samapp.renttrack.domain.repositories.invoice

import android.content.Context
import com.samapp.renttrack.data.local.model.PaymentHistory
import com.samapp.renttrack.data.local.model.Tenant
import java.io.File

interface TransactionInvoiceRepository {
    suspend fun generateInvoice(transactions: List<PaymentHistory>?): File?
    fun shareInvoice(context: Context, file: File)
}