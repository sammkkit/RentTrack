package com.samapp.renttrack.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "tenant_table"
)
data class Tenant(
    @PrimaryKey(autoGenerate = true)val id:Int = 0,
    val name:String = "",
    val email:String = "",
    val contact:String = "",
    val rentDueDate: LocalDate= LocalDate.now(),
    val monthlyRent:Double = 0.0,
    val deposit:Double = 0.0,
    val outstandingDebt:Double = 0.0,
    val isPaid: Boolean = false,
)
