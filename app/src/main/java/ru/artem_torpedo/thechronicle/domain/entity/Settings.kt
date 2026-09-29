package ru.artem_torpedo.thechronicle.domain.entity

data class Settings(
    val language: Language,
    val updateInterval: Interval,
    val notificationsEnabled: Boolean,
    val wifiOnly: Boolean,
) {

    companion object {
        val DEFAULT_LANGUAGE = Language.ENGLISH
        val DEFAULT_INTERVAL = Interval.DAY
        val DEFAULT_NOTIFICATIONS_STATUS = false
        val DEFAULT_WIFI_ONLY = false
    }
}

enum class Language {
    RUSSIAN,
    ENGLISH,
    GERMAN,
    FRENCH,
}

enum class Interval(val minutes: Int) {
    MIN_15(15),
    MIN_30(30),
    HOUR(60),
    HOUR_2(120),
    HOUR_4(240),
    HOUR_8(480),
    HOUR_12(720),
    DAY(1440),
}