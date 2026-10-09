package ru.artem_torpedo.thechronicle.di

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.getSystemService
import androidx.room.Room
import androidx.work.WorkManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import ru.artem_torpedo.thechronicle.data.local.AppDatabase
import ru.artem_torpedo.thechronicle.data.local.dao.NewsDao
import ru.artem_torpedo.thechronicle.data.remote.NewsApiResponse
import ru.artem_torpedo.thechronicle.data.repository.NewsRepositoryImpl
import ru.artem_torpedo.thechronicle.data.repository.SettingsRepositoryImpl
import ru.artem_torpedo.thechronicle.domain.iRepository.NewsRepository
import ru.artem_torpedo.thechronicle.domain.iRepository.SettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface Module {

    @Singleton
    @Binds
    fun getSettingsRepo(
        impl: SettingsRepositoryImpl,
    ): SettingsRepository

    @Singleton
    @Binds
    fun getNewsRepo(
        impl: NewsRepositoryImpl,
    ): NewsRepository

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        @Singleton
        @Provides
        fun provideApiService(): NewsApiResponse {
            val baseUrl = "https://newsapi.org/"
            val converter = json.asConverterFactory(
                "application/json".toMediaType()
            )
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converter)
                .build()
            return retrofit.create<NewsApiResponse>()
        }

        @Singleton
        @Provides
        fun getDbInstance(
            @ApplicationContext context: Context,
        ): AppDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = AppDatabase::class.java,
                name = "news.db"
            ).fallbackToDestructiveMigration(dropAllTables = true).build()
        }

        @Provides
        fun getNewsDao(
            db: AppDatabase,
        ): NewsDao {
            return db.dao()
        }

        @Provides
        fun getWorkManager(
            @ApplicationContext context: Context,
        ): WorkManager {
            return WorkManager.getInstance(context)
        }

        @Provides
        @Singleton
        fun getNotificationManager(
            @ApplicationContext context: Context,
        ): NotificationManager? {
            return context.getSystemService<NotificationManager>()
        }
    }
}