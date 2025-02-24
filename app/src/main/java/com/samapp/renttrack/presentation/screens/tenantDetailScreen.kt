package com.samapp.renttrack.presentation.screens


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.samapp.renttrack.R
import com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.presentation.viewmodels.TenantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun tenantDetailScreen(
    tenantId: Int,
    onBack: () -> Unit,
    onUpdate: (Int)->Unit
) {
    val tenantViewModel: TenantViewModel = hiltViewModel()
    val colors = MaterialTheme.colorScheme
    LaunchedEffect (Unit){
        tenantViewModel.getTenantById(tenantId)
    }
    val tenantState by tenantViewModel.tenantState.collectAsState()
    val tenant = (tenantState as? Result.Success<Tenant>)?.data
    val context = LocalContext.current

    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${tenant?.name ?: "Unknown"}", fontSize = 24.sp, color = colors.onSurface) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = colors.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        menuExpanded = !menuExpanded

                    },
                        modifier = Modifier.background(colors.surface, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = colors.onSurfaceVariant
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier.background(colors.surfaceVariant, shape = RoundedCornerShape(8.dp))
                    ) {
                        DropdownMenuItem(
                            text = { Text("Update Details",color = colors.onSurface) },
                            onClick = {
                                menuExpanded = false
                                onUpdate(tenantId)
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = colors.onSurface,
                                leadingIconColor = colors.primary
                            )
                        )
                        DropdownMenuItem(
                            text = { Text("Delete Tenant",color = colors.error) },
                            onClick = {
                                menuExpanded = false
                                //TODO - show a dialog box to confirm
                                tenantViewModel.deleteTenant(tenant!!)
                                Toast.makeText(context,"Tenant Deleted", Toast.LENGTH_SHORT).show()
                                onBack()
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = colors.error,
                                leadingIconColor = colors.error
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.surface)
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = colors.primary,
                modifier = Modifier
                    .clickable {

                    },
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        ,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.transactionhistory),
                        contentDescription = "transaction history",
                        tint = colors.onPrimary,
                        modifier = Modifier
                            .size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Transactions",
                        fontSize = 22.sp
                    )
                }
            }
        }
    ) {  padding ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when (val state = tenantState) {
                is Result.Loading -> {
                    CircularProgressIndicator()
                }

                is Result.Error -> {
                    LaunchedEffect(Unit) {
                        Toast.makeText(context, "Tenant not found", Toast.LENGTH_SHORT).show()
                        onBack()
                    }
                }

                is Result.Success -> {
                    val tenant = state.data as Tenant

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Name: ${tenant.name}", fontSize = 20.sp)
                        Text(text = "Rent: ${tenant.monthlyRent}", fontSize = 20.sp)
                        Text(text = "Contact: ${tenant.contact}", fontSize = 20.sp)
                    }
                }
            }
        }

    }

}
//@Preview
//@Composable
//fun tenantDetailScreenPreview() {
//    tenantDetailScreen(tenantId = 1, onBack = {}, tenantViewModel = hiltViewModel())
//}