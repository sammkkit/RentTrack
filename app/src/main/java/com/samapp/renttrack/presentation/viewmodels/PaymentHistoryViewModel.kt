package com.samapp.renttrack.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samapp.renttrack.data.local.model.PaymentHistory
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import  com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.domain.usecases.PaymentHistory.AddPaymentHistoryUseCase
import com.samapp.renttrack.domain.usecases.PaymentHistory.GetAllPaymentHistoryUseCase
import com.samapp.renttrack.domain.usecases.PaymentHistory.GetPaymentHistoryForTenantUseCase
import com.samapp.renttrack.domain.usecases.PaymentHistory.GetPaymentInfoForMonthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


@HiltViewModel
class PaymentHistoryViewModel @Inject constructor(
    private val getPaymentHistoryForTenantUseCase: GetPaymentHistoryForTenantUseCase,
    private val addPaymentHistoryUseCase: AddPaymentHistoryUseCase,
    private val getAllPaymentHistoryUseCase: GetAllPaymentHistoryUseCase,
    private val getPaymentInfoForMonthUseCase: GetPaymentInfoForMonthUseCase
) : ViewModel() {

    private val _paymentHistoryState = MutableStateFlow<Result<List<PaymentHistory>>>(Result.Loading())
    val paymentHistoryState: StateFlow<Result<List<PaymentHistory>>> = _paymentHistoryState

    private val _AllpaymentHistoryState = MutableStateFlow<Result<List<PaymentHistory>>>(Result.Loading())
    val AllpaymentHistoryState: StateFlow<Result<List<PaymentHistory>>> = _AllpaymentHistoryState

    private val _currentMonthInfoState = MutableStateFlow<Result<Unit>>(Result.Loading())
    val currentMonthInfoState: StateFlow<Result<Unit>> = _currentMonthInfoState


    private val _addPaymentHistoryEvent = MutableSharedFlow<Result<Unit>>()
    val addPaymentHistoryEvent = _addPaymentHistoryEvent.asSharedFlow()

    fun payRentForCurrentMonth(tenant: Tenant){
        viewModelScope.launch {
            _currentMonthInfoState.value = Result.Loading()
            val result = tenant.monthlyRent?.let {
                getPaymentInfoForMonthUseCase.collectRentForTenant(tenant.id,
                    it
                )
            }
            if (result is Result.Success<*>) {
                _currentMonthInfoState.value = Result.Success(Unit)
            } else {
                _currentMonthInfoState.value = Result.Error(result?.message ?: "Unknown Error")
            }
        }
    }

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
                _addPaymentHistoryEvent.emit(Result.Success(Unit))
            } else {
                _addPaymentHistoryEvent.emit(Result.Error("${(result as Result.Error).message}" ?: "Unknown error"))
            }
        }
    }

    fun getAllPaymentHistory() {
        viewModelScope.launch {
            _AllpaymentHistoryState.value = Result.Loading()
            _AllpaymentHistoryState.value = getAllPaymentHistoryUseCase()
        }
    }
}
