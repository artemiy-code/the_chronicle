package ru.artem_torpedo.thechronicle.data.mapper

import ru.artem_torpedo.thechronicle.data.local.entity.ArticleDbModel
import ru.artem_torpedo.thechronicle.data.local.entity.SubscriptionDbModel
import ru.artem_torpedo.thechronicle.data.remote.NewsDto
import ru.artem_torpedo.thechronicle.domain.entity.Article
import ru.artem_torpedo.thechronicle.domain.entity.Interval
import java.text.SimpleDateFormat
import java.util.Locale

fun NewsDto.toDbModels(topic: String): List<ArticleDbModel> {
    return articles.map {
        ArticleDbModel(
            title = it.title,
            description = it.description,
            imageUrl = it.urlToImage,
            sourceName = it.source.name,
            publishedAt = it.publishedAt.convertToLong(),
            articleUrl = it.url,
            topic = topic
        )
    }
}

private fun String.convertToLong(): Long {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    return formatter.parse(this)?.time ?: System.currentTimeMillis()
}

fun List<SubscriptionDbModel>.convertToStringList(): List<String> {
    return this.map {
        it.topic
    }
}

fun List<ArticleDbModel>.convertToEntities(): List<Article> {
    return this.map {
        Article(
            title = it.title,
            description = it.description,
            imageUrl = it.imageUrl,
            sourceName = it.sourceName,
            publishedAt = it.publishedAt,
            articleUrl = it.articleUrl
        )
    }.distinct()
}

fun Int.getIntervalFromMinutes(): Interval {
    return Interval.entries.find {
        it.minutes == this
    } ?: Interval.DAY
}