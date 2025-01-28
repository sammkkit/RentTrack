package com.samapp.renttrack.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samapp.renttrack.data.local.TypeConverter.ColorTypeConverter
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.data.repository.TenantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.util.getRandomColor
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TenantViewModel @Inject constructor(
    private val tenantRepository: TenantRepository
) : ViewModel(){
    private val _tenantListState = MutableStateFlow<Result<List<Tenant>>>(Result.Loading())
    val tenantListState :  StateFlow<Result<List<Tenant>>> = _tenantListState
    init {
        fetchAllTenants()
    }
    fun fetchAllTenants(){
        viewModelScope.launch {
            _tenantListState.value = Result.Loading()
            _tenantListState.value = tenantRepository.getAllTenants()
        }
    }
    fun getTenantById(tenantId: Int) {
        viewModelScope.launch {
            _tenantListState.value = Result.Loading()
            _tenantListState.value = tenantRepository.getTenantById(tenantId)
        }
    }
    fun addTenant(tenant: Tenant){
        viewModelScope.launch {
            _tenantListState.value = Result.Loading()

            val randomColor = getRandomColor()
            val tenantWithColor = tenant.copy(avatarBackgroundColor = ColorTypeConverter().fromColorToInt(randomColor))

            tenantRepository.insertTenant(tenantWithColor)
        }
    }
    fun searchTenantsByName(nameQuery: String) {
        viewModelScope.launch {
            _tenantListState.value = Result.Loading()
            _tenantListState.value = tenantRepository.searchTenantsByName(nameQuery)
        }
    }
    fun deleteTenant(tenant: Tenant) {
        viewModelScope.launch {
            tenantRepository.deleteTenant(tenant)
            fetchAllTenants()
        }
    }
    fun updateTenant(tenant: Tenant) {
        viewModelScope.launch {
            _tenantListState.value = Result.Loading()
            tenantRepository.updateTenant(tenant)
            fetchAllTenants()
        }
    }
}