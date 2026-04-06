package com.example.cineswipe

data class Movie(
    val id: Int,
    val title: String,
    val genre: String,
    val posterUrl: String,
    val rating: Double
)

data class WatchlistItem(
    val id: Int = 0,
    val movieId: Int,
    val userId: String
)

