package com.samapp.renttrack.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkerFactory
import com.samapp.renttrack.data.local.dao.PaymentHistoryDao
import com.samapp.renttrack.data.local.dao.TenantDao
import com.samapp.renttrack.data.local.database.AppDatabase
import com.samapp.renttrack.data.local.datastore.ThemePreferencesDataSource
import com.samapp.renttrack.data.local.datastore.ThemeRepository
import com.samapp.renttrack.data.local.datastore.ThemeRepositoryImpl
import com.samapp.renttrack.data.repository.RentInvoiceRepositoryImpl
import com.samapp.renttrack.data.repository.PaymentHistoryRepository
import com.samapp.renttrack.data.repository.TenantRepository
import com.samapp.renttrack.data.repository.TransactionInvoiceRepositoryImpl
import com.samapp.renttrack.domain.repositories.invoice.RentInvoiceRepository
import com.samapp.renttrack.domain.repositories.invoice.TransactionInvoiceRepository
import com.samapp.renttrack.domain.usecases.InvoiceUseCases.GenerateInvoiceUseCase
import com.samapp.renttrack.domain.usecases.InvoiceUseCases.GenerateTransactionInvoiceUseCase
import com.samapp.renttrack.domain.usecases.InvoiceUseCases.ShareInvoiceUseCase
import com.samapp.renttrack.domain.usecases.PaymentHistory.AddPaymentHistoryUseCase
import com.samapp.renttrack.domain.usecases.PaymentHistory.CheckCurrentMonthRentUseCase
import com.samapp.renttrack.domain.usecases.PaymentHistory.GetAllPaymentHistoryUseCase
import com.samapp.renttrack.domain.usecases.PaymentHistory.GetPaymentHistoryForTenantUseCase
import com.samapp.renttrack.domain.usecases.PaymentHistory.GetPaymentInfoForMonthUseCase
import com.samapp.renttrack.domain.usecases.Tenants.AddTenantUseCase
import com.samapp.renttrack.domain.usecases.Tenants.DeleteTenantUseCase
import com.samapp.renttrack.domain.usecases.Tenants.GetAllTenantsUseCase
import com.samapp.renttrack.domain.usecases.Tenants.GetTenantByIdUseCase
import com.samapp.renttrack.domain.usecases.Tenants.UpdateTenantUseCase
import com.samapp.renttrack.domain.usecases.Theme.GetThemeUseCase
import com.samapp.renttrack.domain.usecases.Theme.SaveThemeUseCase
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
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext applicationContext: Context): Context {
        return applicationContext
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





    // Provide Tenant UseCases
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




    // Provide Payment History UseCases
    @Provides
    @Singleton
    fun provideGetPaymentHistoryForTenantUseCase(paymentHistoryRepository: PaymentHistoryRepository): GetPaymentHistoryForTenantUseCase {
        return GetPaymentHistoryForTenantUseCase(paymentHistoryRepository)
    }

    @Provides
    @Singleton
    fun provideAddPaymentHistoryUseCase(
        paymentHistoryRepository: PaymentHistoryRepository,
        getTenantByIdUseCase: GetTenantByIdUseCase,
        updateTenantUseCase: UpdateTenantUseCase
    ): AddPaymentHistoryUseCase {
        return AddPaymentHistoryUseCase(paymentHistoryRepository, getTenantByIdUseCase, updateTenantUseCase)
    }

    @Provides
    @Singleton
    fun provideGetAllPaymentHistoryUseCase(paymentHistoryRepository: PaymentHistoryRepository): GetAllPaymentHistoryUseCase {
        return GetAllPaymentHistoryUseCase(paymentHistoryRepository)
    }
    @Provides
    @Singleton
    fun provideGetPaymentInforForMonthUseCase(paymentHistoryRepository: PaymentHistoryRepository): GetPaymentInfoForMonthUseCase{
        return GetPaymentInfoForMonthUseCase(paymentHistoryRepository)
    }

    @Provides
    @Singleton
    fun provideCheckCurrentMonthRentUseCase(paymentHistoryRepository: PaymentHistoryRepository): CheckCurrentMonthRentUseCase{
        return CheckCurrentMonthRentUseCase(paymentHistoryRepository)
    }


    //Theme Section

    @Provides
    @Singleton
    fun provideThemePreferencesDataSource(@ApplicationContext context: Context): ThemePreferencesDataSource {
        return ThemePreferencesDataSource(context)
    }

    @Provides
    @Singleton
    fun provideThemeRepository(dataSource: ThemePreferencesDataSource): ThemeRepository {
        return ThemeRepositoryImpl(dataSource)
    }

    @Provides
    fun provideGetThemeUseCase(repository: ThemeRepository): GetThemeUseCase {
        return GetThemeUseCase(repository)
    }

    @Provides
    fun provideSaveThemeUseCase(repository: ThemeRepository): SaveThemeUseCase {
        return SaveThemeUseCase(repository)
    }

    //invoice
    // Provide Invoice Repository
    @Provides
    @Singleton
    fun provideInvoiceRepository(@ApplicationContext context: Context): RentInvoiceRepository {
        return RentInvoiceRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideTransactionInvoiceRepository(@ApplicationContext context: Context): TransactionInvoiceRepository{
        return TransactionInvoiceRepositoryImpl(context)
    }

    // Provide Generate Invoice Use Case
    @Provides
    @Singleton
    fun provideGenerateInvoiceUseCase(invoiceRepository: RentInvoiceRepository): GenerateInvoiceUseCase {
        return GenerateInvoiceUseCase(invoiceRepository)
    }
    @Provides
    @Singleton
    fun provideGenerateTransactionInvoiceUseCase(transactionInvoiceRepo: TransactionInvoiceRepository):GenerateTransactionInvoiceUseCase{
        return GenerateTransactionInvoiceUseCase(transactionInvoiceRepo)
    }

    // Provide Share Invoice Use Case
    @Provides
    @Singleton
    fun provideShareInvoiceUseCase(invoiceRepository: RentInvoiceRepository): ShareInvoiceUseCase {
        return ShareInvoiceUseCase(invoiceRepository)
    }

}

