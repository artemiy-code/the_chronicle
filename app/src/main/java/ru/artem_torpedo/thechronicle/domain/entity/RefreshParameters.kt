package ru.artem_torpedo.thechronicle.domain.entity

data class RefreshParameters(
    val language: Language,
    val updateInterval: Interval,
    val wifiOnly: Boolean,
)