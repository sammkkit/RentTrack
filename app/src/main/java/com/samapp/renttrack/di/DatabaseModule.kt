package com.samapp.renttrack.di

import android.content.Context
import androidx.room.Room
import com.samapp.renttrack.data.local.dao.PaymentHistoryDao
import com.samapp.renttrack.data.local.dao.TenantDao
import com.samapp.renttrack.data.local.database.AppDatabase
import com.samapp.renttrack.data.repository.PaymentHistoryRepository
import com.samapp.renttrack.data.repository.TenantRepository
import com.samapp.renttrack.domain.usecases.Tenants.AddTenantUseCase
import com.samapp.renttrack.domain.usecases.Tenants.DeleteTenantUseCase
import com.samapp.renttrack.domain.usecases.Tenants.GetAllTenantsUseCase
import com.samapp.renttrack.domain.usecases.Tenants.GetTenantByIdUseCase
import com.samapp.renttrack.domain.usecases.Tenants.UpdateTenantUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context):AppDatabase{
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "rent_track_database"
        ).build()
    }
    @Provides
    fun provideTenantDao(database: AppDatabase): TenantDao {
        return database.tenantDao()
    }

    @Provides
    fun providePaymentHistoryDao(database: AppDatabase): PaymentHistoryDao {
        return database.paymentHistoryDao()
    }

    @Provides
    @Singleton
    fun provideTenantRepository(tenantDao: TenantDao): TenantRepository {
        return TenantRepository(tenantDao)
    }
    @Provides
    @Singleton
    fun providePaymentHistoryRepository(paymentHistoryDao: PaymentHistoryDao): PaymentHistoryRepository {
        return PaymentHistoryRepository(paymentHistoryDao)
    }

    // Provide UseCases
    @Provides
    @Singleton
    fun provideGetAllTenantsUseCase(tenantRepository: TenantRepository): GetAllTenantsUseCase {
        return GetAllTenantsUseCase(tenantRepository)
    }

    @Provides
    @Singleton
    fun provideGetTenantByIdUseCase(tenantRepository: TenantRepository): GetTenantByIdUseCase {
        return GetTenantByIdUseCase(tenantRepository)
    }

    @Provides
    @Singleton
    fun provideAddTenantUseCase(tenantRepository: TenantRepository): AddTenantUseCase {
        return AddTenantUseCase(tenantRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteTenantUseCase(tenantRepository: TenantRepository): DeleteTenantUseCase {
        return DeleteTenantUseCase(tenantRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateTenantUseCase(tenantRepository: TenantRepository): UpdateTenantUseCase {
        return UpdateTenantUseCase(tenantRepository)
    }
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext applicationContext: Context): Context {
        return applicationContext
    }
}