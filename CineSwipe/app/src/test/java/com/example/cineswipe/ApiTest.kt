package com.example.cineswipe

import kotlinx.coroutines.runBlocking
import org.junit.Test

class ApiTest {

    @Test
    fun testGetAllMoviesApi() = runBlocking {
        val movies = RetrofitClient.api.getAllMovies()
        println("Movies fetched: ${movies.size}")
        assert(movies.isNotEmpty())
    }
}
