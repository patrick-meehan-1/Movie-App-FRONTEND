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
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.cineswipe.ui.theme.CineSwipeTheme
import androidx.compose.material3.ButtonDefaults




@Composable
fun LandingPage(onStartClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF141414)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {

            Text(
                text = "CineSwipe",
                fontSize = 42.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Find your next movie",
                fontSize = 18.sp,
                color = Color(0xFFB3B3B3)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onStartClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE50914)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp)
            ) {
                Text(text = "Start", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun LandingPreview() {
    CineSwipeTheme {
        LandingPage(onStartClick = {})
    }
}

