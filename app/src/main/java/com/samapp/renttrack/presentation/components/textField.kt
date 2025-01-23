package com.samapp.renttrack.presentation.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    label: String = "label",
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = false
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        maxLines = maxLines,
        singleLine = singleLine,
    )

}

@Preview(showBackground = true)
@Composable
fun Prev(){
    CustomTextField()
}