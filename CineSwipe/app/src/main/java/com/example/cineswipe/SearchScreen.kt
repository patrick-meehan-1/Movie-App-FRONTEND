package com.example.cineswipe

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import kotlinx.coroutines.launch
import kotlinx.coroutines.GlobalScope
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    SearchScreen(onMovieClick = {})
}


@Composable
fun SearchScreen(onMovieClick: (Movie) -> Unit) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search movies") },
            trailingIcon = {
                IconButton(onClick = {
                    isSearching = true
                    searchMovies(query) { movies ->
                        results = movies
                        isSearching = false
                    }
                }) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isSearching) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(results) { movie ->
                    SearchResultItem(movie = movie, onClick = { onMovieClick(movie) })
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(movie: Movie, onClick: () -> Unit) {
    Text(
        text = movie.title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onClick() }
    )
}

fun searchMovies(query: String, onResult: (List<Movie>) -> Unit) {
    kotlinx.coroutines.GlobalScope.launch {
        try {
            val movies = RetrofitClient.api.searchMovies(query)
            onResult(movies)
        } catch (_: Exception) {
            onResult(emptyList())
        }
    }
}
