package com.samapp.renttrack.presentation.navigation

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.samapp.renttrack.R

sealed class BottomNavigationItem(val label:String,val route: String, val icon: Int){
    data object Tenants : BottomNavigationItem("Tenants",Screen.Home.route, R.drawable.ic_home)
    data object Summary : BottomNavigationItem("Summary",Screen.Summary.route, R.drawable.ic_summary)
    data object test : BottomNavigationItem("Summary",Screen.AddTenant.route, R.drawable.phone)
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier
) {
    val items = listOf(
        BottomNavigationItem.Tenants,
        BottomNavigationItem.Summary,
//        BottomNavigationItem.test
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var selectedItem = items.indexOfFirst { it.route == currentRoute }.takeIf { it != -1 } ?: 0


    NavigationBar(
//        modifier = modifier.windowInsetsPadding(WindowInsets.navigationBars) ,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.route,
                            tint = if(selectedItem== index)MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .size(if(index != 1)35.dp else 25.dp)
                                .offset(y = if (selectedItem == index) (-3).dp else 0.dp)
                        )
                },
                label = if (selectedItem == index) {
                    {
                        Text(
                            text = item.label,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else null,
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route){
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.surface // Theme-based indicator color
                )
            )
        }
    }
}

//@Preview
//@Composable
//fun BottomNavigationBarPreview(){
//    BottomNavigationBar(navController = NavHostController(LocalContext.current))
//
//}