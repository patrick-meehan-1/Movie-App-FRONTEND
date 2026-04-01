package com.example.cineswipe

import kotlinx.coroutines.runBlocking
import org.junit.Test

class ApiTest {

    @Test
    fun testRandomMovieApi() = runBlocking {
        val movie = RetrofitClient.api.getRandomMovie()
        println("Random movie: ${movie.title}")
        assert(movie.title.isNotEmpty())
    }
}
