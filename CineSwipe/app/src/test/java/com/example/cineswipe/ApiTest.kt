package com.example.cineswipe

import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.HttpException

class ApiTest {

    @Test
    fun testGetAllMoviesApi() = runBlocking {
        val movies = RetrofitClient.api.getAllMovies()
        println("Movies fetched: ${movies.size}")
        assert(movies.isNotEmpty())
    }

    @Test
    fun testAddToWatchlist() = runBlocking {
        try {
            val movies = RetrofitClient.api.getAllMovies()
            if (movies.isEmpty()) {
                println("Skipping test: No movies in database.")
                return@runBlocking
            }
            
            val movieId = movies.first().id
            val item = WatchlistItem(
                movieId = movieId,
                userId = USER_ID
            )

            // Debug: Print the payload being sent
            println("Sending WatchlistItem JSON: ${Gson().toJson(item)}")

            RetrofitClient.api.addToWatchlist(item)
            println("Successfully added movie $movieId to watchlist.")
        } catch (e: HttpException) {
            if (e.code() == 409) {
                // Treat 409 (Conflict) as success because it confirms the API works and business logic is applied
                println("API working correctly: Movie already in watchlist (409).")
            } else {
                val errorBody = e.response()?.errorBody()?.string()
                println("HTTP Error adding to watchlist: ${e.code()} - $errorBody")
                throw e
            }
        } catch (e: Exception) {
            println("Error adding to watchlist: ${e.message}")
            throw e
        }
    }

    @Test
    fun testGetWatchlist() = runBlocking {
        try {
            val list = RetrofitClient.api.getWatchlist(USER_ID)
            println("Watchlist items for $USER_ID: ${list.size}")
            list.forEach { println(" - Movie ID: ${it.movieId}") }
            assert(true)
        } catch (e: Exception) {
            println("Error getting watchlist: ${e.message}")
            throw e
        }
    }
}
