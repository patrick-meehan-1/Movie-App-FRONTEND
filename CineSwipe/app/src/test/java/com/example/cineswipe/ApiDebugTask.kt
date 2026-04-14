package com.example.cineswipe

import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.net.URL

class ApiDebugTask {
    @Test
    fun printJsonResponse() {
        try {
            val json = URL("https://movieapp-backend-production-9c1c.up.railway.app/api/movies").readText()
            println("API Response: ${json.take(500)}...")
        } catch (e: Exception) {
            println("Error fetching: ${e.message}")
        }
    }
}
