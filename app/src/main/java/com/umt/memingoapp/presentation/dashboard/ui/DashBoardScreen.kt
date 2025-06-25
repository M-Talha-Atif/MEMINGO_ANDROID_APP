package com.umt.memingoapp.presentation.dashboard.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.umt.memingoapp.R
import com.umt.memingoapp.domain.model.response.SingleCourseResponse
import com.umt.memingoapp.domain.model.response.Topic
import com.umt.memingoapp.presentation.navigation.routes.Screen
import com.umt.memingoapp.ui.theme.LogoColor
import com.umt.memingoapp.presentation.shared_components.AppTypography
import com.umt.memingoapp.presentation.course_selection.viewmodel.CourseViewModel
import com.umt.memingoapp.presentation.course_selection.viewmodel.ResultType
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

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
    viewModel: CourseViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // State for the loaded course details and lessons
    val courseState = remember { mutableStateOf<SingleCourseResponse?>(null) }
    val lessonsState = remember { mutableStateListOf<Lesson>() }
    val scope = rememberCoroutineScope()


    // Fetch course details once (replace "courseId" with actual id)
    LaunchedEffect(Unit) {

        viewModel.getCourseDetails(courseId).collectLatest { result ->
            when (result) {
                is ResultType.Success -> {
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
                else -> { /* Handle loading/error UI if needed */ }
            }
        }
    }

    val course = courseState.value
    val courseName = courseId.substringBefore("-").lowercase()
    val lessons = lessonsState

    // Calculate completed/total lessons
    val totalLessons = lessons.size
    val completedLessons = lessons.count { !it.locked && it.score >= 100 }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController,courseId)
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Header with greeting and user profile
            item { HeaderSection() }

            // Language progress card using course info
            item {
                if (course != null) {
                    LanguageProgressCard(
                        language = course.title,
                        level = course.difficulty,
                        progress = (course.overallProgress / 100f).coerceIn(0.0, 1.0).toFloat(),
                        topics = course.topics
                    )
                } else {
                    LanguageProgressCard()
                }
            }

            // Learning modes section
            item {
                Text(
                    text = "Learning Modes",
                    style = AppTypography.titleLarge,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LearningModeCard(
                        title = "Lessons",
                        description = "Interactive exercises",
                        iconRes = R.drawable.restaurant,
                        onClick = { navController.navigate("lessons") }
                    )
                    LearningModeCard(
                        title = "Stories",
                        description = "Immerse in context",
                        iconRes = R.drawable.teacher,
                        onClick = { navController.navigate("stories") }
                    )
                    LearningModeCard(
                        title = "AR Scan",
                        description = "Real-world practice",
                        iconRes = R.drawable.plane,

                        onClick = { navController.navigate("${Screen.ARScreen.route}/${courseName}/${courseId}") }
                    )
                }
            }

            // Progress section (dynamic from course/lessons)
            item {
                LessonProgressSection(
                    completed = completedLessons,
                    total = totalLessons,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            // Lessons list header
            item {
                Text(
                    text = "Your Lessons",
                    style = AppTypography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Lessons list
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

@Composable
private fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Good morning,",
            style = AppTypography.titleLarge
        )

        IconButton(onClick = { /* Navigate to profile */ }) {
            Image(
                painter = painterResource(id = R.drawable.av1),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
private fun LanguageProgressCard(language: String = "German",
    level: String = "Intermediate",
    progress: Float = 0.65f,
    topics: List<Topic> = listOf(
        Topic("1", "Alphabet", 100.0),
        Topic("2", "Words", 83.0),
        Topic("3", "Sentences", 72.0)
    )
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = LogoColor.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = language,
                    style = AppTypography.titleMedium
                )
                Text(
                    text = level,
                    style = AppTypography.bodyMedium,
                    color = LogoColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = LogoColor,
                trackColor = LogoColor.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                topics.take(3).forEach {
                    ProgressItem(it.name, "${it.progress.toInt()}%")
                }
            }
        }
    }
}

@Composable
private fun ProgressItem(title: String, progress: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = AppTypography.labelSmall
        )
        Text(
            text = progress,
            style = AppTypography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
private fun LearningModeCard(
    title: String,
    description: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = AppTypography.labelLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = description,
                style = AppTypography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LessonProgressSection(completed: Int, total: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = LogoColor.copy(alpha = 0.05f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Lessons Progress",
                    style = AppTypography.titleMedium
                )
                Text(
                    text = "$completed of $total completed",
                    style = AppTypography.bodyMedium
                )
            }

            CircularProgressIndicator(
                progress = completed.toFloat() / total.toFloat(),
                modifier = Modifier.size(48.dp),
                color = LogoColor,
                strokeWidth = 6.dp,
                trackColor = LogoColor.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
private fun LessonListItem(lesson: Lesson, onItemClick: () -> Unit) {
    val isUnlocked = !lesson.locked

    val iconColor = when {
        lesson.locked -> Color.Gray
        lesson.score >= 80 -> Color(0xFF4CAF50)
        lesson.score >= 50 -> Color(0xFFFFA000)
        else -> Color(0xFFD32F2F)
    }

    val iconBgColor = iconColor.copy(alpha = 0.2f)

    val cardBorder = BorderStroke(1.dp, iconColor.copy(alpha = 0.4f))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 8.dp)
            .alpha(if (lesson.locked) 0.6f else 1f),
        shape = RoundedCornerShape(12.dp),
        border = cardBorder,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = { if (isUnlocked) onItemClick() },
        enabled = isUnlocked
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (lesson.locked) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(end = 6.dp)
                        )
                    }
                    Text(
                        text = lesson.title,
                        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = lesson.type,
                    style = AppTypography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                if (isUnlocked) {
                    Text(
                        text = "Quiz: ${lesson.score}%",
                        style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = iconColor,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .background(iconBgColor, shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        lesson.locked -> Icons.Default.Lock
                        lesson.score >= 100 -> Icons.Default.Check
                        else -> Icons.Default.PlayArrow
                    },
                    contentDescription = "Status Icon",
                    tint = iconColor
                )
            }
        }
    }
}





val ActiveColor = Color.White
val InactiveColor = Color.LightGray

@Composable
fun BottomNavigationBar(navController: NavController, courseId: String) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        containerColor = LogoColor
    ) {
        BottomNavItem( route = "${Screen.Dashboard.route}/$courseId", Icons.Default.Home, "Home", navController, currentDestination)
        BottomNavItem(route = "${Screen.Profile.route}/$courseId", Icons.Default.Person, "Profile", navController, currentDestination)
        BottomNavItem("chat", Icons.Default.Chat, "Chat", navController, currentDestination)
        BottomNavItem("leaderboard", Icons.Default.Leaderboard, "Leaderboard", navController, currentDestination)
    }
}

@Composable
private fun RowScope.BottomNavItem(
    route: String,
    icon: ImageVector,
    label: String,
    navController: NavController,
    currentRoute: String?
) {
    val isSelected = currentRoute == route
    NavigationBarItem(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) ActiveColor else InactiveColor
            )
        },
        label = {
            Text(
                text = label,
                color = if (isSelected) ActiveColor else InactiveColor
            )
        },
        selected = isSelected,
        onClick = {
            if (!isSelected) navController.navigate(route)
        },
        alwaysShowLabel = true
    )
}





// Data models
data class Lesson(
    val id: String,
    val title: String,
    val type: String,
    val score: Int,
    val locked: Boolean
)




