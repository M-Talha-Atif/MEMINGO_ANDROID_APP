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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun SignUpFormPreview(){
    val mockNavController = rememberNavController()
    SignUpForm( mockNavController)
}

@Composable
fun SignUpForm(navController: NavHostController,     authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(LocalContext.current.applicationContext as Application))) {

    val username by authViewModel.username.collectAsState()
    val confirmPassword by authViewModel.confirmPassword.collectAsState()
    val email by authViewModel.email.collectAsState()
    val password by authViewModel.password.collectAsState()


    // you need to write toast outside as it gives error inside the onClick of button

    var toastMessage by remember { mutableStateOf<String?>(null) } // State to hold the toast message

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
                painter = painterResource(R.drawable.av7), contentDescription = "LoginImage",
                modifier = Modifier.size(350.dp)
            )
//            Spacer()


            Text(
                "Sign Up",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Learn and Grow",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.height(16.dp))

            // fields section here
            val leadingIconEmail = @Composable {
                Icon(
                    Icons.Default.Email,
                    contentDescription = "email",
                    tint = LogoColor
                )
            }
            val leadingIconUserName = @Composable {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "person",
                    tint = LogoColor
                )
            }

            SimpleField(
                onChange = { authViewModel.onUsernameChanged(it)},
                value = username,
                modifier = Modifier.fillMaxWidth(),
                label = "Username",
                placeholder = "Enter your username",
                startIcon = leadingIconUserName
            )
            Spacer(Modifier.height(16.dp))

            SimpleField(
                onChange = {
                    authViewModel.onEmailChanged(it)
                }, value = email, modifier = Modifier.fillMaxWidth(),
                label = "Email", placeholder = "Enter your email", startIcon = leadingIconEmail
            )

            Spacer(Modifier.height(16.dp))

            PasswordField(
                value = password,
                onChange = { authViewModel.onPasswordChanged(it)},
                submit = { },
                label = "Password",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            PasswordField(
                value = confirmPassword,
                onChange = { authViewModel.onConfirmPasswordChanged(it) },
                label = "Confirm password",
                submit = { },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    authViewModel.signup { isSuccess, errorMessage ->
                        if (isSuccess) {
                           // toastMessage = errorMessage ?: "SignUp Successful"
                           // Toast.makeText(context, "Signup successful!", Toast.LENGTH_SHORT).show()
//                            navController.navigate(Screen.Otp.route) // Navigate to login screen
                            navController.navigate(Screen.Otp.route + "?email=${authViewModel.email.value}")
                        } else {
                           // toastMessage = errorMessage ?: "SignUp failed"
                            Toast.makeText(context, errorMessage ?: "Signup failed", Toast.LENGTH_SHORT).show()
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
                Text("Sign Up")
            }


                Spacer(Modifier.height(16.dp))
                Row {
                    Text(
                        "Old user?",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Login",
                        style = TextStyle(
                            color = LogoColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.clickable(onClick = { navController.navigate(Screen.Login.route) }) // Make text clickable
                    )

                }

            }
        }
    }
