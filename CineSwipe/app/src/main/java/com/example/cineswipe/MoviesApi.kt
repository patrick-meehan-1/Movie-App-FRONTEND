package com.example.cineswipe

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MoviesApi {

    @GET("movies")
    suspend fun getAllMovies(): List<Movie>

    @POST("watchlist")
    suspend fun addToWatchlist(@Body item: WatchlistItem)

    @GET("watchlist/{userId}")
    suspend fun getWatchlist(@Path("userId") userId: String): List<WatchlistItem>
}
