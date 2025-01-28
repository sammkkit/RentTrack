package com.samapp.renttrack.util

import androidx.compose.ui.graphics.Color

fun getRandomColor(): Color {
    return  Color(
        red = (100..255).random() / 255f,
        green = (0..255).random() / 255f,
        blue = (0..255).random() / 255f
    )
}
