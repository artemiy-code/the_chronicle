package ru.artem_torpedo.thechronicle.di

import android.content.Context
import androidx.room.Room
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
import ru.artem_torpedo.thechronicle.domain.iRepository.NewsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface Module {

    @Singleton
    @Binds
    fun getRepo(
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
                "applicatiom/json".toMediaType()
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
    }
}