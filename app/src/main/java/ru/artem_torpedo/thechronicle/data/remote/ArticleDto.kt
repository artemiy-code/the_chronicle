package ru.artem_torpedo.thechronicle.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    @SerialName("title")
    val title: String = "",
    @SerialName("description")
    val description: String = "",
    @SerialName("publishedAt")
    val publishedAt: String = "",
    @SerialName("source")
    val source: SourceDto = SourceDto(),
    @SerialName("url")
    val url: String = "",
    @SerialName("urlToImage")
    val urlToImage: String = "",
)