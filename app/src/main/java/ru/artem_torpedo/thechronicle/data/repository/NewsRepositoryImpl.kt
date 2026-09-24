package ru.artem_torpedo.thechronicle.data.repository

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import ru.artem_torpedo.thechronicle.data.local.dao.NewsDao
import ru.artem_torpedo.thechronicle.data.local.entity.ArticleDbModel
import ru.artem_torpedo.thechronicle.data.local.entity.SubscriptionDbModel
import ru.artem_torpedo.thechronicle.data.mapper.convertToEntities
import ru.artem_torpedo.thechronicle.data.mapper.convertToStringList
import ru.artem_torpedo.thechronicle.data.mapper.toDbModels
import ru.artem_torpedo.thechronicle.data.remote.NewsApiResponse
import ru.artem_torpedo.thechronicle.domain.entity.Article
import ru.artem_torpedo.thechronicle.domain.iRepository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    val newsDao: NewsDao,
    val apiService: NewsApiResponse,
) : NewsRepository {
    override suspend fun addNewSubscription(topic: String) {
        val subscription = SubscriptionDbModel(topic)
        newsDao.addNewSubscription(subscription)
    }

    override suspend fun updateArticlesForNewTopic(topic: String) {
        loadArticles(topic).takeIf {
            it.isNotEmpty()
        }?.also {
            newsDao.addArticles(it)
        }
    }

    private suspend fun loadArticles(topic: String): List<ArticleDbModel> {
        return try {
            val articles =  apiService.getArticles(topic = topic)
            articles.toDbModels(topic)
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Log.e("NewsRepositoryImpl", e.stackTraceToString())
            emptyList()
        }
    }

    override suspend fun deleteSubscription(topic: String) {
        val subscription = SubscriptionDbModel(topic)
        newsDao.deleteSubscription(subscription)
    }

    override fun getAllSubscriptions(): Flow<List<String>> {
        return newsDao.getAllSubscriptions().map { lst ->
            lst.convertToStringList()
        }
    }

    override suspend fun updateAllArticles() {
        // 1.
        val subscriptions = getAllSubscriptions().first()
        coroutineScope {
            subscriptions.forEach {
                launch {
                    updateArticlesForNewTopic(it)
                }
            }
        }

//        2.
//        getAllSubscriptions().collect { subscriptions ->
//            supervisorScope {
//                subscriptions.forEach {
//                    launch {
//                        updateArticlesForNewTopic(it)
//                    }
//                }
//            }
//        }
    }

    override fun getAllArticles(): Flow<List<Article>> {
        return newsDao.getAllArticles().map { lst ->
            lst.convertToEntities()
        }
    }

    override fun getArticlesForTopics(topics: List<String>): Flow<List<Article>> {
        return newsDao.getArticlesForTopics(topics).map {
            it.convertToEntities()
        }
    }

    override suspend fun deleteArticlesForTopics(topics: List<String>) {
        newsDao.deleteArticlesForTopics(topics)
    }
}