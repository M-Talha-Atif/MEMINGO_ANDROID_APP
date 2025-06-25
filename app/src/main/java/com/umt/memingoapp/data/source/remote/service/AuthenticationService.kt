package com.umt.memingoapp.data.source.remote.service
import com.umt.memingoapp.domain.model.request.LoginRequest
import com.umt.memingoapp.domain.model.request.SignupRequest
import com.umt.memingoapp.domain.model.request.VerifyOtpRequest
import com.umt.memingoapp.domain.model.response.ApiResponse
import com.umt.memingoapp.domain.model.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST



interface AuthenticationService {
    @POST("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("/auth/signup")
    suspend fun signup(@Body signupRequest: SignupRequest): ApiResponse

    @POST("/auth/verify-otp")
    suspend fun verifyOtp(@Body verifyOtpRequest: VerifyOtpRequest): ApiResponse


}
