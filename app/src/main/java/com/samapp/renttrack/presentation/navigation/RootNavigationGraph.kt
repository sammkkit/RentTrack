package com.samapp.renttrack.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.samapp.renttrack.presentation.screens.AddTenantScreen
import com.samapp.renttrack.presentation.screens.HomeScreen
import com.samapp.renttrack.presentation.screens.SummaryScreen


@Composable
fun RootNavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // HomeScreen route
        composable(route = Screen.Home.route) {
            HomeScreen(modifier = modifier,navController = navController)
        }

        // SummaryScreen route
        composable(route = Screen.Summary.route) {
            SummaryScreen(modifier = modifier,navController=navController)
        }

        // AddTenantScreen route
        composable(route = Screen.AddTenant.route) {
            AddTenantScreen(modifier = modifier)
        }
    }
}