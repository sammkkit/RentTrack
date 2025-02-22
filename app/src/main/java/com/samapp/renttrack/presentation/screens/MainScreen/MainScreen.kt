package com.samapp.renttrack.presentation.screens.MainScreen

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.samapp.renttrack.presentation.navigation.BottomNavigationBar
import com.samapp.renttrack.presentation.navigation.Screen
import com.samapp.renttrack.presentation.screens.AddTenantScreen
import com.samapp.renttrack.presentation.screens.HomeScreen
import com.samapp.renttrack.presentation.screens.SummaryScreen
import com.samapp.renttrack.presentation.screens.TenantDetailScreen

//
//@Composable
//fun MainScreen(navController: NavHostController) {
//    Scaffold(
//            bottomBar = {
//                BottomNavigationBar(modifier = Modifier, navController = navController)
//            }
//    ) {padding->
//        RootNavigationGraph(
//            modifier = Modifier.padding(padding),
//            navController = navController
//        )
//    }
//}
@Composable
fun MainScreen() {
    val rootNavController = rememberNavController()
    val enterTransition: EnterTransition = fadeIn() + slideInHorizontally(initialOffsetX = { it })
    val exitTransition: ExitTransition = fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
    val popEnterTransition: EnterTransition = fadeIn() + slideInHorizontally(initialOffsetX = { -it })
    val popExitTransition: ExitTransition = fadeOut() + slideOutHorizontally(targetOffsetX = { it })

    NavHost(
        navController = rootNavController,
        startDestination = Screen.BottomNavigation.route,
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
    ) {
        // Navigation Graph with BottomBar (Home & Summary)
        navigation(startDestination = Screen.Home.route, route = Screen.BottomNavigation.route) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onFabClick = { rootNavController.navigate(Screen.AddTenantNavGraph.route) },
                    navController = rootNavController
                )
            }
            composable(Screen.Summary.route) {
                SummaryScreen(
                    navController = rootNavController
                )
            }
        }
        // Separate Navigation Graph for AddTenant (No BottomBar)
        navigation(startDestination = Screen.AddTenant.route, route = Screen.AddTenantNavGraph.route) {
            composable(
                route =Screen.AddTenant.route,
                enterTransition = { enterTransition },
                exitTransition = { exitTransition },
                popEnterTransition = { popEnterTransition },
                popExitTransition = { popExitTransition }
            ) {
                AddTenantScreen(
                    onBack = { rootNavController.popBackStack() }
                )
            }

            composable(
                route = Screen.TenantDetails.route,
                arguments = listOf(navArgument("tenantId") { type = NavType.IntType })
            ) { backStackEntry ->
                val tenantId = backStackEntry.arguments?.getInt("tenantId") ?: return@composable
                TenantDetailScreen(
                    tenantId = tenantId,
                    onBack = { rootNavController.popBackStack() }
                )
            }
        }
    }
}