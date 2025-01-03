package com.umt.memingoapp.services
import com.umt.memingoapp.models.requests.LoginRequest
import com.umt.memingoapp.models.requests.SignupRequest
import com.umt.memingoapp.models.requests.VerifyOtpRequest
import com.umt.memingoapp.models.responses.ApiResponse
import com.umt.memingoapp.models.responses.LoginResponse
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
