@file:OptIn(FlowPreview::class)

package ru.artem_torpedo.thechronicle.domain.useCases.service

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.artem_torpedo.thechronicle.data.mapper.toRefreshParametrs
import ru.artem_torpedo.thechronicle.domain.iRepository.NewsRepository
import ru.artem_torpedo.thechronicle.domain.iRepository.SettingsRepository
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class StartServiceUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val newsRepository: NewsRepository,
) {
    suspend operator fun invoke() {
        settingsRepository.getSettings()
            .map { it.toRefreshParametrs() }
            .debounce(100.milliseconds)
            .distinctUntilChanged()
            .onEach { newsRepository.startBackgroundRefresh(it) }
            .collect()
    }
}