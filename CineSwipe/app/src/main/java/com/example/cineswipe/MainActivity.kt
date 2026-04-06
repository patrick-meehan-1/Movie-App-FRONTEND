package com.example.cineswipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cineswipe.ui.theme.CineSwipeTheme
import kotlin.math.roundToInt

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

val hardcodedMovie = Movie(
    id = 1,
    title = "Inception",
    genre = "Sci-Fi / Thriller",
    posterUrl = "",
    rating = 8.8
)

@Composable
fun SwipeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        MovieCard(movie = hardcodedMovie)
    }
}

@Composable
fun MovieCard(movie: Movie) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = spring(),
        label = "cardOffset"
    )

    val swipeThreshold = 300f
    val tintAlpha = (kotlin.math.abs(animatedOffsetX) / swipeThreshold).coerceIn(0f, 0.6f)
    val rotation = (animatedOffsetX / 30f).coerceIn(-15f, 15f)
    val isLeft = animatedOffsetX < 0

    Box(
        modifier = Modifier
            .width(320.dp)
            .height(480.dp)
            .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
            .rotate(rotation)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF5C6BC0))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = { offsetX = 0f },
                    onDragCancel = { offsetX = 0f },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                    }
                )
            }
    ) {
        // Swipe tint overlay
        if (tintAlpha > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isLeft) Color.Green.copy(alpha = tintAlpha) else Color.Red.copy(alpha = tintAlpha))
            )
        }

        // Swipe icon
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

        // Movie info at bottom
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

@Preview(showBackground = true)
@Composable
fun PreviewSwipeScreen() {
    CineSwipeTheme {
        SwipeScreen()
    }
}
