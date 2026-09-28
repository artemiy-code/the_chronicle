package ru.artem_torpedo.thechronicle.domain.iRepository

import kotlinx.coroutines.flow.Flow
import ru.artem_torpedo.thechronicle.domain.entity.Interval
import ru.artem_torpedo.thechronicle.domain.entity.Language
import ru.artem_torpedo.thechronicle.domain.entity.Settings

interface SettingsRepository {
    fun getSettings(): Flow<Settings>

    suspend fun changeLanguage(language: Language)

    suspend fun updateInterval(interval: Int)

    suspend fun changeNotifications(status: Boolean)

    suspend fun changeWifiUpdateStatus(status: Boolean)
}