package com.samapp.renttrack.domain.usecases.InvoiceUseCases

import android.content.Context
import com.samapp.renttrack.domain.repositories.invoice.RentInvoiceRepository
import java.io.File

class ShareInvoiceUseCase(private val invoiceRepository: RentInvoiceRepository) {
    fun execute(context: Context, file: File) {
        invoiceRepository.shareInvoice(context, file)
    }
}
