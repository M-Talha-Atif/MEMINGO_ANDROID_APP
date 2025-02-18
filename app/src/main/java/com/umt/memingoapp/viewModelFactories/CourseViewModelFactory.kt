package com.umt.memingoapp.viewModelFactories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umt.memingoapp.repositories.CourseRepository
import com.umt.memingoapp.viewModels.CourseViewModel

class  CourseViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CourseViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
