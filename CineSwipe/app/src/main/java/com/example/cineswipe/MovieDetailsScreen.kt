package com.example.cineswipe

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun DetailsPreview() {
    MovieDetailsScreen(
        movie = Movie(
            id = 1,
            title = "Preview Movie",
            genre = "Action",
            rating = 8.5,
            posterUrl = "https://via.placeholder.com/300"
        ),
        onBack = {}
    )
}

@Composable
fun MovieDetailsScreen(movie: Movie, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Button(onClick = onBack) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            modifier = Modifier.fillMaxWidth().height(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = movie.title)
        Text(text = movie.genre)
        Text(text = "⭐ ${movie.rating}")
    }
}
