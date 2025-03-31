package com.samapp.renttrack.presentation.screens


import android.util.Log
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import com.samapp.renttrack.presentation.components.TenantAvatar
import com.samapp.renttrack.presentation.navigation.Screen
import com.samapp.renttrack.presentation.viewmodels.InvoiceViewModel
import com.samapp.renttrack.presentation.viewmodels.PaymentHistoryViewModel
import com.samapp.renttrack.presentation.viewmodels.TenantViewModel
import com.samapp.renttrack.util.openPdf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
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
    val invoiceViewModel: InvoiceViewModel = hiltViewModel()
    val colors = MaterialTheme.colorScheme

    val tenantState by tenantViewModel.tenantState.collectAsState()
    val currentMonthRentState by paymentHistoryViewModel.currentMonthInfoState.collectAsState()


    val tenant = (tenantState as? Result.Success<Tenant>)?.data
    val context = LocalContext.current
    val invoiceFile = invoiceViewModel.invoiceFile.collectAsState()

    LaunchedEffect(tenantId) {
        launch(Dispatchers.IO) {
            tenantViewModel.getTenantById(tenantId)
            delay(1000)
        }
    }
    LaunchedEffect(Unit) {
        paymentHistoryViewModel.addPaymentHistoryEvent.collectLatest { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(context, "Payment Recorded", Toast.LENGTH_SHORT).show()
                }

                is Result.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> Unit
            }
        }
    }
    LaunchedEffect(currentMonthRentState) {
        when (currentMonthRentState) {
            is Result.Error -> {
                Toast.makeText(context,"${(currentMonthRentState as Result.Error).message}",Toast.LENGTH_SHORT).show()
                paymentHistoryViewModel.resetRentState()
            }
            is Result.Success -> {
                Toast.makeText(context,"Rent Collected",Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.Main).launch {
                    Log.d(
                        "Invoice",
                        "Starting invoice generation for ${tenant?.name}"
                    )

                    val invoice = tenant?.let {
                        invoiceViewModel.generateRentInvoice(
                            it,
                            tenant.monthlyRent.toString(),
                            tenant.rentDueDate.toString()
                        )
                    }

                    if (invoice != null) {
                        Log.d(
                            "Invoice",
                            "Invoice successfully generated: ${invoice.absolutePath}"
                        )
                        openPdf(
                            context,
                            invoice
                        ) // Open the PDF once it's ready
                    } else {
                        Log.e("Invoice", "Invoice generation failed!")
                    }
                }
                paymentHistoryViewModel.resetRentState()
            }
            else ->Unit
        }
    }
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
                    val currentMonthRentState by paymentHistoryViewModel.currentMonthInfoState.collectAsState()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TenantInfoCard(
                            tenant
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Button(
                                onClick = {
                                    paymentHistoryViewModel.payRentForCurrentMonth(tenant)

                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Collect Rent")
                            }
                            Button(
                                onClick = { showPaymentDialog = true }, // Open payment dialog
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Record Payment")
                            }
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
                                        onValueChange = {
                                            if (it.all { char -> char.isDigit() }) {
                                                paymentAmount = it
                                            }else{
                                                Toast.makeText(context,"Invalid input", Toast.LENGTH_SHORT).show()
                                            }
                                        },
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
                                            modifier = Modifier
                                                .menuAnchor()
                                                .fillMaxWidth()
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
                                    if(paymentAmount.isEmpty() || paymentAmount.toDoubleOrNull() == null){
                                        Toast.makeText(context,"Enter Valid Amount", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
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
@Composable
fun TenantInfoCard(tenant: Tenant) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TenantAvatar(tenant)
            Column(
                modifier = Modifier.padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Name: ${tenant.name}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "Rent: ${tenant.monthlyRent}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "Contact: ${tenant.contact}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    "Rent Due Date: ${tenant.rentDueDate.toString()[tenant.rentDueDate.toString().length-2]}${tenant.rentDueDate.toString()[tenant.rentDueDate.toString().length-1]} of every month",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
//@Preview
//@Composable
//fun tenantDetailScreenPreview() {
//    tenantDetailScreen(tenantId = 1, onBack = {}, tenantViewModel = hiltViewModel())
//}