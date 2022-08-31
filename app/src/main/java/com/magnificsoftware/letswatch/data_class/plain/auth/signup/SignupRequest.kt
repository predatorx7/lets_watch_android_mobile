package com.magnificsoftware.letswatch.data_class.plain.auth.signup

import com.google.gson.annotations.SerializedName

data class SignupRequest(
    @field:SerializedName("country_id")
    val countryId: Int?,
    @field:SerializedName("email")
    val email: String?,
    @field:SerializedName("first_name")
    val firstName: String?,
    @field:SerializedName("last_name")
    val lastName: String?,
    @field:SerializedName("password")
    val password: String?,
    @field:SerializedName("phone_no")
    val phoneNumber: String?,
)