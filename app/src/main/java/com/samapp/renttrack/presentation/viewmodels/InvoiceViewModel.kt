package com.samapp.renttrack.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.samapp.renttrack.data.local.model.PaymentHistory
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.domain.usecases.InvoiceUseCases.GenerateInvoiceUseCase
import com.samapp.renttrack.domain.usecases.InvoiceUseCases.GenerateTransactionInvoiceUseCase
import com.samapp.renttrack.domain.usecases.InvoiceUseCases.ShareInvoiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val generateInvoiceUseCase: GenerateInvoiceUseCase,
    private val shareInvoiceUseCase: ShareInvoiceUseCase,
    private val generateTransactionInvoiceUseCase: GenerateTransactionInvoiceUseCase
) : ViewModel() {

    private val _invoiceFile = MutableStateFlow<File?>(null)
    val invoiceFile: StateFlow<File?> get() = _invoiceFile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _TransactioninvoiceFile = MutableStateFlow<File?>(null)
    val TransactioninvoiceFile: StateFlow<File?> get() = _TransactioninvoiceFile.asStateFlow()

    suspend fun generateRentInvoice(tenant: Tenant, amount: String, rentDate: String): File? {
        _isLoading.value = true

        return withContext(Dispatchers.IO) {
            val file = generateInvoiceUseCase.execute(tenant, amount, rentDate).firstOrNull()
            _invoiceFile.value = file
            _isLoading.value = false
            file
        }
    }

    suspend fun generateTransactionInvoice(transactions:List<PaymentHistory>?):File?{
        _isLoading.value = true
        return withContext(Dispatchers.IO){
            val file = generateTransactionInvoiceUseCase.execute(transactions).firstOrNull()
            _TransactioninvoiceFile.value = file
            _isLoading.value = false
            file
        }
    }


    fun shareInvoice(context: Context, file: File) {
        shareInvoiceUseCase.execute(context, file)
    }
}

