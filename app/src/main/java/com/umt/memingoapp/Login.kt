package com.umt.memingoapp

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.umt.memingoapp.viewmodels.AuthViewModel
import com.umt.memingoapp.viewModelFactories.AuthViewModelFactory
import com.umt.memingoapp.navigation.Screen
import com.umt.memingoapp.ui.theme.LogoColor
import com.umt.memingoapp.uiComponents.LoadingAnimation
import com.umt.memingoapp.uiComponents.PasswordField
import com.umt.memingoapp.uiComponents.SimpleField
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.*



@Preview
@Composable
fun LoginFormPreview(){
    val mockNavController = rememberNavController()
    LoginForm(mockNavController)
}

@Composable
fun LoginForm(navController: NavHostController,     authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(
    LocalContext.current.applicationContext as Application))) {
    val email by authViewModel.email.collectAsState()
    val password by authViewModel.password.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val visible = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible.value = true
    }


    Surface {
        val context = LocalContext.current

        Box(modifier = Modifier.fillMaxSize()) {


            AnimatedVisibility(
                visible = visible.value,
                enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight / 2 })
            ) {

            // Main Content
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TopHeader()
                Image(
                    painter = painterResource(R.drawable.av3),
                    contentDescription = "LoginImage",
                    modifier = Modifier.size(200.dp)
                )

                Text("Login", style = TextStyle(color = Color.Black, fontSize = 18.sp))
                Spacer(Modifier.height(16.dp))
                Text(
                    "Welcome Back",
                    style = TextStyle(color = Color.Black, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(16.dp))

                SimpleField(
                    onChange = { authViewModel.onEmailChanged(it) },
                    value = email,
                    modifier = Modifier.fillMaxWidth(),
                    label = "Email",
                    placeholder = "Enter your email",
                    startIcon = {
                        Icon(Icons.Default.Email, contentDescription = "", tint = LogoColor)
                    }
                )
                Spacer(Modifier.height(16.dp))

                PasswordField(
                    value = password,
                    onChange = { authViewModel.onPasswordChanged(it) },
                    submit = { },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        when {
                            email.isBlank() -> {
                                Toast.makeText(context, "Email cannot be empty.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                                Toast.makeText(context, "Enter a valid email address.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            password.length <= 5 -> {
                                Toast.makeText(context, "Password must be longer than 5 characters.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                        }

                        authViewModel.setLoading(true)
                        authViewModel.login { isSuccess, errorMessage ->
                            authViewModel.setLoading(false)
                            if (isSuccess) {
                                authViewModel.saveCredentialsToPreferences(email, password)
//                                navController.navigate(Screen.HomeScreen.route)
                                navController.navigate("dashboard")
                            } else {
                                val message = when {
                                    errorMessage != null -> {
                                        when {
                                            errorMessage.contains("AuthUserCollisionException") -> {
                                                "This email is already registered. Try logging in or use another email."
                                            }
                                            errorMessage.contains("AuthInvalidUserException") -> {
                                                "No account found with this email. Please check your email or sign up."
                                            }
                                            errorMessage.contains("wrong password") -> {
                                                "Incorrect password. Please try again."
                                            }
                                            errorMessage.contains("Network Error") -> {
                                                "Network error. Please check your connection and try again."
                                            }
                                            else -> {
                                                "Login failed. $errorMessage"
                                            }
                                        }
                                    }
                                    else -> "Login failed. Please check your email and password."
                                }
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LogoColor,
                        contentColor = Color.White
                    )
                ) {
                    Text("Login")
                }

                Spacer(Modifier.height(16.dp))
                Row {
                    Text("New user?", style = TextStyle(color = Color.Black, fontSize = 16.sp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "SignUp",
                        style = TextStyle(color = LogoColor, fontSize = 16.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.clickable { navController.navigate(Screen.SignUp.route) }
                    )
                }
            }
            // Loading Overlay
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)), // dim background
                    contentAlignment = Alignment.Center
                ) {
                    LoadingAnimation()
                }
            }
        }
    }

    }



}



