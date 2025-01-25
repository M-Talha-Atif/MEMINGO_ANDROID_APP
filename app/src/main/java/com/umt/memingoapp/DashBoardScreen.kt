package com.umt.memingoapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage


@Preview(showBackground = true)
@Composable
fun PreviewDashboardScreen() {
    DashboardScreenWithLessons()

}

@Composable
fun DashboardScreenWithLessons() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Hello, Username
            Text(
                text = "Hello, Username",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )

            // Circular Profile Picture
            Image(
                painter = painterResource(id = R.drawable.p3), // Replace with your drawable resource
                contentDescription = "User Profile Picture",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        // Lessons Section
        Text(
            text = "Lessons",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Lessons List
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(lessonData) { lesson ->
                LessonCard(lesson)
            }
        }
    }
}

@Composable
fun LessonCard(lesson: Lesson) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = lesson.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Button(
                    onClick = { /* Add your action */ },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(text = lesson.buttonText)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Image(
                painter = painterResource(id = lesson.imageRes), // Replace with your drawable resource
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}
    // Lesson Data Model
    data class Lesson(
        val title: String,
        val description: String,
        val buttonText: String,
        val imageRes: Int
    )

// Dummy Data for Lessons
val lessonData = listOf(
    Lesson(
        title = "Basics 1",
        description = "Learn the basics of French",
        buttonText = "Start",
        imageRes = R.drawable.german // Replace with your drawable resource
    ),
    Lesson(
        title = "Basics 2",
        description = "Continue learning the basics of French",
        buttonText = "Unlock",
        imageRes = R.drawable.german // Replace with your drawable resource
    ),
    Lesson(
        title = "Travel",
        description = "Learn how to travel in France",
        buttonText = "Unlock",
        imageRes = R.drawable.german // Replace with your drawable resource
    ),
    Lesson(
        title = "Food",
        description = "Learn how to order food in France",
        buttonText = "Unlock",
        imageRes = R.drawable.german // Replace with your drawable resource
    )
)


