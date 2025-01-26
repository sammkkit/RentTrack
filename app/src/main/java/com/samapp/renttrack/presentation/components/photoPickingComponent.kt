package com.samapp.renttrack.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.samapp.renttrack.R

@Composable
fun PhotoPickingComponent(
    onPhotoPicked: (Uri?) -> Unit
){
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    val getImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            photoUri = uri
            onPhotoPicked(uri)
        }
    )
    Box(
        modifier = Modifier
            .size(100.dp)

        ,
        contentAlignment = Alignment.Center
    ) {
        if (photoUri != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photoUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "Tenant Photo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable {
                        getImage.launch("image/*")
                    }
                ,
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.user),
                contentDescription = "Default avatar",
                tint = Color.Gray,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        getImage.launch("image/*")
                    }
            )
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Add photo",
                tint = Color.Black,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(5.dp)
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PhotoPickingComponentPreview(){
    PhotoPickingComponent(onPhotoPicked = {})
}