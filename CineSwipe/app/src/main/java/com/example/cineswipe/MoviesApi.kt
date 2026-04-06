package com.example.cineswipe

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MoviesApi {

    @GET("movies")
    suspend fun getAllMovies(): List<Movie>

    @POST("watchlist")
    suspend fun addToWatchlist(@Body item: WatchlistItem)
}
