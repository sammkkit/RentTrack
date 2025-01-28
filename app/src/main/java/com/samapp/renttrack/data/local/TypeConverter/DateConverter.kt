package com.samapp.renttrack.data.local.TypeConverter

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromStringToDate(value:String?): LocalDate?{
        return value?.let {
            LocalDate.parse(it,formatter)
        }
    }

    @TypeConverter
    fun fromDateToString(date:LocalDate?):String?{
        return date?.format(formatter)
    }

}