package com.umt.memingoapp

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.umt.memingoapp.models.responses.CourseResponse
import com.umt.memingoapp.models.responses.Language
import com.umt.memingoapp.ui.theme.LogoColor
import com.umt.memingoapp.uiComponents.LoadingAnimation
import com.umt.memingoapp.uiComponents.MemingoButton
import com.umt.memingoapp.viewModelFactories.CourseViewModelFactory
import com.umt.memingoapp.viewmodels.ResultType
import com.umt.memingoapp.viewmodels.CourseViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch




@Composable
fun CourseSelectionScreen(
    navController: NavHostController,
    viewModel: CourseViewModel = viewModel(
        factory = CourseViewModelFactory(LocalContext.current.applicationContext as Application)
    )
) {
    val scope = rememberCoroutineScope()
    var courses by remember { mutableStateOf<List<Language>?>(emptyList()) }
    var showAddLanguageDialog by remember { mutableStateOf(false) }
    var selectedLanguageId by remember { mutableStateOf<String?>(null) }
    var showError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }



    // Function to refresh courses
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

    // Add Language Dialog
    if (showAddLanguageDialog) {
        LanguageDialog(
            onAddLanguage = { languageId ->
                selectedLanguageId = languageId
                scope.launch {
                    viewModel.addLanguage(languageId).collect { result ->
                        when (result) {
                            is ResultType.Success -> {
                                showAddLanguageDialog = false
                                refreshCourses()
                            }
                            is ResultType.Error -> showError = result.message
                            ResultType.Loading -> isLoading = true
                        }
                    }
                }
            },
            onDismiss = { showAddLanguageDialog = false }
        )
    }

    // Main content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Header
        Text(
            text = "Choose a Course",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
        )

        Text(
            text = "Select a language course to start your learning journey",
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Display grid of languages or error/loading states
        when {
            isLoading -> LoadingIndicator()
            showError != null -> ErrorMessage(message = showError!!)
            else -> {
                if (courses != null) {
                    CourseList(
                        courses = courses!!,
                        onCourseSelected = { selectedLanguageId = it },
                        onAddLanguage = { showAddLanguageDialog = true },
                        navController = navController
                    )
                }
            }
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
    val context = LocalContext.current
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(courses) { course ->
            CourseCard(
                course = course,
                onClick = {
                    Toast.makeText(context, "To be Implemented Soon, Stay tuned buddy!!!", Toast.LENGTH_SHORT).show()
                    navController.navigate("alphabet_lesson")
                }
            )
        }

        item {
            AddCourseCard(onAddClick = onAddLanguage)
        }
    }
}


@Composable
private fun CourseCard(
    course: Language,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Flag and title row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                // Flag image
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorScheme.background)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = course.flag,
                            imageLoader = ImageLoader.Builder(context)
                                .components {
                                    add(SvgDecoder.Factory())
                                }
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

                // Course title
                Text(
                    text = course.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // Description
            Text(
                text = "Learn ${course.name} with Mermingo",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Level and topics row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                // Level chip
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    // hexa code of green color of success ->
                    color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = "beginner",
                        color = Color(0xFF4CAF50),
                        // do it bold
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Topics count
                Text(
                    text = "4 topics",
                    style = MaterialTheme.typography.labelMedium,
                    color = colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Start button
            MemingoButton(
                text = "Start Learning",
                onClick = { /* Do Something */ },
                containerColor = Color.White,
                textColor = LogoColor,
                borderColors = listOf(LogoColor, Color.Cyan)
            )

        }
    }
}




@Composable
private fun AddCourseCard(
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceVariant
        ),
        onClick = onAddClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "Add course",
                tint = LogoColor,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Add New Language",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Explore more language courses",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurface.copy(alpha = 0.6f)
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
    val availableCourses = remember { mutableStateOf<List<CourseResponse>>(emptyList()) }
    val selectedCourse = remember { mutableStateOf<CourseResponse?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val expanded = remember { mutableStateOf(false) }

    val scaleAnim = remember { Animatable(0.8f) }
    val scope = rememberCoroutineScope()

    // Collect both available and user-added courses
    LaunchedEffect(true) {
        scaleAnim.animateTo(1f, tween(300))
        val available = viewModel.getAvailableCourses()
        val userCourses = viewModel.getUserLanguages()
//        availableCourses.value = available.filter { course ->
//            userCourses.none { it.id == course.id }
//        }

        isLoading.value = false
    }


    LaunchedEffect(true) {
        scaleAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        )
    }

    LaunchedEffect(Unit) {
        scaleAnim.animateTo(1f, tween(300, easing = FastOutSlowInEasing))
        viewModel.getAvailableCourses().collect { result ->
            when (result) {
                is ResultType.Success -> {
                    availableCourses.value = result.data
                    isLoading.value = false
                }

                is ResultType.Error -> {
                    errorMessage.value = result.message
                    isLoading.value = false
                }

                ResultType.Loading -> {
                    isLoading.value = true
                }
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
            color = colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Add New Language",
                    style = MaterialTheme.typography.titleLarge,
                    color = LogoColor
                )

                when {
                    isLoading.value -> {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(color = LogoColor)
                        }
                    }

                    errorMessage.value != null -> {
                        Text(
                            text = "Error: ${errorMessage.value}",
                            color = colorScheme.error
                        )
                    }

                    else -> {
                        ExposedDropdownMenuBox(
                            expanded = expanded.value,
                            onExpandedChange = { expanded.value = !expanded.value }
                        ) {
                            TextField(
                                value = selectedCourse.value?.title ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Select a Course") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = LogoColor.copy(alpha = 0.1f),
                                    focusedIndicatorColor = LogoColor,
                                    unfocusedIndicatorColor = LogoColor,
                                    focusedLabelColor = LogoColor
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = expanded.value,
                                onDismissRequest = { expanded.value = false }
                            ) {
                                availableCourses.value.forEach { course ->
                                    DropdownMenuItem(
                                        text = { Text(course.title) },
                                        onClick = {
                                            selectedCourse.value = course
                                            expanded.value = false
                                        }
                                    )
                                }
                            }
                        }

                        selectedCourse.value?.let {
                            Text(
                                text = "Selected: ${it.title}",
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            selectedCourse.value?.let {
                                onAddLanguage(it.language_id)
                            }
                        },
                        enabled = selectedCourse.value != null,
                        colors = ButtonDefaults.buttonColors(containerColor = LogoColor)
                    ) {
                        Text("Add", color = Color.White)
                    }
                }
            }
        }
    }
}



@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
       LoadingAnimation()
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .width(300.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Error",
                    color = colorScheme.error,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = message)
            }
        }
    }
}
