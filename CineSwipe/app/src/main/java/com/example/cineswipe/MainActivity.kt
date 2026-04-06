package com.example.cineswipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cineswipe.ui.theme.CineSwipeTheme
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

const val USER_ID = "user1"
const val SWIPE_THRESHOLD = 300f

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CineSwipeTheme {
                var showLanding by remember { mutableStateOf(true) }
                if (showLanding) {
                    LandingPage(onStartClick = { showLanding = false })
                } else {
                    SwipeScreen()
                }
            }
        }
    }
}

@Composable
fun SwipeScreen() {
    var movies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            movies = RetrofitClient.api.getAllMovies().shuffled()
        } catch (_: Exception) {}
        isLoading = false
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            isLoading -> CircularProgressIndicator()
            currentIndex >= movies.size -> Text("You've seen everything!", fontSize = 18.sp)
            else -> key(movies[currentIndex].id) {
                MovieCard(
                    movie = movies[currentIndex],
                    onSwipeLeft = {
                        scope.launch {
                            try {
                                RetrofitClient.api.addToWatchlist(WatchlistItem(movies[currentIndex].id, USER_ID))
                            } catch (_: Exception) {}
                            currentIndex++
                        }
                    },
                    onSwipeRight = { currentIndex++ }
                )
            }
        }
    }
}

@Composable
fun MovieCard(movie: Movie, onSwipeLeft: () -> Unit, onSwipeRight: () -> Unit) {
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val tintAlpha = (kotlin.math.abs(offsetX.value) / SWIPE_THRESHOLD).coerceIn(0f, 0.6f)
    val rotation = (offsetX.value / 30f).coerceIn(-15f, 15f)
    val isLeft = offsetX.value < 0

    Box(
        modifier = Modifier
            .width(320.dp)
            .height(480.dp)
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .rotate(rotation)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1a1a1a))
            .pointerInput(movie.id) {
                detectDragGestures(
                    onDragEnd = {
                        scope.launch {
                            when {
                                offsetX.value < -SWIPE_THRESHOLD -> {
                                    offsetX.animateTo(-2000f, spring())
                                    onSwipeLeft()
                                }
                                offsetX.value > SWIPE_THRESHOLD -> {
                                    offsetX.animateTo(2000f, spring())
                                    onSwipeRight()
                                }
                                else -> offsetX.animateTo(0f, spring())
                            }
                        }
                    },
                    onDragCancel = { scope.launch { offsetX.animateTo(0f, spring()) } },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch { offsetX.snapTo(offsetX.value + dragAmount.x) }
                    }
                )
            }
    ) {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        if (tintAlpha > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isLeft) Color.Green.copy(alpha = tintAlpha) else Color.Red.copy(alpha = tintAlpha))
            )
        }

        if (tintAlpha > 0.1f) {
            Icon(
                imageVector = if (isLeft) Icons.Filled.Bookmark else Icons.Filled.Close,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(if (isLeft) Alignment.TopStart else Alignment.TopEnd)
                    .padding(16.dp)
                    .size(48.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(16.dp)
        ) {
            Text(text = movie.title, color = Color.White, fontSize = 22.sp)
            Text(text = movie.genre, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            Text(text = "⭐ ${movie.rating}", color = Color.White, fontSize = 14.sp)
        }
    }
}
