package com.magnificsoftware.letswatch.data_class.plain.auth.signin

import com.google.gson.annotations.SerializedName
import com.magnificsoftware.letswatch.data_class.plain.auth.userdata.SigninUserData

data class SigninResponseData(
    @field:SerializedName("token")
    val token: String?,
    @field:SerializedName("user")
    val userData: SigninUserData?,
)

data class SigninResponse(
    @field:SerializedName("status")
    val status: Boolean?,
    @field:SerializedName("message")
    val message: String?,
    @field:SerializedName("data")
    val data: SigninResponseData?,
)
