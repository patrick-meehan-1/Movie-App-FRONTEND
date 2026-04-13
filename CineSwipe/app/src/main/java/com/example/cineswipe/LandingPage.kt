package com.example.cineswipe

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview





@Composable
fun LandingPage(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "CineSwipe", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Find your next movie", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = onStartClick) {
            Text(text = "Start")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LandingPreview() {
    LandingPage(onStartClick = {})
}
