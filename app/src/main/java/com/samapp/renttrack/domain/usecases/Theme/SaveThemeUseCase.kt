package com.samapp.renttrack.domain.usecases.Theme

import com.samapp.renttrack.data.local.datastore.ThemeRepository
import javax.inject.Inject

class SaveThemeUseCase @Inject constructor(private val repository: ThemeRepository) {
    suspend operator fun invoke(isDark: Boolean) = repository.saveTheme(isDark)
}
