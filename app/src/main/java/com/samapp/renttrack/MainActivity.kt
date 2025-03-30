package com.samapp.renttrack

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
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
                NotificationPermissionHandler(
                    onPermissionGranted = {
                        // Call your notification logic here
                        Log.d("NotificationTest", "Permission granted, you can show notifications now.")
                    }
                )
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
@Composable
fun NotificationPermissionHandler(
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
                onPermissionGranted()
            } else {
                Toast.makeText(context, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted
                    onPermissionGranted()
                }
                else -> {
                    // Request permission
                    permissionLauncher.launch( android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            // No need for permission on Android 12 and below
            onPermissionGranted()
        }
    }
}

