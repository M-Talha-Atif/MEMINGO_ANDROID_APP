package com.umt.memingoapp

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.umt.memingoapp.models.responses.CourseResponse
import com.umt.memingoapp.models.responses.Language
import com.umt.memingoapp.ui.theme.LogoColor
import com.umt.memingoapp.viewModelFactories.CourseViewModelFactory
import com.umt.memingoapp.viewModels.CourseViewModel
import com.umt.memingoapp.viewModels.ResultType
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

    // Fetch courses on launch
    LaunchedEffect(Unit) {
        viewModel.getUserLanguages().collect { result ->
            when (result) {
                is ResultType.Success -> {
                    courses = result.data
                    isLoading = false
                }
                is ResultType.Error -> {
                    showError = result.message
                    isLoading = false
                }
                ResultType.Loading -> isLoading = true
            }
        }
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

    // Display grid of languages or error/loading states
    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        when {
            isLoading -> LoadingIndicator()
            showError != null -> ErrorMessage(message = showError!!)
            else -> {
                if (courses!=null) {
//                    CourseGrid(courses = courses!!) {
//                        selectedLanguageId = it
//
//                    }
                    CourseGrid(
                        courses = courses!!,
                        onCourseSelected = { selectedLanguageId = it },
                        onAddLanguage = { showAddLanguageDialog = true }
                    )
                }

            }
        }
    }
}

@Composable
private fun CourseGrid(
    courses: List<Language>,
    onCourseSelected: (String) -> Unit,
    onAddLanguage: () -> Unit
) {
    val context = LocalContext.current
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 200.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(courses) { course ->
            CourseCard(course = course, onClick = {

//                onCourseSelected(course.id)
                Toast.makeText(context, "To be Implemented Soon, Stay tuned buddy!!!", Toast.LENGTH_SHORT).show()


            }

            )

        }
        // Add new language card
        item {
            AddLanguageCard(onAddClick = onAddLanguage)
        }

    }
}

@Composable
private fun AddLanguageCard(
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        onClick = onAddClick
    ) {
        Box {

//            Image(
//                painter = painterResource(R.drawable.ic_add),
//                contentDescription = "Add Language",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(64.dp)
//
//            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

//                Spacer(modifier = Modifier.height(50.dp))


                Text(
                    text = "Learn a New Language",
                    fontSize = 20.sp, // Slightly larger for better readability
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Explore new cultures and expand your horizons",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                    //modifier = Modifier.padding(horizontal = .dp) // Padding for smaller screens
                )

                Spacer(modifier = Modifier.weight(1f)) // Push the button to the bottom

//                Button(
//                    onClick = onAddClick,
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = LogoColor
//                    )
//                ) {
//                    Text("Add")
//                }
                FloatingActionButton(
                    onClick = onAddClick,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor  = LogoColor,
                    shape = CircleShape
                            ,
                    elevation = FloatingActionButtonDefaults.elevation()


                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_add), // Replace with your "plus" icon
                        contentDescription = "Add Item",
                        modifier = Modifier.size(24.dp) // Icon size
                    )
                }

            }
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
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        onClick = onClick
    ) {
        Box {
            // Course Image
            Log.d("Course Image URL", course.flag)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(120.dp))

                Text(
                    text = course.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = course.name?: "Start learning ${course.id} today!",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LogoColor
                    )
                ) {
                    Text("Start Learning")
                }
            }
        }
    }
}

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

    // Fetch available courses from ViewModel
    LaunchedEffect(Unit) {
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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Language") },
        text = {
            Column {
                // Display loading spinner if courses are still loading
                if (isLoading.value) {
                    CircularProgressIndicator()
                } else {
                    // Display error message if there was an issue fetching courses
                    errorMessage.value?.let {
                        Text(text = "Error: $it", color = MaterialTheme.colorScheme.error)
                    }

                    // Display available courses in dropdown menu
                    Box {
                        // TextField as a button-like element with a label and clickable area to show dropdown
                        Button(
                            onClick = { expanded.value = !expanded.value },
                            shape = RoundedCornerShape(2.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LogoColor,
                                contentColor = Color.White
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween, // Align Text and Icon at opposite ends
                                verticalAlignment = Alignment.CenterVertically // Center align items vertically
                            ) {
                                Text(selectedCourse.value?.title ?: "Select a course") // Display selected course or default text
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown of course selector") // Icon at the end
                            }
                        }

                        // DropdownMenu for course selection
                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            availableCourses.value.forEach { course ->
                                DropdownMenuItem(
                                    text = { Text(course.title) },
                                    onClick = {
                                        selectedCourse.value = course // Set selected course
                                        expanded.value = false // Close the dropdown after selection
                                    }
                                )
                            }
                        }
                    }

                }

                // Display selected course
                selectedCourse.value?.let {
                    Text("Selected: ${it.title}")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedCourse.value?.let { course ->
                        Log.d("Course title", course.title)
                        Log.d("Course ID", course.language_id)
                        onAddLanguage(course.language_id)
                    }
                }
            ) {
                Text("Add Language")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}



@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
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
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = message)
            }
        }
    }
}
