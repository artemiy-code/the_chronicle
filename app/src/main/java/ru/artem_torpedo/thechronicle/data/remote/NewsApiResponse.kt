package ru.artem_torpedo.thechronicle.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiResponse {

    @GET("v2/everything?apiKey=419ba04ba1e74c46b2bff2803814bfbd")
    suspend fun getArticles(
        @Query("q") topic: String,
    ): NewsDto
}