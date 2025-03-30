package com.samapp.renttrack.data.repository

import android.util.Log
import com.samapp.renttrack.data.local.dao.TenantDao
import com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.data.local.model.Tenant
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

const val TAG = "TenantRepository"

class TenantRepository @Inject constructor(
    private val tenantDao: TenantDao
) {
    suspend fun insertTenant(tenant: Tenant) {
        Log.d(TAG, "inserted element = ${tenant}")
        tenantDao.insertTenant(tenant)
    }
    suspend fun updateTenant(tenant: Tenant){
        tenantDao.updateTenant(tenant)
    }
    suspend fun deleteTenant(tenant: Tenant){
        tenantDao.deleteTenant(tenant.id)
    }
    suspend fun deleteTenantById(tenantId: Int) {
        tenantDao.deleteTenantById(tenantId)
    }
    suspend fun getAllTenants() : Result<List<Tenant>>{
        return try{
            val tenants = tenantDao.getAllTenants()
            Result.Success(data = tenants)
        }catch (e:Exception){
            Result.Error(message = e.message)
        }
    }
    suspend fun getTenantById(tenantId: Int): Result<Tenant> {
        return try {
            val tenant = tenantDao.getTenantById(tenantId)
            if (tenant != null) {
                Result.Success(data = tenant)
            } else {
                Result.Error(message = "Tenant Not Found")
            }
        } catch (e: Exception) {
            Result.Error(message = e.message)
        }
    }

    suspend fun searchTenantsByName(nameQuery: String): Result<List<Tenant>> {
        return try {
            val tenants = tenantDao.searchTenantsByName(nameQuery)
            if (tenants.isNotEmpty()) {
                Result.Success(data = tenants)
            } else {
                Result.Error(message = "No tenants found matching the name '$nameQuery'")
            }
        } catch (e: Exception) {
            Result.Error(message = e.message ?: "An unexpected error occurred")
        }
    }

    suspend fun getUpcomingDueTenants(): List<Tenant>{
        return try {
            val today = LocalDate.now()
            val duedateLimit = LocalDate.now().plusDays(3)

            val tenantsFlow = tenantDao.getUpcomingDueTenants(today, duedateLimit)
            tenantsFlow
        } catch (e: Exception) {
            throw e
        }
    }

}