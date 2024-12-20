package com.umt.memingoapp.services
import com.umt.memingoapp.models.requests.UpdateLanguagePreferenceRequest
import com.umt.memingoapp.models.requests.UpdateProgressRequest
import com.umt.memingoapp.models.responses.CourseResponse
import com.umt.memingoapp.models.responses.LessonResponse
import com.umt.memingoapp.models.responses.ApiResponse
import com.umt.memingoapp.models.responses.Language
import com.umt.memingoapp.models.responses.UserLanguagesResponse
import retrofit2.http.*

interface CourseService {

    @GET("/auth/courses")
    suspend fun getAvailableCourses(): List<CourseResponse>

    @POST("/auth/user/add_language")
    suspend fun addLanguage(@Body body: Map<String, String>): ApiResponse

    @GET("/auth/lesson/{course_id}")
    suspend fun getLesson(@Path("course_id") courseId: String): LessonResponse

    @POST("/auth/lesson/update_progress")
    suspend fun updateLessonProgress(@Body request: UpdateProgressRequest): ApiResponse

    @POST("/auth/update-language-preference")
    suspend fun updateLanguagePreference(@Body request: UpdateLanguagePreferenceRequest): ApiResponse

    @GET("/auth/user/languages")
    suspend fun getUserLanguages(): UserLanguagesResponse
}
