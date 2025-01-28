package com.samapp.renttrack.data.local.TypeConverter

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter


class ColorTypeConverter {

    fun fromIntToColor(colorInt: Int): Color {
        return Color(colorInt)
    }

    fun fromColorToInt(color: Color): Int {
        return color.toArgb()
    }
}