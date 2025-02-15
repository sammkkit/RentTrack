package com.samapp.renttrack.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AddTenant : Screen("add_tenant")
    data object TenantDetails : Screen("tenant_details/{tenantId}") {
        fun createRoute(tenantId: Int) = "tenant_details/$tenantId"
    }
    data object Summary : Screen("summary")
    data object BottomNavigation : Screen("bottom_navigation")
    data object AddTenantNavGraph : Screen("add_tenant_nav_graph")
}
