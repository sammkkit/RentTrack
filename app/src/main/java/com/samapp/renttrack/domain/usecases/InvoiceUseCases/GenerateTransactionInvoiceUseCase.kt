package com.samapp.renttrack.domain.usecases.InvoiceUseCases

import com.samapp.renttrack.data.local.model.PaymentHistory
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.domain.repositories.invoice.RentInvoiceRepository
import com.samapp.renttrack.domain.repositories.invoice.TransactionInvoiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

class GenerateTransactionInvoiceUseCase(
    private val invoiceRepository: TransactionInvoiceRepository
) {
    fun execute(transactions: List<PaymentHistory>?): Flow<File?> = flow {
        val file = invoiceRepository.generateInvoice(transactions)
        emit(file)
    }.flowOn(Dispatchers.IO)
}