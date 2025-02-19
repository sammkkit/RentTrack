package com.samapp.renttrack.presentation.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.samapp.renttrack.R
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.presentation.components.DatePickerTextField
import com.samapp.renttrack.presentation.components.PhotoPickingComponent
import com.samapp.renttrack.presentation.viewmodels.TenantViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTenantScreen(
    onBack:()->Unit
) {
    val tenantViewModel: TenantViewModel = hiltViewModel()
    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var monthlyRent by remember { mutableStateOf("") }
    var debt by remember { mutableStateOf("") }
    var tenantHouseNumber by remember { mutableStateOf("") }
    var deposit by remember { mutableStateOf("") }
    var rentDueDate: LocalDate? by remember { mutableStateOf(null) }
    var addDetailsSection by remember { mutableStateOf(false) }
    var isEmailValid by remember { mutableStateOf(true) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Tenant",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {  // ✅ Fixed Back Button Click
                        Icon(
                            imageVector = Icons.Default.ArrowBack, // Standard back arrow
                            contentDescription = "Back",
                            tint =Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if(name.isEmpty() || contact.isEmpty() || monthlyRent.isEmpty() || rentDueDate ==null ){
                                Log.d("addTenant","${
                                    Tenant(
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
                                }")
                                Toast.makeText(context, "Fill All Details", Toast.LENGTH_SHORT).show()
                                return@clickable
                            }
                            tenantViewModel.addTenant(
                                Tenant(
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
                        }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Add",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    ) {padding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top= 16.dp,start = 16.dp,end = 16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PhotoPickingComponent(
                onPhotoPicked = { uri ->
                    photoUri = uri
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            //TODO - add card starting here
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
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
                            focusedBorderColor = Color.DarkGray,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.DarkGray,
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
                            focusedBorderColor = Color.DarkGray,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.DarkGray,
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
                            focusedBorderColor = Color.DarkGray,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.DarkGray,
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
                            focusedBorderColor = Color.DarkGray,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.DarkGray,
                        )
                    )
                    DatePickerTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onDateSelected = { selectedDate ->
                            rentDueDate = selectedDate
                        }
                    )
                    //TODO - make "add fields"  text(Red color) those are optional that will include
                    //TODO - email , deposit and outstanding debt.
                    val sign = if (addDetailsSection) "- " else "+ "
                    Text(
                        text = "${sign}Add Details( optional )",
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .clickable {
                                addDetailsSection = !addDetailsSection
                            },
                        color = Color.Red
                    )
                    if (addDetailsSection) {

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
                                focusedBorderColor = if (isEmailValid) Color.DarkGray else Color.Red,
                                unfocusedBorderColor = if (isEmailValid) Color.Gray else Color.Red,
                                focusedLabelColor = if (isEmailValid) Color.DarkGray else Color.Red,
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
                                focusedBorderColor = Color.DarkGray,
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = Color.DarkGray,
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
                                focusedBorderColor = Color.DarkGray,
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = Color.DarkGray,
                            )
                        )

                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun AddTenantScreenPreview() {
//    AddTenantScreen()
//}

