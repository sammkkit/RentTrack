package com.samapp.renttrack.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.samapp.renttrack.R
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.presentation.components.DatePickerTextField
import com.samapp.renttrack.presentation.components.PhotoPickingComponent
import com.samapp.renttrack.presentation.viewmodels.TenantViewModel
import java.time.LocalDate
import com.samapp.renttrack.data.local.model.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun changeTenantDetailScreen(
    tenantId: Int,
    onBack: () -> Unit
) {
    val tenantViewModel: TenantViewModel = hiltViewModel()
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme

    LaunchedEffect(tenantId) {
        tenantViewModel.getTenantById(tenantId)
    }

    var filepath by remember { mutableStateOf<String?>(null) }
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
            filepath = tenant.filePath
            rentDueDate = tenant.rentDueDate

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Tenant Detail", fontSize = 24.sp, color = colors.onSurface) },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = colors.onSurface
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.surface)
                    )
                },
                bottomBar = {
                    BottomAppBar(
                        containerColor = colors.primary,
                        modifier = Modifier
                            .clickable {
                                tenantViewModel.updateTenant(
                                    Tenant(
                                        id = tenantId,
                                        name = name,
                                        email = email,
                                        contact = contact,
                                        monthlyRent = monthlyRent.toDoubleOrNull(),
                                        tenantHouseNumber = tenantHouseNumber,
                                        deposit = deposit.toDoubleOrNull(),
                                        filePath = filepath,
                                        rentDueDate = rentDueDate,
                                        outstandingDebt = debt.toDoubleOrNull()
                                    )
                                )
                                Toast.makeText(context, "Tenant updated", Toast.LENGTH_SHORT).show()
                                onBack()
                            },
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Update", modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Update",
                                fontSize = 22.sp
                            )
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
                    PhotoPickingComponent(onPhotoPicked = { filepath = it })
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp),
                        label = { Text("Tenant Name") },
                        value = name,
                        onValueChange = { name = it },
                        maxLines = 1,
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Tenant Name"
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )


                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        label = { Text("Contact") },
                        value = contact,
                        onValueChange = { newValue ->
                            if (newValue.length <= 10) {
                                contact = newValue
                            }
                        },
                        maxLines = 1,
                        singleLine = true,
                        leadingIcon = {
                            Row(
                                modifier = Modifier.padding(start = 18.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.phone),
                                    contentDescription = "Tenant Name",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                if (contact.length >= 1) {
                                    Text(
                                        text = "+91",
                                    )
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )


                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .padding(bottom = 16.dp),
                        label = { Text("Tenant House Number") },
                        value = tenantHouseNumber,
                        onValueChange = { tenantHouseNumber = it },
                        maxLines = 1,
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Tenant House Number"
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        label = { Text("Monthly Rent") },
                        value = monthlyRent,
                        onValueChange = { monthlyRent = it },
                        maxLines = 1,
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.rupee),
                                contentDescription = "Monthly Rent",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    DatePickerTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onDateSelected = { selectedDate ->
                            rentDueDate = selectedDate
                        }
                    )

                    Text(
                        "Optional Details",
                        color = colors.error,
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .padding(bottom = 16.dp),
                        label = { Text("Email") },
                        value = email,
                        onValueChange = {
                            email = it
                            isEmailValid =
                                it.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
                        },
                        maxLines = 1,
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email"
                            )
                        },
                        isError = !isEmailValid,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = if (isEmailValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            unfocusedBorderColor = if (isEmailValid) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.error,
                            focusedLabelColor = if (isEmailValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        label = { Text("Deposit") },
                        value = deposit,
                        onValueChange = { deposit = it },
                        maxLines = 1,
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.rupee),
                                contentDescription = "Deposit",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        label = { Text("Debt") },
                        value = debt,
                        onValueChange = { debt = it },
                        maxLines = 1,
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.rupee),
                                contentDescription = "Debt",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}
