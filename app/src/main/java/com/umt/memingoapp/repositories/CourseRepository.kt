package com.umt.memingoapp.repositories


import android.content.Context
import com.umt.memingoapp.client.CourseRetrofitClient
import com.umt.memingoapp.models.requests.UpdateLanguagePreferenceRequest
import com.umt.memingoapp.models.requests.UpdateProgressRequest
import com.umt.memingoapp.models.responses.Language
import com.umt.memingoapp.models.responses.UserLanguagesResponse

class CourseRepository(private val context: Context) {

    private val service = CourseRetrofitClient.create(context)

    suspend fun getAvailableCourses() = service.getAvailableCourses()

    suspend fun addLanguage(languageId: String) =
        service.addLanguage(mapOf("language_id" to languageId))

    suspend fun getLesson(courseId: String) = service.getLesson(courseId)

    suspend fun updateLessonProgress(request: UpdateProgressRequest) =
        service.updateLessonProgress(request)

    suspend fun updateLanguagePreference(request: UpdateLanguagePreferenceRequest) =
        service.updateLanguagePreference(request)

    suspend fun getUserLanguages(): UserLanguagesResponse = service.getUserLanguages()
}


