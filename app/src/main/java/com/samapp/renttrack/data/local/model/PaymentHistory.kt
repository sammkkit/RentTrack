package com.samapp.renttrack.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "payment_history_table",
    foreignKeys = [ForeignKey(
        entity = Tenant::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("tenantId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class PaymentHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tenantId: Int, // Foreign key reference to Tenant
    val paymentAmount: Double, // Amount paid in this transaction
    val paymentDate: LocalDate = LocalDate.now(), // Date of the payment
    val paymentForWhichMonth: LocalDate = LocalDate.now(), // Month for which the payment was made
)
