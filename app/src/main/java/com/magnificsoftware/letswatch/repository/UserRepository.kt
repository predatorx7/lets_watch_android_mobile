package com.magnificsoftware.letswatch.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.magnificsoftware.letswatch.api.LetsWatchAsyncService
import com.magnificsoftware.letswatch.api.LetsWatchService
import com.magnificsoftware.letswatch.data.UserDao
import com.magnificsoftware.letswatch.data_class.plain.auth.signin.SigninRequest
import com.magnificsoftware.letswatch.data_class.plain.auth.signin.SigninResponse
import com.magnificsoftware.letswatch.data_class.plain.auth.signup.SignupRequest
import com.magnificsoftware.letswatch.data_class.plain.auth.signup.SignupResponse
import com.magnificsoftware.letswatch.data_class.plain.auth.userdata.UserProfileResponse
import com.magnificsoftware.letswatch.data_class.plain.auth.verify_otp.VerifyOTPRequest
import com.magnificsoftware.letswatch.data_class.plain.auth.verify_otp.VerifyOTPResponse
import com.magnificsoftware.letswatch.data_class.vo.User
import com.magnificsoftware.letswatch.util.ApiResponse
import com.magnificsoftware.letswatch.util.AppExecutors
import com.magnificsoftware.letswatch.util.Resource
import com.magnificsoftware.letswatch.util.async_future.FutureResource
import com.magnificsoftware.letswatch.util.network_livedata.NetworkBoundResource
import java.util.concurrent.Future
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles User objects.
 */
@Singleton
class UserRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val userDao: UserDao,
    private val letsWatchService: LetsWatchService,
    private val letsWatchAsyncService: LetsWatchAsyncService
) {
    fun signIn(email: String, password: String): Future<Resource<SigninResponse>> {
        val signInRequest = SigninRequest(email, password)
        return object : FutureResource<SigninResponse, User>(appExecutors) {
            override fun saveCallResult(item: SigninResponse) {
                val user = User.from(item)
                if (user.isValid) {
                    userDao.insert(user)
                }
            }

            override fun createCall(): Future<ApiResponse<SigninResponse>> {
                return letsWatchAsyncService.signIn(signInRequest)
            }
        }.asFuture()
    }

    fun signUp(signupRequest: SignupRequest): Future<Resource<SignupResponse>> {
        return object : FutureResource<SignupResponse, User>(appExecutors) {
            override fun saveCallResult(item: SignupResponse) {
                // Sign up will not save user information
            }

            override fun createCall(): Future<ApiResponse<SignupResponse>> {
                return letsWatchAsyncService.signUp(signupRequest)
            }
        }.asFuture()
    }

    fun verify(request: VerifyOTPRequest): Future<Resource<VerifyOTPResponse>> {
        return object : FutureResource<VerifyOTPResponse, User>(appExecutors) {
            override fun saveCallResult(item: VerifyOTPResponse) {
                val user = User.from(item)
                if (user.isValid) {
                    userDao.insert(user)
                }
            }

            override fun createCall(): Future<ApiResponse<VerifyOTPResponse>> {
                return letsWatchAsyncService.verifySignUp(request)
            }
        }.asFuture()
    }

    fun loadUserAsync(login: String): Future<Resource<UserProfileResponse>> {
        return object : FutureResource<UserProfileResponse, User>(appExecutors) {
            override fun saveCallResult(item: UserProfileResponse) {
                val user = User.from(login, item)
                if (user.isValid) {
                    userDao.insert(user)
                }
            }

            override fun createCall() = letsWatchAsyncService.getUser(login)
        }.asFuture()
    }

    fun loadUser(login: String): LiveData<Resource<UserProfileResponse>> {
        return object :
            NetworkBoundResource<UserProfileResponse, UserProfileResponse>(appExecutors) {
            override fun saveCallResult(item: UserProfileResponse) {
                val user = User.from(login, item)
                if (user.isValid) {
                    userDao.insert(user)
                }
            }

            override fun shouldFetch(data: UserProfileResponse?) = data == null

            override fun loadFromResource(): LiveData<UserProfileResponse> = MutableLiveData(null)

            override fun createCall() = letsWatchService.getUser(login)
        }.asLiveData()
    }

    fun getLatestUser(): Future<User?> {
        return appExecutors.futureDiskIO {
            userDao.getLatestUser()
        }
    }

    fun removeAll(): Future<Unit> {
        return appExecutors.futureDiskIO {
            userDao.removeAll()
        }
    }
}
