package com.umt.memingoapp.repositories


import android.content.Context
import com.umt.memingoapp.client.CourseRetrofitClient
import com.umt.memingoapp.models.requests.UpdateLanguagePreferenceRequest
import com.umt.memingoapp.models.requests.UpdateProgressRequest
import com.umt.memingoapp.models.responses.CourseResponse
import com.umt.memingoapp.models.responses.Language
import com.umt.memingoapp.models.responses.LessonResponse
import com.umt.memingoapp.models.responses.UpdateLessonSubmitRequest
import com.umt.memingoapp.models.responses.UpdateLessonSubmitResponse
import com.umt.memingoapp.models.responses.UserLanguagesResponse

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


