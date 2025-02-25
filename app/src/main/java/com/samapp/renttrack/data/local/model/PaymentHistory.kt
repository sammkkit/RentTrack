package com.samapp.renttrack.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.YearMonth

@Entity(
    tableName = "payment_history_table",
    foreignKeys = [ForeignKey(
        entity = Tenant::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("tenantId"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["tenantId"])]
)
data class PaymentHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tenantId: Int,
    val paymentAmount: Double,
    val paymentDate: LocalDate = LocalDate.now(),
    val paymentForWhichMonth: YearMonth = YearMonth.now(),
    val paymentType: PaymentType = PaymentType.RENT
)
enum class PaymentType {
    RENT, DEPOSIT, UTILITY, OTHER
}
