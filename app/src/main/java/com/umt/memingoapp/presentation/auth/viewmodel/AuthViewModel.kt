package com.umt.memingoapp.presentation.auth.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.umt.memingoapp.data.source.remote.client.AuthenticationRetrofitClient
import com.umt.memingoapp.domain.model.request.LoginRequest
import com.umt.memingoapp.domain.model.request.SignupRequest
import com.umt.memingoapp.domain.model.request.VerifyOtpRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(application : Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    private val sharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

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

    private val _goalDropDownText = MutableStateFlow("Select Goal")
    val goalDropDownText: StateFlow<String> = _goalDropDownText


    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun setLoading(value: Boolean) {
        _isLoading.value = value
    }


    fun onPhoneNumberTextChanged(number: String) {
        _phoneNumber.value = number
    }

    fun onGoalDropDownTextChanged(newGoal: String) {
        _goalDropDownText.value = newGoal
    }




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
                val response = AuthenticationRetrofitClient.instance.login(loginRequest)
                // Save token to SharedPreferences
                sharedPreferences.edit().putString("token", response.access_token).apply()
                sharedPreferences.edit().putBoolean("is_first_time", false).apply()

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
                    name = username.value,
                    email = email.value,
                    password = password.value,
                    phone = phoneNumber.value,
                    learning_goal = goalDropDownText.value
                )
                val response = AuthenticationRetrofitClient.instance.signup(signupRequest)

                onResult(true, response.message)
                sharedPreferences.edit().putBoolean("is_first_time", false).apply()
            } catch (e: Exception) {
                onResult(false, e.localizedMessage ?: "Signup failed")
            }
        }
    }


    fun verifyOtp(email: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val request = VerifyOtpRequest(email = email, otp = otp.value)
                val response = withContext(Dispatchers.IO) {
                    AuthenticationRetrofitClient.instance.verifyOtp(request)
                }
                _isLoading.value = false
                onResult(true, response.message)
            } catch (e: Exception) {
                _isLoading.value = false
                onResult(false, e.localizedMessage ?: "OTP verification failed")
            }
        }
    }

}
