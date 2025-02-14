package com.samapp.renttrack.domain.usecases.Tenants

import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.data.repository.TenantRepository
import javax.inject.Inject

class UpdateTenantUseCase @Inject constructor(private val tenantRepository: TenantRepository) {
    suspend fun execute(tenant: Tenant) {
        tenantRepository.updateTenant(tenant)
    }
}