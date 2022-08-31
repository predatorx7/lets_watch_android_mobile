package com.magnificsoftware.letswatch.data_class.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.magnificsoftware.letswatch.data_class.plain.auth.signin.SigninResponse
import com.magnificsoftware.letswatch.data_class.plain.auth.signup.SignupRequest
import com.magnificsoftware.letswatch.data_class.plain.auth.userdata.SigninUserData
import com.magnificsoftware.letswatch.data_class.plain.auth.userdata.UserProfileResponse
import com.magnificsoftware.letswatch.data_class.plain.auth.verify_otp.VerifyOTPResponse
import java.util.*

@Entity
data class User(
    @field:SerializedName("login")
    val login: String,
    @field:SerializedName("avatar_url")
    val avatarUrl: String?,
    @field:SerializedName("user_firstname")
    val firstName: String?,
    @field:SerializedName("user_lastname")
    val lastName: String?,
    @PrimaryKey
    @field:SerializedName("user_id")
    val userId: String,
    @field:SerializedName("current_subscription_id")
    val subscriptionId: String?,
    @field:SerializedName("user_email")
    val email: String?,
    @field:SerializedName("user_gender")
    val gender: String?,
    @field:SerializedName("user_mobile")
    val mobileNumber: String?,
    @field:SerializedName("user_mobile_country_code")
    val mobileCountryCode: String?,
    @field:SerializedName("user_city")
    val city: String?,
    @field:SerializedName("user_country")
    val country: String?,
    @field:SerializedName("creation_time")
    val creationDateTime: Long = Calendar.getInstance().timeInMillis
) {
    companion object {
        fun from(response: SigninResponse): User {
            val loginToken: String = response.data?.token ?: ""
            val data: SigninUserData? = response.data?.userData

            return User(
                login = loginToken,
                avatarUrl = null,
                firstName = data?.userFirstName,
                lastName = data?.userLastName,
                userId = data?.userId.toString(),
                subscriptionId = data?.currentSubscriptionId,
                email = data?.userEmail,
                gender = data?.userGender,
                mobileNumber = data?.userMobile,
                mobileCountryCode = data?.userMobileCountryCode,
                city = data?.userCity,
                country = data?.userCountry,
            )
        }

        fun from(data: SignupRequest): User {
            return User(
                login = "",
                avatarUrl = null,
                firstName = data.firstName,
                lastName = data.lastName,
                userId = "",
                subscriptionId = null,
                email = data.email,
                gender = null,
                mobileNumber = data.phoneNumber,
                mobileCountryCode = null,
                city = null,
                country = null,
            )
        }

        fun from(response: VerifyOTPResponse): User {
            val loginToken = response.data?.token ?: ""
            val data = response.data?.userData
            return User(
                login = loginToken,
                avatarUrl = null,
                firstName = data?.firstName,
                lastName = data?.lastName,
                userId = data?.userId ?: "",
                subscriptionId = null,
                email = data?.email,
                gender = data?.gender,
                mobileNumber = data?.mobile,
                mobileCountryCode = null,
                city = null,
                country = null,
            )
        }

        fun from(loginToken: String, response: UserProfileResponse): User {
            val data = response.data
            return User(
                login = loginToken,
                avatarUrl = null,
                firstName = data.firstName,
                lastName = data.lastName,
                userId = data.userId ?: "",
                subscriptionId = null,
                email = data.email,
                gender = data.gender,
                mobileNumber = data.mobile,
                mobileCountryCode = null,
                city = data.city,
                country = null,
            )
        }
    }

    val isValid: Boolean get() = login.isNotBlank() && userId.isNotBlank()
}
