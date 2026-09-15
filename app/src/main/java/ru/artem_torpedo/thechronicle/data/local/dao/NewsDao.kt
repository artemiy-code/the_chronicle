package ru.artem_torpedo.thechronicle.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.artem_torpedo.thechronicle.data.local.entity.ArticleDbModel
import ru.artem_torpedo.thechronicle.data.local.entity.SubscriptionDbModel

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNewSubscription(sub: SubscriptionDbModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addArticles(articles : List<ArticleDbModel>)

    @Transaction
    @Delete
    suspend fun deleteSubscription(sub: SubscriptionDbModel)

    @Query("DELETE FROM articles WHERE topic IN (:topics)")
    suspend fun deleteArticlesForTopics(topics: List<String>)

    @Query("SELECT * FROM subscriptions")
    fun getAllSubscriptions(): Flow<List<SubscriptionDbModel>>

    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun getAllArticles(): Flow<List<ArticleDbModel>>

    @Query("SELECT * FROM articles WHERE topic IN (:topics) ORDER BY publishedAt DESC")
    fun getArticlesForTopics(topics: List<String>): Flow<List<ArticleDbModel>>
}