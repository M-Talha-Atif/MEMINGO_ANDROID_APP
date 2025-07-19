package com.umt.memingoapp.presentation.course_selection.ui


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.umt.memingoapp.R
import com.umt.memingoapp.domain.model.response.CourseResponse
import com.umt.memingoapp.domain.model.response.Language
import com.umt.memingoapp.presentation.navigation.routes.Screen
import com.umt.memingoapp.presentation.shared_components.LoadingAnimation
import com.umt.memingoapp.presentation.shared_components.MemingoButton
import com.umt.memingoapp.presentation.course_selection.viewmodel.ResultType
import com.umt.memingoapp.presentation.course_selection.viewmodel.CourseViewModel
import com.umt.memingoapp.presentation.shared_components.ErrorMessage
import kotlinx.coroutines.launch

@Composable
fun CourseSelectionScreen(
    navController: NavHostController,
    viewModel: CourseViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    var courses by remember { mutableStateOf<List<Language>?>(null) }
    var showAddLanguageDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun refreshCourses() {
        isLoading = true
        scope.launch {
            viewModel.getUserLanguages().collect { result ->
                when (result) {
                    is ResultType.Success -> {
                        courses = result.data
                        isLoading = false
                        errorMessage = null
                    }
                    is ResultType.Error -> {
                        errorMessage = result.message
                        isLoading = false
                    }
                    ResultType.Loading -> isLoading = true
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        refreshCourses()
    }

    if (showAddLanguageDialog) {
        LanguageDialog(
            onAddLanguage = { languageId ->
                scope.launch {
                    viewModel.addLanguage(languageId).collect { result ->
                        when (result) {
                            is ResultType.Success -> {
                                showAddLanguageDialog = false
                                refreshCourses()
                            }
                            is ResultType.Error -> errorMessage = result.message
                            ResultType.Loading -> isLoading = true
                        }
                    }
                }
            },
            onDismiss = { showAddLanguageDialog = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Choose a Course",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
        )

        Text(
            text = "Select a language course to start your learning journey",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        when {
            isLoading -> LoadingAnimation()
            // error message
            errorMessage != null -> ErrorMessage(
                message = errorMessage!!,
                onRetry = { refreshCourses() }
            )
            courses.isNullOrEmpty() -> EmptyCourseList(onAddClick = { showAddLanguageDialog = true })
            else -> CourseList(
                courses = courses!!,
                onCourseSelected = { /* Handle selection */ },
                onAddLanguage = { showAddLanguageDialog = true },
                navController = navController
            )
        }
    }
}

@Composable
private fun CourseList(
    courses: List<Language>,
    onCourseSelected: (String) -> Unit,
    onAddLanguage: () -> Unit,
    navController: NavHostController
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(courses) { course ->
            CourseCard(
                course = course,
                onClick = {
                    navController.navigate("${Screen.Dashboard.route}/${course.id}")
                }
            )
        }
        item { AddCourseCard(onAddClick = onAddLanguage) }
    }
}

@Composable
private fun CourseCard(
    course: Language,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = course.flag,
                            imageLoader = ImageLoader.Builder(LocalContext.current)
                                .components { add(SvgDecoder.Factory()) }
                                .build(),
                            placeholder = painterResource(R.drawable.p4),
                            error = painterResource(R.drawable.p3)
                        ),
                        contentDescription = "${course.name} flag",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = course.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "Learn ${course.name} with Mermingo",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = "beginner",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = "4 topics",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            MemingoButton(
                text = "Start Learning",
                onClick = onClick,
                containerColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun AddCourseCard(onAddClick: () -> Unit) {
    Card(
        onClick = onAddClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = "Add course",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Add New Language",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Explore more language courses",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDialog(
    onAddLanguage: (String) -> Unit,
    onDismiss: () -> Unit,
    viewModel: CourseViewModel = viewModel()
) {
    var availableCourses by remember { mutableStateOf<List<CourseResponse>>(emptyList()) }
    var selectedCourse by remember { mutableStateOf<CourseResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val scaleAnim = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        scaleAnim.animateTo(1f, tween(300, easing = FastOutSlowInEasing))
        viewModel.getAvailableCourses().collect { result ->
            when (result) {
                is ResultType.Success -> {
                    availableCourses = result.data
                    isLoading = false
                }
                is ResultType.Error -> {
                    errorMessage = result.message
                    isLoading = false
                }
                ResultType.Loading -> isLoading = true
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scaleAnim.value),
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 12.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Add New Language",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                when {
                    isLoading -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.primary
                    )
                    errorMessage != null -> Text(
                        text = "Error: $errorMessage",
                        color = MaterialTheme.colorScheme.error
                    )
                    else -> {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            TextField(
                                value = selectedCourse?.title ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Select a Course") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                                    focusedLabelColor = MaterialTheme.colorScheme.primary
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                availableCourses.forEach { course ->
                                    DropdownMenuItem(
                                        text = { Text(course.title) },
                                        onClick = {
                                            selectedCourse = course
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        selectedCourse?.let {
                            Text(
                                text = "Selected: ${it.title}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
                    }

                    Button(
                        onClick = { selectedCourse?.let { onAddLanguage(it.language_id) } },
                        enabled = selectedCourse != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}





@Composable
private fun EmptyCourseList(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No courses available",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Add New Course")
        }
    }
}