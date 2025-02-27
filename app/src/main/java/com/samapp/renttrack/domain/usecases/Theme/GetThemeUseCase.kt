package com.samapp.renttrack.domain.usecases.Theme

import com.samapp.renttrack.data.local.datastore.ThemeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(private val repository: ThemeRepository) {
    operator fun invoke(): Flow<Boolean> = repository.getTheme()
}
