package com.magnificsoftware.letswatch.data_class.plain.auth.signup

import com.google.gson.annotations.SerializedName


data class SignupResponseData(
    @field:SerializedName("status")
    val status: Boolean?,
)

data class SignupResponse(
    @field:SerializedName("status")
    val status: Boolean?,
    @field:SerializedName("message")
    val message: String?,
    @field:SerializedName("data")
    val data: SignupResponseData?,
)