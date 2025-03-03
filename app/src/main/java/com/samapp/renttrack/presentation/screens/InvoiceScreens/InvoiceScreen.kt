package com.samapp.renttrack.presentation.screens.InvoiceScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.presentation.viewmodels.InvoiceViewModel
import com.samapp.renttrack.presentation.viewmodels.TenantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceScreen(
    tenantId: Int,
    navController: NavHostController
) {
    val tenantViewModel: TenantViewModel = hiltViewModel()
    val tenantState by tenantViewModel.tenantState.collectAsState()
    val tenant = (tenantState as? Result.Success<Tenant>)?.data

    val invoiceViewModel :InvoiceViewModel = hiltViewModel()

    LaunchedEffect(tenantId) {
        tenantViewModel.getTenantById(tenantId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Invoice") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (tenant != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Invoice for ${tenant.name}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("Rent Amount: ${tenant.monthlyRent}")
                    Text("Due Date: ${tenant.rentDueDate}")
                    Spacer(Modifier.height(20.dp))
//                    Button(onClick = { invoiceViewModel.generateRentInvoice(
//                        tenant = tenant,
//                        amount = tenant.monthlyRent.toString(),
//                        rentDate = tenant.rentDueDate.toString()
//                    ) }) {
//                        Text("Download Invoice")
//                    }
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}


