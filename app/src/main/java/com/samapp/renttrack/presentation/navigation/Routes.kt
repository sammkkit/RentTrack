package com.samapp.renttrack.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AddTenant : Screen("add_tenant")

    data object ChangeTenantDetails : Screen("change_tenant_details/{tenantId}") {
        fun createRoute(tenantId: Int) = "change_tenant_details/$tenantId"
    }

    data object Summary : Screen("summary")

    data object BottomNavigation : Screen("bottom_navigation")

    data object TenantDetails : Screen("tenant_details/{tenantId}"){
        fun createRoute(tenantId: Int) = "tenant_details/$tenantId"
    }

    data object TransactionHistory : Screen("transaction_history/{tenantId}"){
        fun createRoute(tenantId: Int) = "transaction_history/$tenantId"
    }

    data object Invoice : Screen("invoice/{tenantId}"){
        fun createRoute(tenantId: Int) = "invoice/$tenantId"
    }
}
