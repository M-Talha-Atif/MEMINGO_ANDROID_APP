package com.umt.memingoapp.presentation.course_selection.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.umt.memingoapp.domain.model.request.UpdateLanguagePreferenceRequest
import com.umt.memingoapp.domain.model.request.UpdateProgressRequest
import com.umt.memingoapp.domain.model.response.CourseResponse
import com.umt.memingoapp.domain.model.response.Language
import com.umt.memingoapp.domain.model.response.LessonResponse
import com.umt.memingoapp.domain.model.response.SingleCourseResponse
import com.umt.memingoapp.data.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.Dispatchers



class CourseViewModel(application: Application) : AndroidViewModel(application){

    private val courseRepository = CourseRepository(application.applicationContext)

    fun getAvailableCourses(): Flow<ResultType<List<CourseResponse>>> = flow {
        try {
            val courses = courseRepository.getAvailableCourses()
            emit(ResultType.Success(courses))
        } catch (e: Exception) {
            Log.d("Error in available courses", e.message.toString())
            emit(ResultType.Error(e.message ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)

    fun getLesson(courseId: String): Flow<ResultType<LessonResponse>> = flow {
        try {
            val lesson = courseRepository.getLesson(courseId)
            emit(ResultType.Success(lesson))
        } catch (e: Exception) {
            emit(ResultType.Error(e.message ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)

    fun addLanguage(languageId: String): Flow<ResultType<Unit>> = flow {
        try {
            courseRepository.addLanguage(languageId)
            emit(ResultType.Success(Unit))
        } catch (e: Exception) {
            Log.d("Error in add language", e.message.toString())
            emit(ResultType.Error(e.message ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)

    fun updateLessonProgress(courseId: String, lessonId: String, score: String = "3"): Flow<ResultType<Unit>> = flow {
        try {
            val request = UpdateProgressRequest(courseId, lessonId, score)
            courseRepository.updateLessonProgress(request)
            emit(ResultType.Success(Unit))
        } catch (e: Exception) {
            emit(ResultType.Error(e.message ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)

    fun updateLanguagePreference(language: String): Flow<ResultType<Unit>> = flow {
        try {
            val request = UpdateLanguagePreferenceRequest(language)

            courseRepository.updateLanguagePreference(request)
            emit(ResultType.Success(Unit))
        } catch (e: Exception) {
            emit(ResultType.Error(e.message ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)

    fun getUserLanguages(): Flow<ResultType<List<Language>?>> = flow {
        try {
            // Call the service to get the list of languages
            val data = courseRepository.getUserLanguages()

            for (m in data.languages!!) {
                // Check if the language is not null before logging its name
                m.let {
                    Log.d("Language Name", it.name)  // Logging the name of each language
                }
            }


            // Emit the result as Success
            emit(ResultType.Success(data.languages))
        } catch (e: Exception) {
            Log.d("Error in user languages", e.message.toString())
            // If there's an error, emit the error message
            emit(ResultType.Error(e.message ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)


    fun getCourseDetails(courseId: String): Flow<ResultType<SingleCourseResponse>> = flow {
        try {
            val details = courseRepository.getCourseDetails(courseId)
            emit(ResultType.Success(details))
        } catch (e: Exception) {
            emit(ResultType.Error(e.message ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)





}

// ResultType sealed class
sealed class ResultType<out T> {
    data class Success<out T>(val data: T) : ResultType<T>()
    data class Error(val message: String) : ResultType<Nothing>()
    object Loading : ResultType<Nothing>()
}

