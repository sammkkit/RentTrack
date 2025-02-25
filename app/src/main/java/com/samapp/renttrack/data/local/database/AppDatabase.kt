package com.samapp.renttrack.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.samapp.renttrack.data.local.TypeConverter.DateConverter
import com.samapp.renttrack.data.local.TypeConverter.PaymentTypeConverter
import com.samapp.renttrack.data.local.TypeConverter.YearMonthConverter
import com.samapp.renttrack.data.local.dao.PaymentHistoryDao
import com.samapp.renttrack.data.local.dao.TenantDao
import com.samapp.renttrack.data.local.model.PaymentHistory
import com.samapp.renttrack.data.local.model.Tenant

@Database(
    entities = [Tenant::class, PaymentHistory::class],
    version = 2
)
@TypeConverters(DateConverter::class,PaymentTypeConverter::class, YearMonthConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tenantDao(): TenantDao
    abstract fun paymentHistoryDao(): PaymentHistoryDao
}
