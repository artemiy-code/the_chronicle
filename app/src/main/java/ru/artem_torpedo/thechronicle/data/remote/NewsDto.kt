package ru.artem_torpedo.thechronicle.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsDto(
    @SerialName("articles")
    val articles: List<ArticleDto> = listOf(),
)