package com.samapp.renttrack.domain.repositories.invoice

import android.content.Context
import com.samapp.renttrack.data.local.model.Tenant
import java.io.File

interface InvoiceRepository {
    suspend fun generateInvoice(tenant:Tenant,amount: String, rentDate: String): File?
    fun shareInvoice(context: Context, file: File)
}