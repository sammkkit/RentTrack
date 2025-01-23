package com.samapp.renttrack.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.samapp.renttrack.presentation.components.PhotoPickingComponent

@Composable
fun AddTenantScreen(
    modifier: Modifier = Modifier
) {
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PhotoPickingComponent(
            onPhotoPicked = { uri ->
            photoUri = uri
        })
    }
}

@Preview(showBackground = true)
@Composable
fun AddTenantScreenPreview() {
    AddTenantScreen()
}

