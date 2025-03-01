package com.samapp.renttrack.domain.usecases.InvoiceUseCases

import android.content.Context
import com.samapp.renttrack.domain.repositories.invoice.InvoiceRepository
import java.io.File

class ShareInvoiceUseCase(private val invoiceRepository: InvoiceRepository) {
    fun execute(context: Context, file: File) {
        invoiceRepository.shareInvoice(context, file)
    }
}
