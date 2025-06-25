package com.umt.memingoapp.data.source.remote.service
import com.umt.memingoapp.domain.model.request.UpdateLanguagePreferenceRequest
import com.umt.memingoapp.domain.model.request.UpdateProgressRequest
import com.umt.memingoapp.domain.model.response.CourseResponse
import com.umt.memingoapp.domain.model.response.LessonResponse
import com.umt.memingoapp.domain.model.response.ApiResponse
import com.umt.memingoapp.domain.model.response.SingleCourseResponse
import com.umt.memingoapp.domain.model.request.UpdateLessonSubmitRequest
import com.umt.memingoapp.domain.model.response.UpdateLessonSubmitResponse
import com.umt.memingoapp.domain.model.response.UserLanguagesResponse
import retrofit2.http.*

interface CourseService {

    @GET("/courses")
    suspend fun getAvailableCourses(): List<CourseResponse>

    @POST("/courses/user/add_language")
    suspend fun addLanguage(@Body body: Map<String, String>): ApiResponse

    @GET("/lessons/{lesson_id}")
    suspend fun getLesson(@Path("lesson_id") lessonId: String): LessonResponse

    @POST("/lesson/update_progress")
    suspend fun updateLessonProgress(@Body request: UpdateProgressRequest): ApiResponse

    @POST("/auth/update-language-preference")
    suspend fun updateLanguagePreference(@Body request: UpdateLanguagePreferenceRequest): ApiResponse

    @GET("/courses/user/languages")
    suspend fun getUserLanguages(): UserLanguagesResponse


    @GET("/courses/{course_id}")
    suspend fun getCourseDetails(@Path("course_id") courseId: String): SingleCourseResponse

    @POST("/lessons/{lesson_id}/submit")
    suspend fun submitLessonResults(
        @Path("lesson_id") lessonId: String,
        @Body request: UpdateLessonSubmitRequest
    ): UpdateLessonSubmitResponse

}
