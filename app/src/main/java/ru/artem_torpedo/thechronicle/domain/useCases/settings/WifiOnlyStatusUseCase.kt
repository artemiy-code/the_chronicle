package ru.artem_torpedo.thechronicle.domain.useCases.settings

import kotlinx.coroutines.flow.Flow
import ru.artem_torpedo.thechronicle.domain.entity.Language
import ru.artem_torpedo.thechronicle.domain.entity.Settings
import ru.artem_torpedo.thechronicle.domain.iRepository.SettingsRepository
import javax.inject.Inject

class WifiOnlyStatusUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(status: Boolean) {
        repository.changeWifiUpdateStatus(status)
    }
}