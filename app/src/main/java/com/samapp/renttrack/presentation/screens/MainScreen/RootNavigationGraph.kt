package com.samapp.renttrack.presentation.screens.MainScreen

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.samapp.renttrack.presentation.navigation.Screen
import com.samapp.renttrack.presentation.screens.AddTenantScreen
import com.samapp.renttrack.presentation.screens.HomeScreen
import com.samapp.renttrack.presentation.screens.SummaryScreen
import com.samapp.renttrack.presentation.screens.changeTenantDetailScreen
import com.samapp.renttrack.presentation.screens.tenantDetailScreen


@Composable
fun RootNavigationGraph(modifier: Modifier, navController: NavHostController) {

    val enterTransition: EnterTransition = fadeIn() + slideInHorizontally(initialOffsetX = { it })
    val exitTransition: ExitTransition = fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
    val popEnterTransition: EnterTransition = fadeIn() + slideInHorizontally(initialOffsetX = { -it })
    val popExitTransition: ExitTransition = fadeOut() + slideOutHorizontally(targetOffsetX = { it })


    NavHost(
        navController = navController,
        startDestination = Screen.BottomNavigation.route,
        modifier = modifier.background(MaterialTheme.colorScheme.background)
    ) {
        /** 🔹 Bottom Navigation Graph */
        navigation(startDestination = Screen.Home.route, route = Screen.BottomNavigation.route) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onFabClick = { navController.navigate(Screen.AddTenant.route) },
                    navController = navController
                )
            }
            composable(Screen.Summary.route) {
                SummaryScreen(
                    navController = navController,
                )
            }
        }

        /** 🔹 Tenant Management (Add, Details, Update) */
        composable(
            route= Screen.AddTenant.route,
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }
        )
        {
            AddTenantScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.TenantDetails.route + "/{tenantId}",
            arguments = listOf(navArgument("tenantId") { type = NavType.IntType }),
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }
        ) { backStackEntry ->
            val tenantId = backStackEntry.arguments?.getInt("tenantId") ?: return@composable
            tenantDetailScreen(
                tenantId = tenantId,
                onBack = { navController.popBackStack() },
                onUpdate = {tenantId->
                    navController.navigate(Screen.ChangeTenantDetails.createRoute(tenantId))
                }
            )
        }

        composable(
            route = Screen.ChangeTenantDetails.route,
            arguments = listOf(navArgument("tenantId") { type = NavType.IntType }),
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }
        ) { backStackEntry ->
            val tenantId = backStackEntry.arguments?.getInt("tenantId") ?: return@composable
            changeTenantDetailScreen(
                tenantId = tenantId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
