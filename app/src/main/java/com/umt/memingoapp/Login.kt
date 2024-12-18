package com.umt.memingoapp

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.umt.memingoapp.models.AuthViewModel
import com.umt.memingoapp.models.AuthViewModelFactory
import com.umt.memingoapp.models.Screen
import com.umt.memingoapp.ui.theme.LogoColor


@Preview
@Composable
fun LoginFormPreview(){
    val mockNavController = rememberNavController()
    LoginForm(mockNavController)
}

@Composable
fun LoginForm(navController: NavHostController,     authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(LocalContext.current.applicationContext as Application))) {
    val email by authViewModel.email.collectAsState()
    val password by authViewModel.password.collectAsState()

    Surface {
        val context = LocalContext.current
        TopHeader()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.av3),
                contentDescription = "LoginImage",
                modifier = Modifier.size(350.dp)
            )

            Text("Login", style = TextStyle(color = Color.Black, fontSize = 18.sp))
            Spacer(Modifier.height(16.dp))
            Text("Welcome Back", style = TextStyle(color = Color.Black, fontSize = 28.sp, fontWeight = FontWeight.Bold))
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

                    authViewModel.login { isSuccess, errorMessage ->
                        if (isSuccess) {
                            navController.navigate(Screen.HomeScreen.route) // Navigate to home screen
                        } else {
                            Toast.makeText(context, errorMessage ?: "Login failed", Toast.LENGTH_SHORT).show()
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
    }
}
