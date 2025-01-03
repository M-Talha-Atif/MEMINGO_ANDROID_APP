package com.umt.memingoapp.viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.umt.memingoapp.client.AuthenticationRetrofitClient
import com.umt.memingoapp.models.requests.LoginRequest
import com.umt.memingoapp.models.requests.SignupRequest
import com.umt.memingoapp.models.requests.VerifyOtpRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application : Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    private val sharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

    private val _email = MutableStateFlow(getPersistedEmail())
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


    fun onPhoneNumberTextChanged(number: String) {
        _phoneNumber.value = number
    }

    fun onGoalDropDownTextChanged(newGoal: String) {
        _goalDropDownText.value = newGoal
    }


    private fun getPersistedEmail(): String {
        return sharedPreferences.getString("email", "") ?: ""
    }

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
        saveEmailToPreferences(newEmail) // Persist the updated email
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

                onResult(true, null) // Login successful
            } catch (e: Exception) {
                onResult(false, e.localizedMessage ?: "Login failed")
            }
        }
    }

    private fun saveEmailToPreferences(email: String) {
        sharedPreferences.edit().putString("email", email).apply()
    }

    fun saveCredentialsToPreferences(email: String, password: String) {
        sharedPreferences.edit().putString("email", email).apply()
        sharedPreferences.edit().putString("password", password).apply()
    }

    fun signup(onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val signupRequest = SignupRequest(
                    email = email.value,
                    password = password.value,
                    phone = phoneNumber.value,
                    learning_goal = goalDropDownText.value
                )
                val response = AuthenticationRetrofitClient.instance.signup(signupRequest)
                // store the email to use it in otp
                saveEmailToPreferences(email.value)
                onResult(true, response.message)
            } catch (e: Exception) {
                onResult(false, e.localizedMessage ?: "Signup failed")
            }
        }
    }



    fun verifyOtp(onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val verifyOtpRequest = VerifyOtpRequest(
                    email =  getPersistedEmail(),
                    otp = otp.value
                )
                Log.d("Email",  getPersistedEmail())
                Log.d("OTP", otp.value.toString())
                val response = AuthenticationRetrofitClient.instance.verifyOtp(verifyOtpRequest)
                onResult(true, response.message)
            } catch (e: Exception) {
                onResult(false, e.localizedMessage ?: "OTP verification failed")
            }
        }
    }
}
