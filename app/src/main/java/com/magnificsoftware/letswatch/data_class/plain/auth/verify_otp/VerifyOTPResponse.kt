package com.magnificsoftware.letswatch.data_class.plain.auth.verify_otp

import com.google.gson.annotations.SerializedName

data class VerifiedUserData(
    @field:SerializedName("user_id")
    val userId: String?,
    @field:SerializedName("first_name")
    val firstName: String?,
    @field:SerializedName("last_name")
    val lastName: String?,
    @field:SerializedName("email")
    val email: String?,
    @field:SerializedName("mobile")
    val mobile: String?,
    @field:SerializedName("gender")
    val gender: String?,
)

data class VerifyOTPResponseData(
    @field:SerializedName("token")
    val token: String?,
    @field:SerializedName("user")
    val userData: VerifiedUserData?,
)

data class VerifyOTPResponse(
    @field:SerializedName("status")
    val status: Boolean?,
    @field:SerializedName("message")
    val message: String?,
    @field:SerializedName("data")
    val data: VerifyOTPResponseData?,
)