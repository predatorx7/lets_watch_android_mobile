package com.magnificsoftware.letswatch.data_class.plain.auth.verify_otp

import com.google.gson.annotations.SerializedName

data class VerifyOTPRequest(
    @field:SerializedName("email")
    val email: String?,
    @field:SerializedName("otp")
    val otp: String?,
)