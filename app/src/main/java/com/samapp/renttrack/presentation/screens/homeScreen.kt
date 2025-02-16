package com.samapp.renttrack.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.presentation.components.TenantComponent
import com.samapp.renttrack.presentation.navigation.BottomNavigationBar
import com.samapp.renttrack.presentation.navigation.CustomTopAppBar
import com.samapp.renttrack.presentation.navigation.Screen
import com.samapp.renttrack.presentation.viewmodels.TenantViewModel
import okhttp3.internal.wait
import java.time.LocalDate
import kotlin.random.Random

// Function to generate random names with random characters
fun generateRandomName(): String {
    val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    return (1..10) // 10 characters for the name
        .map { characters.random() }
        .joinToString("")
}

val mockTenants = listOf(
    Tenant(
        id = 1,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 2, 1),
        monthlyRent = 1500.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 2,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 2, 5),
        monthlyRent = 2000.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 3,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 2, 10),
        monthlyRent = 1800.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 4,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 2, 15),
        monthlyRent = 2200.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 5,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 2, 20),
        monthlyRent = 1700.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 6,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 2, 25),
        monthlyRent = 1600.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 7,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 3, 1),
        monthlyRent = 2100.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 8,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 3, 5),
        monthlyRent = 1900.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 9,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 3, 10),
        monthlyRent = 2300.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 10,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 3, 15),
        monthlyRent = 2400.0,
        avatarBackgroundColor = Random.nextInt()
    )
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    onFabClick: ()->Unit
) {
    val tenantViewModel: TenantViewModel = hiltViewModel()
    val tenantsListState by tenantViewModel.tenantListState.collectAsState()
    Scaffold(
        modifier = Modifier,
        bottomBar = { BottomNavigationBar(
            navController = navController,
            modifier = Modifier
        ) },
        topBar = {
            CustomTopAppBar(title = "Tenants")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onFabClick() },
                containerColor = Color(0xFF539DF3),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Tenant")
            }
        },
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
           when(tenantsListState){
               is Result.Loading -> {
                   CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
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
                   tenants?.let {
                       LazyColumn {
                           itemsIndexed(it) { _, tenant ->
                               TenantComponent(
                                   modifier = Modifier,
                                   tenant = tenant
                               )
                           }
                       }
                   }
               }
           }
        }
    }
}

