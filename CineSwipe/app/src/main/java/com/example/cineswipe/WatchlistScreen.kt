package com.example.cineswipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun WatchlistScreen(allMovies: List<Movie>) {
    var watchlistItems by remember { mutableStateOf<List<WatchlistItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            watchlistItems = RetrofitClient.api.getWatchlist(USER_ID)
        } catch (_: Exception) {}
        isLoading = false
    }

    val watchlistMovies = watchlistItems.mapNotNull { item ->
        allMovies.find { it.id == item.movieId }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            isLoading -> CircularProgressIndicator()
            watchlistMovies.isEmpty() -> Text("Your watchlist is empty.", fontSize = 18.sp)
            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(watchlistMovies) { movie ->
                    WatchlistCard(movie)
                }
            }
        }
    }
}

@Composable
fun WatchlistCard(movie: Movie) {
    Box(
        modifier = Modifier
            .aspectRatio(0.67f)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1a1a1a))
    ) {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(8.dp)
        ) {
            Text(text = movie.title, color = Color.White, fontSize = 13.sp)
            Text(text = "⭐ ${movie.rating}", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
        }
    }
}
