package com.umt.memingoapp.presentation.profile.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.umt.memingoapp.R
import com.umt.memingoapp.presentation.theme.LogoColor
import com.umt.memingoapp.presentation.profile.viewmodel.ProfileViewModel
import com.umt.memingoapp.presentation.shared_components.SimpleField
import com.umt.memingoapp.presentation.shared_components.LoadingAnimation
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.material.icons.filled.ArrowDropDown
import com.umt.memingoapp.presentation.shared_components.ArrowBack
import com.umt.memingoapp.presentation.shared_components.PhoneField



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val userProfile by profileViewModel.userProfile.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()

    // Call fetchUserProfile when screen loads
    LaunchedEffect(Unit) {
        profileViewModel.fetchUserProfile()
    }

    // Populate fields when userProfile updates
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var goalDropDownText by remember { mutableStateOf("Select Goal") }

    LaunchedEffect(userProfile) {
        userProfile?.let {
            name = it.name ?: ""
            phone = it.phone ?: ""
            goalDropDownText = it.learning_goal ?: "Select Goal"
        }
    }

    var expanded by remember { mutableStateOf(false) }
    val visible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible.value = true }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = visible.value,
                enter = slideInVertically(initialOffsetY = { it / 2 })
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    // Profile Picture
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.av5),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Change Photo",
                            color = LogoColor,
                            modifier = Modifier.clickable { /* Handle photo change */ }
                        )
                    }

                    // Form Fields
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        SimpleField(
                            value = name,
                            onChange = { name = it },
                            label = "Full Name",
                            placeholder = "Enter your name",
                            startIcon = {
                                Icon(Icons.Default.Person, contentDescription = "Name", tint = LogoColor)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        PhoneField(
                            value = phone,
                            onChange = { phone = it },
                            label = "Phone Number",
                            placeholder = "Enter phone number",
                            startIcon = {
                                Icon(Icons.Default.Phone, contentDescription = "Phone", tint = LogoColor)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Learning Goal Dropdown
                        Box {
                            Button(
                                onClick = { expanded = !expanded },
                                shape = RoundedCornerShape(4.dp),
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
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(goalDropDownText)
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Drop Down")
                                }
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val goals = listOf("Travel", "Business", "Academic", "Personal Growth")
                                goals.forEach { goal ->
                                    DropdownMenuItem(
                                        text = { Text(goal) },
                                        onClick = {
                                            goalDropDownText = goal
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                when {
                                    name.isBlank() -> {
                                        Toast.makeText(context, "Name cannot be empty.", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    phone.isBlank() -> {
                                        Toast.makeText(context, "Phone number cannot be empty.", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    goalDropDownText == "Select Goal" -> {
                                        Toast.makeText(context, "Please select a goal.", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                }

                                profileViewModel.updateProfile(
                                    name = name,
                                    phone = phone,
                                    learningGoal = goalDropDownText
                                ) { success, error ->
                                    if (success) {
                                        Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                                        navController.popBackStack()
                                    } else {
                                        Toast.makeText(context, error ?: "Failed to update profile", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LogoColor,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Update Profile", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingAnimation()
                }
            }
        }
    }
}


