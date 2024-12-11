package com.umt.memingoapp.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umt.memingoapp.client.RetrofitClient
import com.umt.memingoapp.models.requests.LoginRequest
import com.umt.memingoapp.models.requests.SignupRequest
import com.umt.memingoapp.models.requests.VerifyOtpRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _otp = MutableStateFlow("")
    val otp: StateFlow<String> = _otp

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun onConfirmPasswordChanged(newPassword: String) {
        _confirmPassword.value = newPassword
    }

    fun onUsernameChanged(newUsername: String) {
        _username.value = newUsername
    }

    fun onOtpChanged(newOtp: String) {
        _otp.value = newOtp
    }

    fun login(onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(
                    email = email.value,
                    password = password.value
                )
                val response = RetrofitClient.instance.login(loginRequest)
                onResult(true, null) // Login successful
            } catch (e: Exception) {
                onResult(false, e.localizedMessage ?: "Login failed")
            }
        }
    }

    fun signup(onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val signupRequest = SignupRequest(
                    email = email.value,
                    password = password.value
                )
                val response = RetrofitClient.instance.signup(signupRequest)
                onResult(true, response.message)
            } catch (e: Exception) {
                onResult(false, e.localizedMessage ?: "Signup failed")
            }
        }
    }

    fun verifyOtp(onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val verifyOtpRequest = VerifyOtpRequest(
                    email = email.value,
                    otp = otp.value
                )
                val response = RetrofitClient.instance.verifyOtp(verifyOtpRequest)
                onResult(true, response.message)
            } catch (e: Exception) {
                onResult(false, e.localizedMessage ?: "OTP verification failed")
            }
        }
    }
}
