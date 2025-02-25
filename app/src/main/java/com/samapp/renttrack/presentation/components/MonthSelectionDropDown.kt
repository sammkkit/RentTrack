package com.samapp.renttrack.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MonthSelectionDropDown(
    selectedMonth:YearMonth,
    onMonthSelected: (YearMonth) -> Unit
){
    var showMonthDropdown by remember { mutableStateOf(false) }
    val monthsList = (0..11).map { YearMonth.now().minusMonths(it.toLong()) }

    Box {
        OutlinedButton(
            onClick = { showMonthDropdown = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("${selectedMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${selectedMonth.year}")
        }

        DropdownMenu(
            expanded = showMonthDropdown,
            onDismissRequest = { showMonthDropdown = false }
        ) {
            monthsList.forEach { month ->
                DropdownMenuItem(
                    onClick = {
                        onMonthSelected(month)
                        showMonthDropdown = false
                    },
                    text = {
                        Text("${month.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${month.year}")
                    }
                )
            }
        }
    }
}
