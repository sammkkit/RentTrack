package com.samapp.renttrack.data.local.datastore

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getTheme(): Flow<Boolean>
    suspend fun saveTheme(isDark: Boolean)
}
