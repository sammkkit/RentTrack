package com.samapp.renttrack.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.presentation.components.TenantComponent
import com.samapp.renttrack.presentation.navigation.BottomNavigationBar
import com.samapp.renttrack.presentation.navigation.CustomTopAppBar
import com.samapp.renttrack.presentation.navigation.Screen
import com.samapp.renttrack.presentation.viewmodels.TenantViewModel
import java.time.LocalDate
import kotlin.random.Random
val TAG ="HomeScreen"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    onFabClick: ()->Unit
) {
    val tenantViewModel: TenantViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        tenantViewModel.fetchAllTenants()
    }
    val tenantsListState by tenantViewModel.tenantListState.collectAsState()

    Scaffold(
        modifier = Modifier,
        bottomBar = {  BottomNavigationBar(
            navController = navController,
            modifier = Modifier
        ) },
        topBar = {
            CustomTopAppBar(title = "Tenants")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onFabClick() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Tenant")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
           when(tenantsListState){
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
                   val tenants = (tenantsListState as Result.Success<List<Tenant>>).data
                   if (tenants.isNullOrEmpty()) {
                       Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                           Text(
                               "No tenants found",
                               style = MaterialTheme.typography.bodyLarge,
                               color = MaterialTheme.colorScheme.onSurface
                           )
                       }
                   } else {
                       LazyColumn {
                           itemsIndexed(tenants) { _, tenant ->
                               TenantComponent(
                                   modifier = Modifier,
                                   tenant = tenant,
                                   onClick = { tenantId ->
                                       Log.d(TAG, "id - $tenantId")
                                       navController.navigate(Screen.TenantDetails.createRoute(tenantId))
                                   }
                               )
                           }
                       }
                   }
               }
           }
        }
    }
}

