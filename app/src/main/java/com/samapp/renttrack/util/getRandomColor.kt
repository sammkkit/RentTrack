package com.samapp.renttrack.util

import androidx.compose.ui.graphics.Color

fun getRandomColor(): Color {
    val colors = listOf(
        Color.Red, Color.Green, Color.Blue, Color.Magenta, Color.Cyan, Color.Yellow
    )
    return colors.random()
}
