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
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.shadow




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
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var movies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }


    LaunchedEffect(Unit) {
        try {
            movies = RetrofitClient.api.getAllMovies().shuffled()
        } catch (_: Exception) {}
        isLoading = false
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF141414),
                contentColor = Color.White
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            Icons.Filled.Movie,
                            contentDescription = "Swipe",
                            tint = if (selectedTab == 0) Color(0xFFE50914) else Color(0xFFB3B3B3)
                        )
                    },
                    label = {
                        Text(
                            "Swipe",
                            color = if (selectedTab == 0) Color.White else Color(0xFFB3B3B3)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            Icons.Filled.Bookmark,
                            contentDescription = "Watchlist",
                            tint = if (selectedTab == 1) Color(0xFFE50914) else Color(0xFFB3B3B3)
                        )
                    },
                    label = {
                        Text(
                            "Watchlist",
                            color = if (selectedTab == 1) Color.White else Color(0xFFB3B3B3)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = if (selectedTab == 2) Color(0xFFE50914) else Color(0xFFB3B3B3)
                        )
                    },
                    label = {
                        Text(
                            "Search",
                            color = if (selectedTab == 2) Color.White else Color(0xFFB3B3B3)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    ) { innerPadding ->


        Box(modifier = Modifier.padding(innerPadding)) {

            if (selectedMovie != null) {
                MovieDetailsScreen(
                    movie = selectedMovie!!,
                    onBack = { selectedMovie = null }
                )
            } else {
                when (selectedTab) {
                    0 -> SwipeScreen(movies, isLoading)
                    1 -> WatchlistScreen(movies, onMovieClick = { selectedMovie = it })
                    2 -> SearchScreen(onMovieClick = { selectedMovie = it })
                }
            }
        }
    }

}

@Composable
fun SwipeScreen(movies: List<Movie>, isLoading: Boolean) {
    var currentIndex by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            isLoading -> CircularProgressIndicator()
            currentIndex >= movies.size -> Text("You've seen everything!", fontSize = 18.sp)
            else -> key(movies[currentIndex].id) {
                MovieCard(
                    movie = movies[currentIndex],
                    onSwipeRight = {
                        scope.launch {
                            try {
                                RetrofitClient.api.addToWatchlist(WatchlistItem(movieId = movies[currentIndex].id, userId = USER_ID))
                            } catch (_: Exception) {}
                            currentIndex++
                        }
                    },
                    onSwipeLeft = { currentIndex++ }
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
    val isRight = offsetX.value > 0

    Box(
        modifier = Modifier
            .width(330.dp)
            .height(500.dp)
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .rotate(rotation)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF1A1A1A)) // Netflix dark card
            .shadow(20.dp, RoundedCornerShape(20.dp), clip = false)
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


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                        startY = 300f
                    )
                )
        )


        if (tintAlpha > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (isRight)
                            Color.Green.copy(alpha = tintAlpha)
                        else
                            Color(0xFFE50914).copy(alpha = tintAlpha)
                    )
            )
        }


        if (tintAlpha > 0.1f) {
            Icon(
                imageVector = if (isRight) Icons.Filled.Bookmark else Icons.Filled.Close,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(if (isRight) Alignment.TopEnd else Alignment.TopStart)
                    .padding(16.dp)
                    .size(48.dp)
            )
        }


        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = movie.title,
                color = Color.White,
                fontSize = 24.sp
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


@Preview(showBackground = true)
@Composable
fun MovieCardPreview() {
    MovieCard(
        movie = Movie(
            id = 1,
            title = "Preview Movie",
            genre = "Action",
            rating = 8.5,
            posterUrl = "https://via.placeholder.com/300",
            description = "Sample Description"
        ),
        onSwipeLeft = {},
        onSwipeRight = {}
    )
}

@Composable
fun NavBarContent() {
    var selectedTab by remember { mutableIntStateOf(0) }

    NavigationBar(
        containerColor = Color(0xFF141414),
        contentColor = Color.White
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            icon = {
                Icon(
                    Icons.Filled.Movie,
                    contentDescription = "Swipe",
                    tint = if (selectedTab == 0) Color(0xFFE50914) else Color(0xFFB3B3B3)
                )
            },
            label = {
                Text(
                    "Swipe",
                    color = if (selectedTab == 0) Color.White else Color(0xFFB3B3B3)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            icon = {
                Icon(
                    Icons.Filled.Bookmark,
                    contentDescription = "Watchlist",
                    tint = if (selectedTab == 1) Color(0xFFE50914) else Color(0xFFB3B3B3)
                )
            },
            label = {
                Text(
                    "Watchlist",
                    color = if (selectedTab == 1) Color.White else Color(0xFFB3B3B3)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { selectedTab = 2 },
            icon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = if (selectedTab == 2) Color(0xFFE50914) else Color(0xFFB3B3B3)
                )
            },
            label = {
                Text(
                    "Search",
                    color = if (selectedTab == 2) Color.White else Color(0xFFB3B3B3)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NavBarPreview() {
    NavBarContent()
}

@Preview(showBackground = true)
@Composable
fun NavBarScaffoldPreview() {
    CineSwipeTheme {
        Scaffold(
            bottomBar = { NavBarContent() }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color(0xFF141414))
            )
        }
    }
}
