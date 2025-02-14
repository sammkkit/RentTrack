package com.samapp.renttrack.presentation.screens.MainScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.samapp.renttrack.presentation.navigation.BottomNavigationBar
import com.samapp.renttrack.presentation.navigation.RootNavigationGraph

@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
            bottomBar = {
                BottomNavigationBar(modifier = Modifier, navController = navController)
            }
    ) {padding->
        RootNavigationGraph(modifier = Modifier.padding(padding), navController = navController)
    }
}