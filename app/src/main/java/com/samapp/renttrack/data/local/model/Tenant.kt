package com.samapp.renttrack.data.local.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "tenant_table",
)
data class Tenant(
    @PrimaryKey(autoGenerate = true)val id:Int = 0,
    val name:String = "",
    val email:String = "",
    val contact:String = "",
    val rentDueDate: LocalDate? = LocalDate.now(),
    val monthlyRent:Double? = 0.0,
    val tenantHouseNumber : String = "",
    val deposit:Double? = 0.0,
    val outstandingDebt:Double? = 0.0,
    val photoUri : String? = null,
    val avatarBackgroundColor: Int = Color.Black.toArgb()
)
