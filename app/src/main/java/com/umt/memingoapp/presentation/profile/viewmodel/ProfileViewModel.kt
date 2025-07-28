package com.umt.memingoapp.presentation.profile.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umt.memingoapp.domain.model.request.UpdateProfileRequest
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

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _userProfile = MutableStateFlow<UserProfileResponse?>(null)
    val userProfile: StateFlow<UserProfileResponse?> = _userProfile

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    private val _updateStatus = MutableStateFlow<Resource<Unit>?>(null)
    val updateStatus: StateFlow<Resource<Unit>?> = _updateStatus

    fun fetchUserProfile(onResult: (Boolean, String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

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

    fun updateProfile(
        name: String,
        phone: String,
        learningGoal: String,
        onResult: (Boolean, String?) -> Unit = { _, _ -> }
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _updateStatus.value = Resource.Loading()
            try {
                val response = repository.updateProfile(
                    UpdateProfileRequest(
                        name = name,
                        phone = phone,
                        learning_goal = learningGoal
                    )
                )
                _userProfile.value = response
                _updateStatus.value = Resource.Success(Unit)
                _isLoading.value = false
                onResult(true, null)
            } catch (e: Exception) {
                _updateStatus.value = Resource.Error(e.message ?: "Failed to update profile")
                _isLoading.value = false
                onResult(false, e.message ?: "Failed to update profile")
            }
        }
    }


    fun clearUpdateStatus() {
        _updateStatus.value = null
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }

}



sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}
