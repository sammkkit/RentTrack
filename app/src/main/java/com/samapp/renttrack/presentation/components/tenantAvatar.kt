package com.samapp.renttrack.presentation.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.samapp.renttrack.data.local.model.Tenant
import java.io.File

const val TAG = "TenantAvatar"
@Composable
fun TenantAvatar(tenant: Tenant) {
    val filepath  = tenant.filePath
    Log.d(TAG, "Tenant Name: ${tenant.name}") // Log tenant name
    Log.d(TAG, "URI String BEFORE parsing: $filepath")
    if (filepath != null) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(File(filepath))
                    .build()
            ),
            contentDescription = "Image from Internal Storage",
            modifier = Modifier.size(64.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        Log.d(TAG, "TenantAvatar does not have photo uri: ${tenant.avatarBackgroundColor}")

        val backgroundColor = Color(tenant.avatarBackgroundColor)
        val textColor = Color.White

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .background(backgroundColor, CircleShape)
        ) {
            Text(
                text = tenant.name.firstOrNull()?.toString()?.uppercase() ?: "U",
                color = MaterialTheme.colorScheme.surface,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
