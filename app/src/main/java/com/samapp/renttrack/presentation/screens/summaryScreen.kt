package com.samapp.renttrack.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.samapp.renttrack.presentation.navigation.BottomNavigationBar
import com.samapp.renttrack.presentation.navigation.CustomTopAppBar
import com.samapp.renttrack.presentation.viewmodels.TenantViewModel
import com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.presentation.components.SummaryCardBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val tenantViewModel: TenantViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        tenantViewModel.fetchAllTenants()
    }
    val tenantsListState by tenantViewModel.tenantListState.collectAsState()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                modifier = Modifier
            )
        },
        topBar = {
            CustomTopAppBar(title = "Summary")
        },
    ) {padding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            when (tenantsListState) {
                is Result.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier)
                    }
                }

                is Result.Error -> {
                    val errorMessage = (tenantsListState as Result.Error<List<Tenant>>).message
                    Text(
                        text = "Error: $errorMessage",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)

                    )
                }

                is Result.Success -> {
                    val scrollState = rememberScrollState()
                    val tenants = (tenantsListState as Result.Success<List<Tenant>>).data
                    val totalRentCollection = tenants?.sumOf { it.monthlyRent?.toInt() ?: 0 } ?: 0
                    val totalTenants = tenants?.size ?: 0
                    val totalDeposit = tenants?.sumOf { it.deposit?.toInt() ?: 0  }?:0
                    val totalDebt = tenants?.sumOf { it.outstandingDebt?.toInt() ?: 0 } ?: 0
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                        ,
                        horizontalAlignment = Alignment.Start
                    ) {
                        SummaryCardBox(
                            heading = "Monthly Total Collection",
                            amount = "₹$totalRentCollection",
                            tenantInfo = "From $totalTenants Tenants"
                        )
                        Spacer(modifier=Modifier.height(10.dp))
                        Divider()
                        Spacer(modifier=Modifier.height(10.dp))
                        SummaryCardBox(
                            heading = "Total Tenants",
                            amount = "$totalTenants",
                            tenantInfo = ""
                        )
                        Spacer(modifier=Modifier.height(10.dp))
                        Divider()
                        Spacer(modifier=Modifier.height(10.dp))
                        SummaryCardBox(
                            heading = "Total Deposit",
                            amount = "₹$totalDeposit",
                            tenantInfo = "From $totalTenants Tenants"
                        )
                        Spacer(modifier=Modifier.height(10.dp))
                        Divider()
                        Spacer(modifier=Modifier.height(10.dp))
                        SummaryCardBox(
                            heading = "Total Debt",
                            amount = "₹$totalDebt",
                            tenantInfo = "From $totalTenants Tenants"
                        )
                    }
                }
            }
        }
    }
}