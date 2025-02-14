package com.samapp.renttrack.domain.usecases.Tenants

import com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.data.repository.TenantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTenantsUseCase@Inject constructor(private val tenantRepository: TenantRepository) {
    suspend fun execute(): Result<List<Tenant>> {
        return tenantRepository.getAllTenants()
    }
}