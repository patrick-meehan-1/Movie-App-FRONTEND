package com.example.cineswipe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview


@Preview(showBackground = true)
@Composable
fun WatchlistScreenPreview() {
    val sampleMovies = listOf(
        Movie(1, "Movie One", "Drama","https://via.placeholder.com/300", 7.8, "Description one" ),
        Movie(2, "Movie Two", "Action", "https://via.placeholder.com/300",8.2, "Description two")
    )

    WatchlistScreen(allMovies = sampleMovies, onMovieClick = {})
}




@Composable
fun WatchlistScreen(allMovies: List<Movie>, onMovieClick: (Movie) -> Unit) {
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF141414)),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> CircularProgressIndicator(color = Color(0xFFE50914))
            watchlistMovies.isEmpty() -> Text(
                "Your watchlist is empty.",
                fontSize = 18.sp,
                color = Color.White
            )

            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(watchlistMovies) { movie ->
                    WatchlistCard(movie = movie, onClick = { onMovieClick(movie) })
                }
            }
        }
    }
}


@Composable
fun WatchlistCard(movie: Movie, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(0.67f)
            .clip(RoundedCornerShape(16.dp))
            .shadow(12.dp, RoundedCornerShape(16.dp), clip = false)
            .background(Color(0xFF1A1A1A))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                        startY = 200f
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = movie.title,
                color = Color.White,
                fontSize = 14.sp
            )
            Text(
                text = "⭐ ${movie.rating}",
                color = Color(0xFFB3B3B3),
                fontSize = 12.sp
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WatchlistCardPreview() {
    WatchlistCard(
        movie = Movie(
            id = 1,
            title = "Preview Movie",
            genre = "Action",
            rating = 8.5,
            posterUrl = "https://via.placeholder.com/300",
            description = "A great movie description"
        ),
        onClick = {}
    )
}
