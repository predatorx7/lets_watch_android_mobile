package com.magnificsoftware.letswatch.data_class.plain.auth.userdata

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @field:SerializedName("user_id")
    val userId: String?,
    @field:SerializedName("user_firstname")
    val firstName: String?,
    @field:SerializedName("user_lastname")
    val lastName: String?,
    @field:SerializedName("user_email")
    val email: String?,
    @field:SerializedName("user_mobile")
    val mobile: String?,
    @field:SerializedName("user_gender")
    val gender: String?,
    @field:SerializedName("user_city")
    val city: String?,
)

data class UserProfileResponse(
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("data")
    val data: UserProfile,
)
