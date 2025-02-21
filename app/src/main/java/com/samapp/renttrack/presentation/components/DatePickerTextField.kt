package com.samapp.renttrack.presentation.components

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
@Composable
fun DatePickerTextField(
    modifier: Modifier = Modifier,
    onDateSelected: (LocalDate) -> Unit
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Trigger DatePickerDialog when clicked
    if (showDatePicker) {
        val datePickerDialog = DatePickerDialog(
            LocalContext.current,
            { _, selectedYear, selectedMonth, selectedDay ->
                val localDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                selectedDate = localDate
                onDateSelected(localDate)
                showDatePicker = false
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline)
            .clickable { showDatePicker = true }
            .padding(16.dp) // Padding inside the Box
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Select Rent Due Date",
                tint = if (selectedDate != null) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant, // Adaptive tint
                modifier = Modifier.size(24.dp) // Ensure icon size is consistent
            )
            Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
            Text(
                text = selectedDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    ?: "Select Rent Due Date",  // Format date if selected
                style = MaterialTheme.typography.bodyLarge,
                color = if (selectedDate != null) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDatePicker() {
    DatePickerTextField(
        onDateSelected = { selectedDate ->
            println("Selected Date: $selectedDate")
        }
    )
}
