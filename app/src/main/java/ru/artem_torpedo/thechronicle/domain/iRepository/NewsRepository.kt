package ru.artem_torpedo.thechronicle.domain.iRepository

import kotlinx.coroutines.flow.Flow
import ru.artem_torpedo.thechronicle.domain.entity.Article
import ru.artem_torpedo.thechronicle.domain.entity.RefreshParameters

interface NewsRepository {

    suspend fun addNewSubscription(topic: String)

    suspend fun updateArticlesForNewTopic(topic: String)

    suspend fun deleteSubscription(topic: String)

    fun getAllSubscriptions(): Flow<List<String>>

    suspend fun updateAllArticles()

    fun getAllArticles(): Flow<List<Article>>

    fun getArticlesForTopics(topics: List<String>): Flow<List<Article>>

    suspend fun deleteArticlesForTopics(topics: List<String>)

    suspend fun startBackgroundRefresh(refreshParameters: RefreshParameters)
}