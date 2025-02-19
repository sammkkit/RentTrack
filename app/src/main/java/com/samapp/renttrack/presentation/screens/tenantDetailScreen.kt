package com.samapp.renttrack.presentation.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.presentation.components.DatePickerTextField
import com.samapp.renttrack.presentation.components.PhotoPickingComponent
import com.samapp.renttrack.presentation.viewmodels.TenantViewModel
import java.time.LocalDate
import com.samapp.renttrack.data.local.model.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantDetailScreen(
    tenantId: Int,
    onBack: () -> Unit
) {
    val tenantViewModel: TenantViewModel = hiltViewModel()
    val context = LocalContext.current

    LaunchedEffect(tenantId) {
        tenantViewModel.getTenantById(tenantId)
    }

    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var monthlyRent by remember { mutableStateOf("") }
    var debt by remember { mutableStateOf("") }
    var tenantHouseNumber by remember { mutableStateOf("") }
    var deposit by remember { mutableStateOf("") }
    var rentDueDate: LocalDate? by remember { mutableStateOf(null) }
    var isEmailValid by remember { mutableStateOf(true) }

    val tenantState by tenantViewModel.tenantState.collectAsState()
    when (tenantState) {
        is Result.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        is Result.Error -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Tenant not found", Toast.LENGTH_SHORT).show()
                onBack()
            }
            return
        }

        is Result.Success -> {
            val tenant = (tenantState as Result.Success<Tenant>).data
            if (tenant == null) {
                LaunchedEffect(Unit) {
                    Toast.makeText(context, "Tenant not found", Toast.LENGTH_SHORT).show()
                    onBack()
                }
                return
            }
            // Proceed with using tenant data

            if (tenant == null) {
                Toast.makeText(context, "Tenant not found", Toast.LENGTH_SHORT).show()
                onBack()
                return
            }
            name = tenant.name
            email = tenant.email ?: ""
            contact = tenant.contact
            monthlyRent = tenant.monthlyRent?.toString() ?: ""
            tenantHouseNumber = tenant.tenantHouseNumber
            deposit = tenant.deposit?.toString() ?: ""
            debt = tenant.outstandingDebt?.toString() ?: ""
            photoUri = tenant.photoUri?.let { Uri.parse(it) }
            rentDueDate = tenant.rentDueDate

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Tenant Detail", fontSize = 24.sp, color = Color.Black) },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.Black
                                )
                            }
                        },
                        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.White)
                    )
                },
                bottomBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            tenantViewModel.updateTenant(
                                Tenant(
                                    id = tenantId,
                                    name = name,
                                    email = email,
                                    contact = contact,
                                    monthlyRent = monthlyRent.toDoubleOrNull(),
                                    tenantHouseNumber = tenantHouseNumber,
                                    deposit = deposit.toDoubleOrNull(),
                                    photoUri = photoUri?.toString(),
                                    rentDueDate = rentDueDate,
                                    outstandingDebt = debt.toDoubleOrNull()
                                )
                            )
                            onBack()
                            Toast.makeText(context, "Tenant updated", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Update")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Update")
                        }

                        Button(onClick = {
                            tenantViewModel.deleteTenant(tenant)
                            Toast.makeText(context, "Tenant deleted", Toast.LENGTH_SHORT).show()
                            onBack()

                        }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Delete")
                        }
                    }
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PhotoPickingComponent(onPhotoPicked = { photoUri = it })
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Tenant Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = contact,
                        onValueChange = { contact = it.take(10) },
                        label = { Text("Contact") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = tenantHouseNumber,
                        onValueChange = { tenantHouseNumber = it },
                        label = { Text("House Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = monthlyRent,
                        onValueChange = { monthlyRent = it },
                        label = { Text("Monthly Rent") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DatePickerTextField(
                        modifier = Modifier.fillMaxWidth(),
                        onDateSelected = { rentDueDate = it }
                    )

                    Text(
                        "Optional Details",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            isEmailValid = it.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
                        },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = !isEmailValid
                    )
                    OutlinedTextField(
                        value = deposit,
                        onValueChange = { deposit = it },
                        label = { Text("Deposit") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = debt,
                        onValueChange = { debt = it },
                        label = { Text("Outstanding Debt") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
