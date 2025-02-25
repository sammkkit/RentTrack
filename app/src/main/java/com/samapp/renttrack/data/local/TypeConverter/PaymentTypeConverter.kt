package com.samapp.renttrack.data.local.TypeConverter

import androidx.room.TypeConverter
import com.samapp.renttrack.data.local.model.PaymentType

class PaymentTypeConverter {
    @TypeConverter
    fun fromPaymentType(value: PaymentType): String {
        return value.name
    }

    @TypeConverter
    fun toPaymentType(value: String): PaymentType {
        return PaymentType.valueOf(value)
    }
}