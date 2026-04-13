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
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import coil.compose.AsyncImage

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF141414))
            .padding(16.dp)
    ) {


        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search movies", color = Color(0xFFB3B3B3)) },
            trailingIcon = {
                IconButton(onClick = {
                    isSearching = true
                    searchMovies(query) { movies ->
                        results = movies
                        isSearching = false
                    }
                }) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color(0xFFE50914)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE50914),
                unfocusedBorderColor = Color(0xFF333333),
                cursorColor = Color(0xFFE50914),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isSearching) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFE50914))
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(results) { movie ->
                    SearchResultItem(movie = movie, onClick = { onMovieClick(movie) })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchResultItemPreview() {
    SearchResultItem(
        movie = Movie(
            id = 1,
            title = "Preview Movie",
            genre = "Action",
            rating = 8.5,
            posterUrl = "https://via.placeholder.com/300"
        ),
        onClick = {}
    )
}

@Composable
fun SearchResultItem(movie: Movie, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1A1A1A))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = movie.title,
                color = Color.White,
                fontSize = 18.sp
            )
            Text(
                text = movie.genre,
                color = Color(0xFFB3B3B3),
                fontSize = 14.sp
            )
            Text(
                text = "⭐ ${movie.rating}",
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
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
