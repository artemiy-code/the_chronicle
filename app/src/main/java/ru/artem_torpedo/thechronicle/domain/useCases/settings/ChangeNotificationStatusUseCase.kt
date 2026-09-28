package ru.artem_torpedo.thechronicle.domain.useCases.settings

import ru.artem_torpedo.thechronicle.domain.iRepository.SettingsRepository
import javax.inject.Inject

class ChangeNotificationStatusUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(status: Boolean) {
        repository.changeNotifications(status)
    }
}