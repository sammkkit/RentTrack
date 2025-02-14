package com.samapp.renttrack.domain.usecases.Tenants

import com.samapp.renttrack.data.local.TypeConverter.ColorTypeConverter
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.data.repository.TenantRepository
import com.samapp.renttrack.util.getRandomColor
import javax.inject.Inject

class AddTenantUseCase @Inject constructor(private val tenantRepository: TenantRepository) {
    suspend fun execute(tenant: Tenant) {
        val randomColor = getRandomColor()
        val tenantWithColor = tenant.copy(avatarBackgroundColor = ColorTypeConverter().fromColorToInt(randomColor))
        tenantRepository.insertTenant(tenantWithColor)
    }
}