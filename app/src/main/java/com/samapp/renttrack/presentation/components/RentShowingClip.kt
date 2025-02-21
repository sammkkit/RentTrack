package com.samapp.renttrack.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RentShowingClip(
    modifier: Modifier = Modifier,
    rent: Int = 0
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp)) // Rounded corners
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(16.dp))
            .padding(top = 4.dp,bottom = 4.dp,start = 16.dp,end = 16.dp)

        ,
        contentAlignment = Alignment.Center // Center the text
    ) {
        Text(
            text = "₹$rent",
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun RentShowingClipPreview(){
    RentShowingClip()
}