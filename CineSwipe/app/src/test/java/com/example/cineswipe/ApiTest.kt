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

    @Test
    fun testSearchMoviesApi() = runBlocking {
        val movies = RetrofitClient.api.searchMovies("Inception")
        println("Movies found for 'Inception': ${movies.size}")
        // We don't assert isNotEmpty because "Inception" might not be in the DB, 
        // but we want to see if it throws an exception.
    }

    @Test
    fun testAddToWatchlist() = runBlocking {
        val item = WatchlistItem(
            movieId = 1,
            userId = USER_ID
        )

        RetrofitClient.api.addToWatchlist(item)

        // If no exception is thrown, passes
        assert(true)
    }

    @Test
    fun testGetWatchlist() = runBlocking {
        val list = RetrofitClient.api.getWatchlist(USER_ID)
        println("Watchlist items: ${list.size}")


        assert(list.isNotEmpty())
    }
}
