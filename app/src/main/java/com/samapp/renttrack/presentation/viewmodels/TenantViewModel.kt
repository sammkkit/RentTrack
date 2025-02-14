package com.samapp.renttrack.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samapp.renttrack.data.local.TypeConverter.ColorTypeConverter
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.data.repository.TenantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.domain.usecases.Tenants.AddTenantUseCase
import com.samapp.renttrack.domain.usecases.Tenants.DeleteTenantUseCase
import com.samapp.renttrack.domain.usecases.Tenants.GetAllTenantsUseCase
import com.samapp.renttrack.domain.usecases.Tenants.GetTenantByIdUseCase
import com.samapp.renttrack.domain.usecases.Tenants.SearchTenantsByNameUseCase
import com.samapp.renttrack.domain.usecases.Tenants.UpdateTenantUseCase
import com.samapp.renttrack.util.getRandomColor
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TenantViewModel @Inject constructor(
    private val getAllTenantsUseCase: GetAllTenantsUseCase,
    private val getTenantByIdUseCase: GetTenantByIdUseCase,
    private val addTenantUseCase: AddTenantUseCase,
    private val searchTenantsByNameUseCase: SearchTenantsByNameUseCase,
    private val deleteTenantUseCase: DeleteTenantUseCase,
    private val updateTenantUseCase: UpdateTenantUseCase
) : ViewModel(){
    private val _tenantListState = MutableStateFlow<Result<List<Tenant>>>(Result.Loading())
    val tenantListState :  StateFlow<Result<List<Tenant>>> = _tenantListState

    init {
        fetchAllTenants()
    }

    fun fetchAllTenants() {
        viewModelScope.launch {
            _tenantListState.value = Result.Loading()
            _tenantListState.value = getAllTenantsUseCase.execute()
        }
    }

    fun getTenantById(tenantId: Int) {
        viewModelScope.launch {
            _tenantListState.value = Result.Loading()
            val tenant = getTenantByIdUseCase.execute(tenantId)
            _tenantListState.value = tenant
        }
    }

    fun addTenant(tenant: Tenant){
        viewModelScope.launch {
            _tenantListState.value = Result.Loading()
            addTenantUseCase.execute(tenant)
            fetchAllTenants()
        }
    }
    fun searchTenantsByName(nameQuery: String) {
        viewModelScope.launch {
            _tenantListState.value = Result.Loading()
            _tenantListState.value = searchTenantsByNameUseCase.execute(nameQuery)
        }
    }
    fun deleteTenant(tenant: Tenant) {
        viewModelScope.launch {
            _tenantListState.value = Result.Loading()
            deleteTenantUseCase.execute(tenant)
            fetchAllTenants()
        }
    }
    fun updateTenant(tenant: Tenant) {
        viewModelScope.launch {
            _tenantListState.value = Result.Loading()
            updateTenantUseCase.execute(tenant)
            fetchAllTenants()
        }
    }
}