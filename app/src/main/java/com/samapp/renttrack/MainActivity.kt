package com.samapp.renttrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.samapp.renttrack.di.App
import com.samapp.renttrack.presentation.navigation.RootNavigationGraph
import com.samapp.renttrack.presentation.screens.HomeScreen
import com.samapp.renttrack.presentation.screens.MainScreen.MainScreen
//import com.samapp.renttrack.presentation.screens.MainScreen
import com.samapp.renttrack.ui.theme.RentTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RentTrackTheme {
                val navController = rememberNavController()
                MainScreen(
                    navController = navController
                )
            }
        }
    }
}