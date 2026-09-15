package ru.artem_torpedo.thechronicle.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.artem_torpedo.thechronicle.data.local.dao.NewsDao
import ru.artem_torpedo.thechronicle.data.local.entity.ArticleDbModel
import ru.artem_torpedo.thechronicle.data.local.entity.SubscriptionDbModel
import javax.inject.Singleton

@Database(
    entities = [
        ArticleDbModel::class,
        SubscriptionDbModel::class
    ],
    version = 1,
    exportSchema = false,
)
@Singleton
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): NewsDao
}