package ru.artem_torpedo.thechronicle.domain.useCases.settings

import kotlinx.coroutines.flow.Flow
import ru.artem_torpedo.thechronicle.domain.entity.Settings
import ru.artem_torpedo.thechronicle.domain.iRepository.SettingsRepository
import javax.inject.Inject

class GetAllSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    operator fun invoke(): Flow<Settings> {
        return repository.getSettings()
    }
}