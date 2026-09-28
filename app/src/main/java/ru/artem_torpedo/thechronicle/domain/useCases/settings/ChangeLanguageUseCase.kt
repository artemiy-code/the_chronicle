package ru.artem_torpedo.thechronicle.domain.useCases.settings

import ru.artem_torpedo.thechronicle.domain.entity.Language
import ru.artem_torpedo.thechronicle.domain.iRepository.SettingsRepository
import javax.inject.Inject

class ChangeLanguageUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(language: Language) {
        repository.changeLanguage(language)
    }
}