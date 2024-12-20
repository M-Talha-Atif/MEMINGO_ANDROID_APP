// Updated OtpScreen implementation and necessary files to support your requested setup.

package com.umt.memingoapp

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.umt.memingoapp.viewModels.AuthViewModel
import com.umt.memingoapp.viewModelFactories.AuthViewModelFactory
import com.umt.memingoapp.models.Screen


//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun otpScreenPreview() {
//    OtpScreen()
//}

@Composable
fun OtpScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(LocalContext.current.applicationContext as Application))

) {
    val context = LocalContext.current
    val otp by authViewModel.otp.collectAsState()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Enter OTP",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        OtpTextField(
            otpText = otp,
            onOtpTextChange = { text, _ ->
                authViewModel.onOtpChanged(text)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                authViewModel.verifyOtp { success, message ->
                    if (success) {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }

                    else {
//                        // Handle error message
                        Toast.makeText(context, "Error : ${message}!", Toast.LENGTH_SHORT).show()

//                        CoroutineScope(Dispatchers.Main).launch {
//                            Toast.makeText(context, "Error : ${message}!", Toast.LENGTH_SHORT).show()
//                        }


                    }
                }
            },
            enabled = otp.length == 6
        ) {
            Text(text = "Submit")
        }
    }
}
