package com.samapp.renttrack.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.domain.usecases.InvoiceUseCases.GenerateInvoiceUseCase
import com.samapp.renttrack.domain.usecases.InvoiceUseCases.ShareInvoiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val generateInvoiceUseCase: GenerateInvoiceUseCase,
    private val shareInvoiceUseCase: ShareInvoiceUseCase
) : ViewModel() {

    private val _invoiceFile = MutableStateFlow<File?>(null)
    val invoiceFile: StateFlow<File?> get() = _invoiceFile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    fun generateInvoice(tenant: Tenant, amount: String, rentDate: String) {
        viewModelScope.launch {
            _isLoading.value = true
            generateInvoiceUseCase.execute(tenant, amount, rentDate)
                .collect { file ->
                    _invoiceFile.value = file
                    _isLoading.value = false
                }
        }
    }

    fun shareInvoice(context: Context, file: File) {
        shareInvoiceUseCase.execute(context, file)
    }
}

