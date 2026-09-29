package ru.artem_torpedo.thechronicle.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.artem_torpedo.thechronicle.data.mapper.getIntervalFromMinutes
import ru.artem_torpedo.thechronicle.domain.entity.Language
import ru.artem_torpedo.thechronicle.domain.entity.Settings
import ru.artem_torpedo.thechronicle.domain.iRepository.SettingsRepository
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context,
) : SettingsRepository {
    private val LANGUAGE_KEY = stringPreferencesKey("language")
    private val INTERVAL_KEY = intPreferencesKey("interval")
    private val NOTIFICATIONS_ENABLED_KEY = booleanPreferencesKey("notifications")
    private val WIFI_ONLY_KEY = booleanPreferencesKey("wifi")
    override fun getSettings(): Flow<Settings> {
        return context.dataStore.data
            .map { preferences ->
                val a = preferences[LANGUAGE_KEY] ?: Settings.DEFAULT_LANGUAGE.name
                val b =
                    preferences[INTERVAL_KEY]?.getIntervalFromMinutes() ?: Settings.DEFAULT_INTERVAL
                val c =
                    preferences[NOTIFICATIONS_ENABLED_KEY] ?: Settings.DEFAULT_NOTIFICATIONS_STATUS
                val d = preferences[WIFI_ONLY_KEY] ?: Settings.DEFAULT_WIFI_ONLY
                Settings(Language.valueOf(a), b, c, d)
            }
    }

    override suspend fun changeLanguage(language: Language) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language.name
        }
    }

    override suspend fun updateInterval(interval: Int) {
        context.dataStore.edit { preferences ->
            preferences[INTERVAL_KEY] = interval
        }
    }

    override suspend fun changeNotifications(status: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED_KEY] = status
        }
    }

    override suspend fun changeWifiUpdateStatus(status: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[WIFI_ONLY_KEY] = status
        }
    }
}