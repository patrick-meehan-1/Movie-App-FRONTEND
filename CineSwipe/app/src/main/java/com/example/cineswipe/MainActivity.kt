package com.example.cineswipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cineswipe.ui.theme.CineSwipeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CineSwipeTheme {
                var showLanding by remember { mutableStateOf(true) }

                if (showLanding) {
                    LandingPage(
                        onStartClick = { showLanding = false }
                    )
                } else {
                    RandomMovieScreen()
                }
            }
        }

    }
}

@Composable
fun RandomMovieScreen() {
    var movieTitle by remember { mutableStateOf("Loading...") }

    LaunchedEffect(Unit) {
        try {
            val movie = RetrofitClient.api.getRandomMovie()
            movieTitle = movie.title
        } catch (_: Exception) {
            movieTitle = "Error loading movie"
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = movieTitle,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRandomMovieScreen() {
    CineSwipeTheme {
        RandomMovieScreen()
    }
}
