package com.magnificsoftware.letswatch.api

import com.magnificsoftware.letswatch.data_class.plain.auth.signin.SigninRequest
import com.magnificsoftware.letswatch.data_class.plain.auth.signin.SigninResponse
import com.magnificsoftware.letswatch.data_class.plain.auth.signup.SignupRequest
import com.magnificsoftware.letswatch.data_class.plain.auth.signup.SignupResponse
import com.magnificsoftware.letswatch.data_class.plain.auth.userdata.UserProfileResponse
import com.magnificsoftware.letswatch.data_class.plain.auth.verify_otp.VerifyOTPRequest
import com.magnificsoftware.letswatch.data_class.plain.auth.verify_otp.VerifyOTPResponse
import com.magnificsoftware.letswatch.util.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.concurrent.Future

interface LetsWatchAsyncService {
    @GET("user_detail/user_profile")
    fun getUser(@Header("Authorization") login: String): Future<ApiResponse<UserProfileResponse>>

    @POST("user/login")
    fun signIn(@Body request: SigninRequest): Future<ApiResponse<SigninResponse>>

    @POST("user/register")
    fun signUp(@Body request: SignupRequest): Future<ApiResponse<SignupResponse>>

    @POST("user/verify-otp")
    fun verifySignUp(@Body request: VerifyOTPRequest): Future<ApiResponse<VerifyOTPResponse>>
}
