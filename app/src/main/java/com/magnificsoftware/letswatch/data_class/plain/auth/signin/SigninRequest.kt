package com.magnificsoftware.letswatch.data_class.plain.auth.signin

import com.google.gson.annotations.SerializedName

data class SigninRequest(
    @field:SerializedName("email")
    val email: String?,
    @field:SerializedName("password")
    val password: String?,
)
