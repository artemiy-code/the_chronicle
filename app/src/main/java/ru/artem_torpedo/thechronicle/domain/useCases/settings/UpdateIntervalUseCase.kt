package ru.artem_torpedo.thechronicle.domain.useCases.settings

import ru.artem_torpedo.thechronicle.domain.entity.Interval
import ru.artem_torpedo.thechronicle.domain.iRepository.SettingsRepository
import javax.inject.Inject

class UpdateIntervalUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(interval: Interval) {
        repository.updateInterval(interval.minutes)
    }
}