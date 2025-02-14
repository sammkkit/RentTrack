package com.samapp.renttrack.domain.usecases.Tenants

import com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.data.repository.TenantRepository
import javax.inject.Inject

class SearchTenantsByNameUseCase@Inject constructor(private val tenantRepository: TenantRepository) {
    suspend fun execute(nameQuery: String): Result<List<Tenant>> {
        return tenantRepository.searchTenantsByName(nameQuery)
    }
}