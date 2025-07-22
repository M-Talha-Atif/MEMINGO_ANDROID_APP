package com.umt.memingoapp.presentation.profile.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umt.memingoapp.domain.model.response.UserProfileResponse
import com.umt.memingoapp.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfileResponse?>(null)
    val userProfile: StateFlow<UserProfileResponse?> = _userProfile

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchUserProfile(onResult: (Boolean, String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
//                val token = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
//                    .getString("token", null)
//
//                if (token.isNullOrBlank()) {
//                    onResult(false, "Token not found")
//                    _isLoading.value = false
//                    return@launch
//                }

                val profile = repository.getUserProfile()
                _userProfile.value = profile
                _isLoading.value = false
                onResult(true, null)
            } catch (e: Exception) {
                _isLoading.value = false
                onResult(false, e.localizedMessage ?: "Failed to load profile")
            }
        }
    }
}

