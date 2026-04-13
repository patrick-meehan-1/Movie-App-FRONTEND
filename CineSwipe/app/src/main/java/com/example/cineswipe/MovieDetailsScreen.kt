package com.example.cineswipe

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp



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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF141414))
            .verticalScroll(rememberScrollState())
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
        ) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent)
                        )
                    )
            )


            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(16.dp)
                    .size(36.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }


        Column(modifier = Modifier.padding(20.dp)) {

            Text(
                text = movie.title,
                color = Color.White,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${movie.genre} • ⭐ ${movie.rating}",
                color = Color(0xFFB3B3B3),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))


            Button(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE50914)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Add to Watchlist", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))


        }
    }
}

