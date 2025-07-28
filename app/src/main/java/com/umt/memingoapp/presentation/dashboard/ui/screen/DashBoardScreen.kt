package com.umt.memingoapp.presentation.dashboard.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.umt.memingoapp.R
import com.umt.memingoapp.domain.model.response.SingleCourseResponse
import com.umt.memingoapp.presentation.navigation.routes.Screen
import com.umt.memingoapp.presentation.theme.LogoColor
import com.umt.memingoapp.presentation.shared_components.AppTypography
import com.umt.memingoapp.presentation.course_selection.viewmodel.CourseViewModel
import com.umt.memingoapp.presentation.course_selection.viewmodel.ResultType
import com.umt.memingoapp.presentation.dashboard.ui.components.*
import com.umt.memingoapp.presentation.dashboard.ui.components.shimmer.*
import com.umt.memingoapp.presentation.profile.viewmodel.ProfileViewModel
import com.umt.memingoapp.presentation.shared_components.BottomNavigationBar
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalTime
import java.time.ZoneId

@Preview(showBackground = true)
@Composable
fun PreviewDashboardScreen() {
    val mockNavController = rememberNavController()
    // You might want to provide a mock CourseViewModel here for preview
    CourseHomeScreen(mockNavController, "german-1")
}
@Composable
fun CourseHomeScreen(
    navController: NavController,
    courseId: String,
    viewModel: CourseViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val courseState = remember { mutableStateOf<SingleCourseResponse?>(null) }
    val lessonsState = remember { mutableStateListOf<Lesson>() }

    val userProfile = profileViewModel.userProfile.collectAsState().value
    val dateText = remember { getCurrentFormattedDate() }

    val greetingText = remember(userProfile) {
        userProfile?.let {
            "${getGreeting()}, ${it.name}"
        } ?: "Hello!"
    }

    LaunchedEffect(Unit) {
        profileViewModel.fetchUserProfile()
    }

    LaunchedEffect(Unit) {
        viewModel.getCourseDetails(courseId).collectLatest { result ->
            if (result is ResultType.Success) {
                courseState.value = result.data
                lessonsState.clear()
                lessonsState.addAll(
                    result.data.lessons.map { l ->
                        Lesson(
                            id = l.id,
                            title = l.title,
                            type = l.type,
                            score = l.quizScore?.toInt() ?: 0,
                            locked = l.status == "locked"
                        )
                    }
                )
            }

        }
    }

    val course = courseState.value
    val courseName = courseId.substringBefore("-").lowercase()
    val lessons = lessonsState
    val totalLessons = lessons.size
    val completedLessons = lessons.count { !it.locked && it.score >= 100 }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, courseId) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                if (userProfile != null) {
                    HeaderBar(
                        dateText = dateText,
                        greetingText = greetingText,
                        avatarImage = painterResource(R.drawable.p1),
                        languageIcon = painterResource(R.drawable.german),
                        notificationIcon = painterResource(R.drawable.ic_notification),
                        notificationCount = 3,
                        onLanguageClick = {
                            navController.navigate(Screen.HomeScreen.route)
                        },
                        onNotificationClick = {}
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                if (course != null) {
                    LanguageProgressCard(
                        language = course.title,
                        level = course.difficulty,
                        progress = (course.overallProgress / 100f).coerceIn(0.0, 1.0).toFloat(),
                        topics = course.topics
                    )
                } else {
                    ShimmerLanguageProgressCard()
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "Learning Modes",
                    style = AppTypography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LearningModeCard(
                        title = "AI Chat",
                        description = "Talk to the AI assistant",
                        iconRes = R.drawable.av5, // replace with real icon
                        onClick = { navController.navigate(Screen.Chat.route) }
                    )
                    LearningModeCard(
                        title = "AR Scan",
                        description = "Object-based learning",
                        iconRes = R.drawable.av1,
                        onClick = {
                            navController.navigate("${Screen.ARScreen.route}/${courseName}/${courseId}")
                        }
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                if (course == null)
                    ShimmerLessonProgressSection(modifier = Modifier.padding(vertical = 16.dp))
                else
                    LessonProgressSection(
                        completed = completedLessons,
                        total = totalLessons,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
            }

            item {
                Text(
                    text = "Your Lessons",
                    style = AppTypography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (lessons.isEmpty() || course == null) {
                items(3) { ShimmerLessonListItem() }
            }
            else {

                items(lessons) { lesson ->
                    LessonListItem(
                        lesson = lesson,
                        onItemClick = {
                            if (!lesson.locked) {
                                navController.navigate("${Screen.LessonScreen.route}/${courseId}/${lesson.id}")
                            }
                        }
                    )
                }
            }
        }
    }
}


@SuppressLint("NewApi")
fun getCurrentFormattedDate(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM", Locale.getDefault())
    return currentDate.format(formatter)
}


@SuppressLint("NewApi")
fun getGreeting(): String {
    val currentHour = LocalTime.now(ZoneId.systemDefault()).hour
    return when (currentHour) {
        in 5..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        in 17..20 -> "Good Evening"
        else -> "Good Night"
    }
}


// Data models
data class Lesson(
    val id: String,
    val title: String,
    val type: String,
    val score: Int,
    val locked: Boolean
)




