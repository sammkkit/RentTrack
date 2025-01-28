package com.samapp.renttrack.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.presentation.components.TenantComponent
import java.time.LocalDate
import kotlin.random.Random

// Function to generate random names with random characters
fun generateRandomName(): String {
    val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    return (1..10) // 10 characters for the name
        .map { characters.random() }
        .joinToString("")
}

val mockTenants = listOf(
    Tenant(
        id = 1,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 2, 1),
        monthlyRent = 1500.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 2,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 2, 5),
        monthlyRent = 2000.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 3,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 2, 10),
        monthlyRent = 1800.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 4,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 2, 15),
        monthlyRent = 2200.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 5,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 2, 20),
        monthlyRent = 1700.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 6,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 2, 25),
        monthlyRent = 1600.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 7,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 3, 1),
        monthlyRent = 2100.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 8,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 3, 5),
        monthlyRent = 1900.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 9,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 3, 10),
        monthlyRent = 2300.0,
        avatarBackgroundColor = Random.nextInt()
    ),
    Tenant(
        id = 10,
        name = generateRandomName(),
        rentDueDate = LocalDate.of(2025, 3, 15),
        monthlyRent = 2400.0,
        avatarBackgroundColor = Random.nextInt()
    )
)


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn {
            itemsIndexed(mockTenants){ _, tenant ->
                TenantComponent(
                    modifier=Modifier,
                    tenant = tenant
                )
            }
        }
    }
}