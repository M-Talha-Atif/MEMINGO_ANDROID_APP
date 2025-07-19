package com.umt.memingoapp.presentation.auth.ui

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.umt.memingoapp.presentation.auth.viewmodel.AuthViewModel
import com.umt.memingoapp.presentation.auth.viewmodel.AuthViewModelFactory
import com.umt.memingoapp.presentation.navigation.routes.Screen
import com.umt.memingoapp.presentation.theme.LogoColor
import com.umt.memingoapp.presentation.shared_components.LoadingAnimation
import com.umt.memingoapp.presentation.shared_components.OtpTextField


@Composable
fun OtpScreen(
    navController: NavHostController,
    email: String,
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(LocalContext.current.applicationContext as Application))
) {
    val context = LocalContext.current
    val otp by authViewModel.otp.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()

    Surface {

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = "One Time Password",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Code has been sent to ",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = LogoColor
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                OtpTextField(
                    otpText = otp,
                    onOtpTextChange = { text, _ -> authViewModel.onOtpChanged(text) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        authViewModel.verifyOtp(email) { success, message ->
                            if (success) {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    },
                    enabled = otp.length == 6,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LogoColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Verify Now")
                }
            }

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


