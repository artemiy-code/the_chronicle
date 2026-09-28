package ru.artem_torpedo.thechronicle.domain.entity

data class Settings(
    val language: Language,
    val updateInterval: Interval,
    val notificationsEnabled: Boolean,
    val wifiOnly: Boolean,
)

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