package com.samapp.renttrack.domain.usecases.InvoiceUseCases

import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.domain.repositories.invoice.RentInvoiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

class GenerateInvoiceUseCase(private val invoiceRepository: RentInvoiceRepository) {
    fun execute(tenant: Tenant, amount: String, rentDate: String): Flow<File?> = flow {
        val file = invoiceRepository.generateInvoice(tenant, amount, rentDate)
        emit(file)
    }.flowOn(Dispatchers.IO)
}

