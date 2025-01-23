package com.samapp.renttrack.di

import android.content.Context
import androidx.room.Room
import com.samapp.renttrack.data.local.dao.PaymentHistoryDao
import com.samapp.renttrack.data.local.dao.TenantDao
import com.samapp.renttrack.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}