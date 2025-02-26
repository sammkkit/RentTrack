package com.samapp.renttrack.presentation.screens


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.samapp.renttrack.R
import com.samapp.renttrack.data.local.model.PaymentHistory
import com.samapp.renttrack.data.local.model.PaymentType
import com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.presentation.components.MonthSelectionDropDown
import com.samapp.renttrack.presentation.navigation.Screen
import com.samapp.renttrack.presentation.viewmodels.PaymentHistoryViewModel
import com.samapp.renttrack.presentation.viewmodels.TenantViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun tenantDetailScreen(
    tenantId: Int,
    onBack: () -> Unit,
    onUpdate: (Int)->Unit,
    navController : NavHostController
) {
    val tenantViewModel: TenantViewModel = hiltViewModel()
    val paymentHistoryViewModel: PaymentHistoryViewModel = hiltViewModel()
    val colors = MaterialTheme.colorScheme
    LaunchedEffect(tenantId) {
        launch(Dispatchers.IO) {
            tenantViewModel.getTenantById(tenantId)
        }
    }

    val tenantState by tenantViewModel.tenantState.collectAsState()
    val tenant = (tenantState as? Result.Success<Tenant>)?.data
    val context = LocalContext.current

    var menuExpanded by remember { mutableStateOf(false) }
    var showDeleteDialogBox by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${tenant?.name ?: "Unknown"}", fontSize = 24.sp, color = colors.onSurface) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = colors.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        menuExpanded = !menuExpanded

                    },
                        modifier = Modifier.background(colors.surface, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = colors.onSurfaceVariant
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier.background(colors.surfaceVariant, shape = RoundedCornerShape(8.dp))
                    ) {
                        DropdownMenuItem(
                            text = { Text("Update Details",color = colors.onSurface) },
                            onClick = {
                                menuExpanded = false
                                onUpdate(tenantId)
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = colors.onSurface,
                                leadingIconColor = colors.primary
                            )
                        )
                        DropdownMenuItem(
                            text = { Text("Delete Tenant",color = colors.error) },
                            onClick = {
                                menuExpanded = false
                                showDeleteDialogBox = true
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = colors.error,
                                leadingIconColor = colors.error
                            )
                        )

                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.surface)
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = colors.surface,
                modifier = Modifier
                    .clickable {
                        navController.navigate(Screen.TransactionHistory.createRoute(tenantId))
                    },
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        ,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.transactionhistory),
                        contentDescription = "transaction history",
                        tint = colors.onSurface,
                        modifier = Modifier
                            .size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Transactions",
                        fontSize = 22.sp
                    )
                }
            }
        }
    ) {  padding ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when (val state = tenantState) {
                is Result.Loading -> {
                    CircularProgressIndicator()
                }

                is Result.Error -> {
                    LaunchedEffect(Unit) {
                        Toast.makeText(context, "Tenant not found", Toast.LENGTH_SHORT).show()
                        onBack()
                    }
                }

                is Result.Success -> {
                    val tenant = state.data as Tenant
                    var showPaymentDialog by remember { mutableStateOf(false) }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Name: ${tenant.name}", fontSize = 20.sp)
                        Text(text = "Rent: ${tenant.monthlyRent}", fontSize = 20.sp)
                        Text(text = "Contact: ${tenant.contact}", fontSize = 20.sp)
                        Text(text = "id: ${tenant.id}", fontSize = 20.sp)
                        Text(text = "email: ${tenant.email}", fontSize = 20.sp)
                        Text(text = "avatarBackgroundColor: ${tenant.avatarBackgroundColor}", fontSize = 20.sp)
                        Text(text = "deposit: ${tenant.deposit}", fontSize = 20.sp)
                        Text(text = "outstandingDebt: ${tenant.outstandingDebt}", fontSize = 20.sp)
                        Text(text = "tenantHouseNumber: ${tenant.tenantHouseNumber}", fontSize = 20.sp)
                        Text(text = "filePath: ${tenant.filePath}", fontSize = 20.sp)
                        Button(
                            onClick = { showPaymentDialog = true }, // Open payment dialog
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Record Payment")
                        }

                    }
                    if (showPaymentDialog) {
                        var paymentAmount by remember { mutableStateOf("") }
                        var selectedMonth by remember { mutableStateOf<YearMonth>(YearMonth.now()) }
                        var selectedPaymentType by remember { mutableStateOf(PaymentType.RENT) }
                        var expanded by remember { mutableStateOf(false) } // Controls dropdown visibility
                        AlertDialog(
                            onDismissRequest = { showPaymentDialog = false },
                            title = { Text("Record Payment") },
                            text = {
                                Column {
                                    OutlinedTextField(
                                        value = paymentAmount,
                                        onValueChange = { paymentAmount = it },
                                        label = { Text("Amount") },
                                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                                    )
                                    ExposedDropdownMenuBox(
                                        expanded = expanded,
                                        onExpandedChange = { expanded = it }
                                    ) {
                                        OutlinedTextField(
                                            value = selectedPaymentType.name,
                                            onValueChange = {},
                                            readOnly = true,
                                            label = { Text("Payment Type") },
                                            trailingIcon = {
                                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                            },
                                            modifier = Modifier.menuAnchor().fillMaxWidth()
                                        )
                                        DropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false }
                                        ) {
                                            PaymentType.values().forEach { type ->
                                                DropdownMenuItem(
                                                    text = { Text(type.name) },
                                                    onClick = {
                                                        selectedPaymentType = type
                                                        expanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                    Spacer(Modifier.height(10.dp))
                                    if(selectedPaymentType.name == "RENT") {
                                        MonthSelectionDropDown(
                                            selectedMonth = selectedMonth,
                                            onMonthSelected = { it ->
                                                selectedMonth = it
                                            }
                                        )
                                    }

                                }
                            },
                            confirmButton = {
                                Button(onClick = {
                                    // Call ViewModel function to save payment
                                    paymentHistoryViewModel.addPaymentHistory(
                                        PaymentHistory(
                                            tenantId = tenantId,
                                            paymentAmount = paymentAmount.toDouble(),
                                            paymentForWhichMonth = selectedMonth,
                                            paymentType = selectedPaymentType
                                        )
                                    )
                                    showPaymentDialog = false
                                }) {
                                    Text("Save")
                                }
                            },
                            dismissButton = {
                                Button(onClick = { showPaymentDialog = false }) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }

                }
            }
        }

    }
    if (showDeleteDialogBox) {
        AlertDialog(
            onDismissRequest = { showDeleteDialogBox = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this tenant? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialogBox = false
                        tenantViewModel.deleteTenant(tenant!!)
                        Toast.makeText(
                            context,
                            "Tenant Deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                        onBack()
                    }
                ) {
                    Text("Delete", color = colors.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialogBox = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
//@Preview
//@Composable
//fun tenantDetailScreenPreview() {
//    tenantDetailScreen(tenantId = 1, onBack = {}, tenantViewModel = hiltViewModel())
//}