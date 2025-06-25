package com.umt.memingoapp.data.repository


import android.content.Context
import com.umt.memingoapp.data.source.remote.client.CourseRetrofitClient
import com.umt.memingoapp.domain.model.request.UpdateLanguagePreferenceRequest
import com.umt.memingoapp.domain.model.request.UpdateProgressRequest
import com.umt.memingoapp.domain.model.response.LessonResponse
import com.umt.memingoapp.domain.model.request.UpdateLessonSubmitRequest
import com.umt.memingoapp.domain.model.response.UpdateLessonSubmitResponse
import com.umt.memingoapp.domain.model.response.UserLanguagesResponse

class CourseRepository(private val context: Context) {

    private val service = CourseRetrofitClient.create(context)

    suspend fun getAvailableCourses() = service.getAvailableCourses()

    suspend fun addLanguage(languageId: String) =
        service.addLanguage(mapOf("language_id" to languageId))

    suspend fun updateLanguagePreference(request: UpdateLanguagePreferenceRequest) =
        service.updateLanguagePreference(request)

    suspend fun getUserLanguages(): UserLanguagesResponse = service.getUserLanguages()

    
    // course detail by ID
    suspend fun getCourseDetails( courseId: String) = service.getCourseDetails(courseId)

    // lessons
    suspend fun getLesson(lessonId: String): LessonResponse =
        service.getLesson(lessonId)


    suspend fun updateLessonProgress(request: UpdateProgressRequest) =
        service.updateLessonProgress(request)

    // post lesson results
    suspend fun submitLessonResults(
        lessonId: String,
        request: UpdateLessonSubmitRequest
    ): UpdateLessonSubmitResponse {
        return service.submitLessonResults(lessonId, request)
    }

}


