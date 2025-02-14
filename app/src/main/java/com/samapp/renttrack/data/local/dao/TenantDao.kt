package com.samapp.renttrack.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.data.local.model.Tenant

@Dao
interface TenantDao {
    @Insert
    suspend fun insertTenant(tenant:Tenant)

    @Query("SELECT * FROM tenant_table")
    suspend fun getAllTenants() :List<Tenant>

    @Update
    suspend fun updateTenant(tenant: Tenant)

    @Delete
    suspend fun deleteTenant(tenant: Tenant)

    @Query("DELETE FROM tenant_table WHERE id = :tenantId")
    suspend fun deleteTenantById(tenantId: Int)

    @Query("SELECT * FROM tenant_table WHERE id = :tenantId")
    suspend fun getTenantById(tenantId: Int): Tenant?

    @Query("SELECT * FROM tenant_table WHERE name LIKE :nameQuery")
    suspend fun searchTenantsByName(nameQuery: String): List<Tenant>
}