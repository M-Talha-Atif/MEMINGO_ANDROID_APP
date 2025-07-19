package com.umt.memingoapp.data.repository

import android.content.Context
import com.umt.memingoapp.data.source.remote.client.CourseRetrofitClient
import com.umt.memingoapp.domain.model.request.UpdateLanguagePreferenceRequest
import com.umt.memingoapp.domain.model.request.UpdateProgressRequest
import com.umt.memingoapp.domain.model.request.UpdateLessonSubmitRequest
import com.umt.memingoapp.domain.model.response.LessonResponse
import com.umt.memingoapp.domain.model.response.UpdateLessonSubmitResponse
import com.umt.memingoapp.domain.model.response.UserLanguagesResponse
import com.umt.memingoapp.domain.repository.CourseRepository
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor(
    context: Context
) : CourseRepository{

    private val service = CourseRetrofitClient.create(context)

    override suspend fun getAvailableCourses() = service.getAvailableCourses()

    override suspend fun addLanguage(languageId: String) =
        service.addLanguage(mapOf("language_id" to languageId))

    override suspend fun updateLanguagePreference(request: UpdateLanguagePreferenceRequest) =
        service.updateLanguagePreference(request)

    override suspend fun getUserLanguages(): UserLanguagesResponse = service.getUserLanguages()

    override suspend fun getCourseDetails(courseId: String) =
        service.getCourseDetails(courseId)

    override suspend fun getLesson(lessonId: String): LessonResponse =
        service.getLesson(lessonId)

    override suspend fun updateLessonProgress(request: UpdateProgressRequest) =
        service.updateLessonProgress(request)

    override suspend fun submitLessonResults(
        lessonId: String,
        request: UpdateLessonSubmitRequest
    ): UpdateLessonSubmitResponse =
        service.submitLessonResults(lessonId, request)
}
