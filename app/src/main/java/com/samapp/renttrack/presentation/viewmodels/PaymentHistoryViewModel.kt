package com.samapp.renttrack.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samapp.renttrack.data.local.model.PaymentHistory
import com.samapp.renttrack.data.repository.PaymentHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import  com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.domain.usecases.PaymentHistory.AddPaymentHistoryUseCase
import com.samapp.renttrack.domain.usecases.PaymentHistory.GetAllPaymentHistoryUseCase
import com.samapp.renttrack.domain.usecases.PaymentHistory.GetPaymentHistoryForTenantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class PaymentHistoryViewModel @Inject constructor(
    private val getPaymentHistoryForTenantUseCase: GetPaymentHistoryForTenantUseCase,
    private val addPaymentHistoryUseCase: AddPaymentHistoryUseCase,
    private val getAllPaymentHistoryUseCase: GetAllPaymentHistoryUseCase
) : ViewModel() {

    private val _paymentHistoryState = MutableStateFlow<Result<List<PaymentHistory>>>(Result.Loading())
    val paymentHistoryState: StateFlow<Result<List<PaymentHistory>>> = _paymentHistoryState

    fun getPaymentHistoryForTenant(tenantId: Int) {
        viewModelScope.launch {
            _paymentHistoryState.value = Result.Loading()
            _paymentHistoryState.value = getPaymentHistoryForTenantUseCase(tenantId)
        }
    }

    fun addPaymentHistory(paymentHistory: PaymentHistory) {
        viewModelScope.launch {
            val result = addPaymentHistoryUseCase(paymentHistory)
            if (result is Result.Success<*>) {
                getPaymentHistoryForTenant(paymentHistory.tenantId)
            } else {
                _paymentHistoryState.value = Result.Error("Failed to add payment history")
            }
        }
    }

    fun getAllPaymentHistory() {
        viewModelScope.launch {
            _paymentHistoryState.value = Result.Loading()
            _paymentHistoryState.value = getAllPaymentHistoryUseCase()
        }
    }
}
