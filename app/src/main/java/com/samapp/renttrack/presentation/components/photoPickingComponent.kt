package com.samapp.renttrack.presentation.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.samapp.renttrack.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.UUID

@Composable
fun PhotoPickingComponent(
    modifier: Modifier = Modifier,
    onPhotoPicked: (String?) -> Unit
){
    var filepath by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val getImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            val newFilePath = uri?.let { copyImageToInternalStorage(context, it) }
            filepath = newFilePath
            onPhotoPicked(filepath)
        }
    )
    Box(
        modifier = modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                getImage.launch("image/*")
            }
        ,
        contentAlignment = Alignment.Center
    ) {
        if (filepath != null) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(File(filepath))
                        .build()
                ),
                contentDescription = "Image from Internal Storage",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.user),
                contentDescription = "Default avatar",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
            )
//            Icon(
//                imageVector = Icons.Default.AddCircle,
//                contentDescription = "Add photo",
//                tint = MaterialTheme.colorScheme.primary,
//                modifier = Modifier
//                    .padding(6.dp)
//                    .size(24.dp)
//                    .align(Alignment.BottomCenter)
//                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
//            )
        }
    }
}

fun copyImageToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        inputStream?.use { input ->
            val bitmap: Bitmap? = BitmapFactory.decodeStream(input)
            bitmap?.let {
                val imagesDir = File(context.filesDir, "images") // App's internal storage/images directory
                if (!imagesDir.exists()) {
                    imagesDir.mkdirs()
                }

                val fileName = "image_${UUID.randomUUID()}.jpg" // Unique file name
                val imageFile = File(imagesDir, fileName)

                FileOutputStream(imageFile).use { outputStream ->
                    it.compress(Bitmap.CompressFormat.JPEG, 90, outputStream) // Compress and save
                }

                imageFile.absolutePath // Return the absolute path to the saved file
            } ?: run {
                null // Bitmap decoding failed
            }
        } ?: run {
            null // InputStream is null
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null // Error during file operations
    } catch (e: SecurityException) {
        e.printStackTrace()
        null  //Permissions issue
    }
}
@Preview(showBackground = true)
@Composable
fun PhotoPickingComponentPreview(){
    PhotoPickingComponent(onPhotoPicked = {})
}