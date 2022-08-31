package com.magnificsoftware.letswatch.manager.navigation

import androidx.fragment.app.Fragment
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.commons.AppIntentNames
import com.magnificsoftware.letswatch.ui.fragments.auth.AuthDetailsArgument
import com.magnificsoftware.letswatch.ui.fragments.auth.LoginFragment
import com.magnificsoftware.letswatch.ui.fragments.auth.SignupFragment
import com.magnificsoftware.letswatch.ui.fragments.details.DetailsScreen
import com.magnificsoftware.letswatch.ui.fragments.details.show_details.ShowDetailsArgument
import com.magnificsoftware.letswatch.ui.fragments.details.show_details.ShowDetailsFragment
import timber.log.Timber
import javax.inject.Inject


class SubNavigation @Inject constructor(val appNavigator: AppNavigator) {
    /**
     * Change hostFragment fragment with the fragment represented by [screen].
     *
     * @param screen The Screen of type [Fragment] & [DetailsScreen]
     */
    fun <T> switchScreen(screen: T) where T : Fragment, T : DetailsScreen {
        appNavigator.navigateTo(screen, false, R.id.main_details_container)
    }

    /**
     * Auto change screen based on received intent's bundle parcelable extras with key [AppIntentNames.ACTIVITY_PARAMS.name]
     * */
    fun changeScreenFor(activity: android.app.Activity) {
        when (val params = appNavigator.getParcel(activity, AppIntentNames.ACTIVITY_PARAMS.name)) {
            null -> {
                Timber.e("Parameter obtained from the intent is null")
            }
            is ShowDetailsArgument -> {
                switchScreen(ShowDetailsFragment(params))
            }
            is AuthDetailsArgument -> {
                if (params.openSignupPage) {
                    switchScreen(SignupFragment(params))
                } else {
                    switchScreen(LoginFragment(params))
                }
            }
            else -> {
                Timber.e("Parameter ${params::class.simpleName} obtained from the intent is not known")
            }
        }
    }
}