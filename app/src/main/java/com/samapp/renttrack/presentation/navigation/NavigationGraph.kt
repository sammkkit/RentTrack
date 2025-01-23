package com.samapp.renttrack.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.samapp.renttrack.presentation.screens.HomeScreen

@Composable
fun NavigationGraph(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen()
        }
        composable(route = Screen.AddTenant.route) {
//            AddTenantScreen()
        }
    }
}