package com.example.cineswipe

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String,
    val genre: String,
    val posterUrl: String,
    val rating: Double,
    val description: String? = "No description available."
)

data class WatchlistItem(
    val id: Int = 0,
    @SerializedName("MovieId")
    val movieId: Int,
    @SerializedName("UserId")
    val userId: String
)
