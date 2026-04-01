package com.example.cineswipe

import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("movies/random")
    suspend fun getRandomMovie(): Movie

    @GET("movies/search")
    suspend fun searchMovies(
        @Query("query") query: String
    ): List<Movie>
}
