package com.samapp.renttrack.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.samapp.renttrack.R
//import com.samapp.renttrack.presentation.components.CustomTextField
import com.samapp.renttrack.presentation.components.DatePickerTextField
import com.samapp.renttrack.presentation.components.PhotoPickingComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTenantScreen(
    modifier: Modifier = Modifier
) {
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var monthlyRent by remember { mutableStateOf("") }
    var tenantHouseNumber by remember { mutableStateOf("") }
    var deposit by remember { mutableStateOf("") }
    var rentDueDate by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
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
                            Spacer(modifier=Modifier.width(4.dp))
                            if(contact.length >= 1){
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
                        .clip( RoundedCornerShape(16.dp))
                        .padding(bottom = 16.dp)
                    ,
                    label = { Text("Tenant House Number") },
                    value = tenantHouseNumber,
                    onValueChange = { tenantHouseNumber = it },
                    maxLines = 1,
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Home,
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
                    label = { Text("Monthly Rent") },
                    value = deposit,
                    onValueChange = { deposit = it },
                    maxLines = 1,
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.rupee),
                            contentDescription = "Tenant Name",
                            modifier=Modifier.size(20.dp)
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
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    onDateSelected = { selectedDate ->
                        rentDueDate = selectedDate
                    }
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddTenantScreenPreview() {
    AddTenantScreen()
}

