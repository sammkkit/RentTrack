package com.samapp.renttrack

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.samapp.renttrack.di.App
import com.samapp.renttrack.presentation.screens.HomeScreen
//import com.samapp.renttrack.presentation.screens.MainScreen.MainScreen
import com.samapp.renttrack.presentation.screens.MainScreen.RootNavigationGraph
import com.samapp.renttrack.presentation.viewmodels.ThemeViewModel
//import com.samapp.renttrack.presentation.screens.MainScreen
import com.samapp.renttrack.ui.theme.RentTrackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            val colors = MaterialTheme.colorScheme
            LaunchedEffect(colors) {
                Log.d("ThemeDebug", "Primary: ${colors.primary}, Secondary: ${colors.secondary}")
            }

            RentTrackTheme(darkTheme = isDarkTheme,dynamicColor = false) {
                RootNavigationGraph(
                    modifier = Modifier.fillMaxSize(),
                    navController = rememberNavController(),
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = { themeViewModel.toggleTheme() }
                )
            }
        }

    }
}