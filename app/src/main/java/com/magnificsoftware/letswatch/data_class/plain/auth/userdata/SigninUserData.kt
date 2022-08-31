package com.magnificsoftware.letswatch.data_class.plain.auth.userdata

import com.google.gson.annotations.SerializedName

data class SigninUserData(
    @field:SerializedName("user_id")
    val userId: Int?,
    @field:SerializedName("user_email")
    val userEmail: String?,
    @field:SerializedName("user_firstname")
    val userFirstName: String?,
    @field:SerializedName("user_lastname")
    val userLastName: String?,
    @field:SerializedName("user_gender")
    val userGender: String?,
    @field:SerializedName("user_mobile")
    val userMobile: String?,
    @field:SerializedName("user_city")
    val userCity: String?,
    @field:SerializedName("user_country")
    val userCountry: String?,
    @field:SerializedName("user_mobile_country_code")
    val userMobileCountryCode: String?,
    @field:SerializedName("user_mobile_verified")
    val userMobileVerified: String?,
    @field:SerializedName("user_email_verified")
    val userEmailVerified: String?,
    @field:SerializedName("current_subscription_id")
    val currentSubscriptionId: String?,
)
