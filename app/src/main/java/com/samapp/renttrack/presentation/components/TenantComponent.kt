package com.samapp.renttrack.presentation.components

import android.graphics.Paint.Align
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samapp.renttrack.data.local.model.Tenant

@Composable
fun TenantComponent(
    modifier: Modifier = Modifier,
    tenant: Tenant = Tenant(
        name = "Sam",
    )
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TenantAvatar(photoUri = tenant.photoUri, tenant = tenant)
                Column {
                    Text(
                        text = tenant.name,
                        modifier = Modifier.padding(start = 8.dp),
                        color = Color.Black,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Due Date : ${tenant.rentDueDate}",
                        modifier = Modifier.padding(start = 8.dp),
                        color = Color.Gray
                    )
                }
            }
            RentShowingClip(
                rent = tenant.monthlyRent.toInt()
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun TenantComponentPreview(){
    TenantComponent()
}