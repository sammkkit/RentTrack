package com.samapp.renttrack.data.local.datastore

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class ThemeRepositoryImpl @Inject constructor(
    private val dataSource: ThemePreferencesDataSource
) : ThemeRepository {
    override fun getTheme(): Flow<Boolean> = dataSource.isDarkTheme
    override suspend fun saveTheme(isDark: Boolean) = dataSource.saveTheme(isDark)
}
