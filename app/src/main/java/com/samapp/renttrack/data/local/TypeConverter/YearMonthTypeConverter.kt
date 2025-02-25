package com.samapp.renttrack.data.local.TypeConverter

import androidx.room.TypeConverter
import java.time.YearMonth

class YearMonthConverter {
    @TypeConverter
    fun fromYearMonth(value: YearMonth): String {
        return value.toString() // Example: "2025-02"
    }

    @TypeConverter
    fun toYearMonth(value: String): YearMonth {
        return YearMonth.parse(value)
    }
}
