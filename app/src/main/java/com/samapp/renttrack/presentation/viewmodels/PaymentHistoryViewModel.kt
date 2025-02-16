package com.samapp.renttrack.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samapp.renttrack.data.local.model.PaymentHistory
import com.samapp.renttrack.data.repository.PaymentHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import  com.samapp.renttrack.data.local.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PaymentHistoryViewModel @Inject constructor(
    private val paymentHistoryRepository: PaymentHistoryRepository
):ViewModel() {
    private val _paymentHistoryState = MutableStateFlow<Result<List<PaymentHistory>>>(Result.Loading())
    val paymentHistoryState: StateFlow<Result<List<PaymentHistory>>> = _paymentHistoryState

    fun getPaymentHistoryForTenant(tenantId: Int) {
        viewModelScope.launch {
            _paymentHistoryState.value = Result.Loading()
            _paymentHistoryState.value = paymentHistoryRepository.getPaymentHistoryForTenant(tenantId)
        }
    }
    fun addPaymentHistory(paymentHistory: PaymentHistory) {
        viewModelScope.launch {
            paymentHistoryRepository.insertPaymentHistory(paymentHistory)
            getPaymentHistoryForTenant(paymentHistory.tenantId) // Refresh after insertion
        }
    }
    fun getAllPaymentHistory() {
        viewModelScope.launch {
            _paymentHistoryState.value = Result.Loading()
            _paymentHistoryState.value = paymentHistoryRepository.getAllPaymentHistory()
        }
    }

}