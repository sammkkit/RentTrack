package com.samapp.renttrack.presentation.components

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.samapp.renttrack.R
import com.samapp.renttrack.data.local.TypeConverter.ColorTypeConverter
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.util.getRandomColor

const val TAG = "TenantAvatar"
@Composable
fun TenantAvatar(tenant: Tenant) {
    val uriString  = tenant.photoUri
    Log.d(TAG, "Tenant Name: ${tenant.name}") // Log tenant name
    Log.d(TAG, "URI String BEFORE parsing: $uriString")  // Log URI string
//    val uri: Uri? = try {
//        uriString?.let {
//            val parsedUri = Uri.parse(it)
//            Log.d(TAG, "Parsed URI: $parsedUri")
//            parsedUri
//        }
//    } catch (e: Exception) {
//        Log.e(TAG, "Error parsing URI: ${e.message}")
//        null
//    }
//    if (uri != null) {
//        Log.d(TAG, "TenantAvatar have photo uri: ${tenant.photoUri}")
//
//        val context = LocalContext.current
//
//        val request = ImageRequest.Builder(context)
//            .data(uri)
//            .crossfade(true) // Optional: Add crossfade animation
//            .build()
//
//        val painter = rememberAsyncImagePainter(request)
//
//        Image(
//            painter = painter,
//            contentDescription = "Image from URI",
//            modifier = Modifier
//                .size(64.dp) // Added a size modifier to make it visible
//                .clip(CircleShape)
//        )
//    } else {
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
//    }
}
