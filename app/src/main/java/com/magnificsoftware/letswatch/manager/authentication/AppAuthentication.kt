package com.magnificsoftware.letswatch.manager.authentication

import androidx.fragment.app.FragmentActivity
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.data_class.plain.auth.signin.SigninResponse
import com.magnificsoftware.letswatch.data_class.plain.auth.signup.SignupRequest
import com.magnificsoftware.letswatch.data_class.plain.auth.signup.SignupResponse
import com.magnificsoftware.letswatch.data_class.plain.auth.verify_otp.VerifyOTPRequest
import com.magnificsoftware.letswatch.data_class.plain.auth.verify_otp.VerifyOTPResponse
import com.magnificsoftware.letswatch.data_class.vo.User
import com.magnificsoftware.letswatch.manager.navigation.LetsWatchNavigator
import com.magnificsoftware.letswatch.repository.UserRepository
import com.magnificsoftware.letswatch.ui.activity.LaunchActivity
import com.magnificsoftware.letswatch.ui.activity.MainActivity
import com.magnificsoftware.letswatch.ui.fragments.auth.AuthDetailsArgument
import com.magnificsoftware.letswatch.util.Resource
import timber.log.Timber
import javax.inject.Inject


class AppAuthentication @Inject constructor(
    val letsWatchNavigator: LetsWatchNavigator,
    val userRepository: UserRepository,
    val hostFragment: FragmentActivity,
) {
    companion object {
        private var state: AppAuthenticationState = AppAuthenticationState(null)
        private val user: User? get() = state.user
        private fun setUser(value: User?) {
            state = AppAuthenticationState(value)
        }

        val hasUser: Boolean get() = state.hasUser
        val userId: String get() = state.userId
        val email: String get() = state.userEmail
        val isAuthenticated get() = state.isAuthenticated
        val status get() = state.status
    }

    fun login(username: String?, password: String?): Resource<SigninResponse> {
        Timber.w("Attempting to login with username:${username} and password:${password}")

        if (username.isNullOrBlank() || password.isNullOrBlank()) {
            return Resource.success(
                SigninResponse(
                    false,
                    message = "Username or password must not be empty",
                    null
                )
            )
        }
        val response = userRepository.signIn(username, password).get()
        if (response.data?.data != null) {
            setUser(User.from(response.data))
            onAuthenticationChange()
        } else {
            val message = response.data?.message ?: response.message
            Timber.e(message)
        }
        return response
    }

    fun register(
        email: String?,
        firstName: String?,
        lastName: String?,
        password: String?,
        phoneNumber: String?,
    ): Resource<SignupResponse> {
        Timber.w("Attempting to register with username:${email} and password:${password}")
        val validationWarning: String = when {
            email.isNullOrBlank() -> {
                "Email cannot be empty"
            }
            firstName.isNullOrBlank() -> {
                "First name cannot be empty"
            }
            lastName.isNullOrBlank() -> {
                "Last name cannot be empty"
            }
            password.isNullOrBlank() -> {
                "Password cannot be empty"
            }
            phoneNumber.isNullOrBlank() -> {
                "Mobile number cannot be empty"
            }
            else -> {
                ""
            }
        }

        if (validationWarning.isNotBlank()) {
            return Resource.success(
                SignupResponse(
                    false,
                    validationWarning,
                    null
                )
            )
        }

        val signupRequest = SignupRequest(
            1,
            email,
            firstName,
            lastName,
            password,
            phoneNumber,
        )

        // For testing only >>>>>

//        setUser(User.from(signupRequest))
//
//        return Resource.success(
//            SignupResponse(
//                true,
//                validationWarning,
//                null,
//            )
//        )

        // <<<<<

        val response = userRepository.signUp(signupRequest).get()
        if (response.data?.data?.status == true) {
            // this is only used for verification [verify]
            setUser(User.from(signupRequest))
        } else {
            val message = response.data?.message ?: response.message
            Timber.e(message)
        }
        return response
    }

    fun verify(verificationCode: String?): Resource<VerifyOTPResponse> {
        Timber.w("Attempting to verify with code:${verificationCode}")

        val validationWarning: String = when {
            user?.email.isNullOrBlank() -> {
                "Email cannot be empty"
            }
            verificationCode.isNullOrBlank() -> {
                "verification code cannot be empty"
            }
            else -> {
                ""
            }
        }

        if (validationWarning.isNotBlank()) {
            return Resource.success(
                VerifyOTPResponse(
                    false,
                    validationWarning,
                    null
                )
            )
        }

        val verifyOTPRequest = VerifyOTPRequest(
            user?.email,
            verificationCode,
        )

        val response = userRepository.verify(verifyOTPRequest).get()

        if (response.data?.data != null) {
            setUser(User.from(response.data))
            onAuthenticationChange()
        } else {
            val message = response.data?.message ?: response.message
            Timber.e(message)
        }
        return response
    }

    fun start(activity: LaunchActivity) {
        if (activity.isFinishing) return
        setUser(userRepository.getLatestUser().get())
        openHomePage()
    }

    private fun onAuthenticationChange() {
        if (isAuthenticated) {
            letsWatchNavigator.appNavigator.openActivity(MainActivity::class.java)
        } else {
            letsWatchNavigator.openDetailsPage(AuthDetailsArgument(null))
        }
        hostFragment.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        letsWatchNavigator.appNavigator.close(hostFragment)
    }

    fun openHomePage() {
        letsWatchNavigator.appNavigator.openActivity(MainActivity::class.java)
        hostFragment.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        letsWatchNavigator.appNavigator.close(hostFragment)
    }

    fun openAuthPage(signup: Boolean = false) {
        letsWatchNavigator.openDetailsPage(AuthDetailsArgument(null, signup))
        hostFragment.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        letsWatchNavigator.appNavigator.close(hostFragment)
    }

    fun logout() {
        userRepository.removeAll()
        setUser(null)
        onAuthenticationChange()
    }

    private data class AppAuthenticationState(
        private var _user: User? = null,
    ) {
        val user get(): User? = _user

        val hasUser get() = user != null

        val userId: String get() = user?.userId ?: ""

        val userEmail: String get() = user?.email ?: ""

        val isAuthenticated get() = user?.isValid == true

        val hasSubscription get() = isAuthenticated && !user?.subscriptionId.isNullOrBlank()

        val status: AppAuthenticationStatus
            get() {
                return if (!isAuthenticated) {
                    AppAuthenticationStatus.Unauthenticated
                } else if (!hasSubscription) {
                    AppAuthenticationStatus.Authenticated
                } else {
                    AppAuthenticationStatus.Subscribed
                }
            }
    }
}