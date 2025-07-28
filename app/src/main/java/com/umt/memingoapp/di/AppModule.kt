package com.umt.memingoapp.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.umt.memingoapp.data.repository.CourseRepositoryImpl
import com.umt.memingoapp.data.repository.GeminiRepository
import com.umt.memingoapp.data.repository.LeaderboardRepositoryImpl
import com.umt.memingoapp.data.repository.ProfileRepositoryImpl
import com.umt.memingoapp.data.source.remote.service.AuthenticationService
import com.umt.memingoapp.data.source.remote.service.LeaderboardService
import com.umt.memingoapp.domain.model.response.Activity
import com.umt.memingoapp.domain.model.response.ActivityDeserializer
import com.umt.memingoapp.domain.repository.CourseRepository
import com.umt.memingoapp.domain.repository.LeaderboardRepository
import com.umt.memingoapp.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCourseRepository(
        @ApplicationContext context: Context
    ): CourseRepository {
        return CourseRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideTokenInterceptor(@ApplicationContext context: Context): Interceptor {
        return Interceptor { chain ->
            val sharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", null)
            val requestBuilder = chain.request().newBuilder()
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(requestBuilder.build())
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenInterceptor: Interceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(Activity::class.java, ActivityDeserializer())
            .create()

        return Retrofit.Builder()
            .baseUrl("https://memingo-backend-pi.vercel.app/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthenticationService {
        return retrofit.create(AuthenticationService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(api: AuthenticationService): ProfileRepository {
        return ProfileRepositoryImpl(api)
    }


    @Provides
    @Singleton
    fun provideLeaderboardApiService(retrofit: Retrofit): LeaderboardService {
        return retrofit.create(LeaderboardService::class.java)
    }

    @Provides
    @Singleton
    fun provideLeaderboardRepository(api: LeaderboardService): LeaderboardRepository {
        return LeaderboardRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideGeminiRepository(): GeminiRepository {
        val apiKey = "AIzaSyDwFEkSwh7qD2-5NUsGMUOReDow6xzvxSc"
        return GeminiRepository(apiKey)
    }

}