package com.magnificsoftware.letswatch.manager.navigation

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.magnificsoftware.letswatch.commons.AppIntentNames
import javax.inject.Inject


/**
 * Navigator implementation used to Navigate to the given fragment.
 *
 * @param hostFragment       Fragment to navigate to.
 */
class AppNavigatorImpl @Inject constructor(val hostFragment: FragmentActivity) : AppNavigator {
    override fun navigateTo(
        fragment: Fragment,
        addToBackstack: Boolean,
        replacementOfFragment: Int
    ) {
        var transaction = this.hostFragment.supportFragmentManager.beginTransaction()
            .replace(replacementOfFragment, fragment)

        if (addToBackstack) {
            transaction = transaction.addToBackStack(fragment::class.java.canonicalName)
        }

        transaction.commit()
    }

    override fun openActivity(activityClass: Class<*>) {
        val intent = Intent(hostFragment.applicationContext, activityClass)
        hostFragment.startActivity(intent)
    }

    override fun openActivity(activityClass: Class<*>, options: Bundle) {
        val intent = Intent(hostFragment.applicationContext, activityClass)
        intent.putExtra(AppIntentNames.ACTIVITY_BUNDLE_PARAM.name, options)
        hostFragment.startActivity(intent)
    }

    override fun openActivity(activityClass: Class<*>, argument: AppAcitivityArgumentsProvider) {
        val intent = Intent(hostFragment.applicationContext, activityClass)
        argument.addArguments(intent)
        hostFragment.startActivity(intent)
    }

    override fun openActivity(activityClass: Class<*>, argument: Parcelable) {
        val intent = Intent(hostFragment.applicationContext, activityClass)
        intent.putExtra(AppIntentNames.ACTIVITY_PARAMS.name, argument)
        hostFragment.startActivity(intent)
    }

    override fun close(activity: android.app.Activity) {
        activity.finish()
    }

    override fun getOptions(activity: android.app.Activity): Bundle? {
        return activity.intent.extras?.getBundle(AppIntentNames.ACTIVITY_BUNDLE_PARAM.name)
    }

    override fun getParcel(activity: android.app.Activity, key: String): Parcelable? {
        return activity.intent.getParcelableExtra(key)
    }
}
