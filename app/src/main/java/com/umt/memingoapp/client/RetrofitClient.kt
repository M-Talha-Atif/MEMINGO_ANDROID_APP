package com.umt.memingoapp.client

import com.umt.memingoapp.services.AuthenticationService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:5000"

//It logs the details of HTTP requests and responses (including request body) for easier troubleshooting
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
//    An OkHttpClient instance is created with the logging interceptor
//    added to it. This client is responsible for making the HTTP requests.
//    The addInterceptor(loggingInterceptor) ensures that logging is done
//    for every request/response.
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val instance: AuthenticationService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(AuthenticationService::class.java)
    }
}
