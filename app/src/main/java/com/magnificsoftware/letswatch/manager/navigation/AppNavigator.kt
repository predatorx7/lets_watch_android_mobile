package com.magnificsoftware.letswatch.manager.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment


/**
 * Interfaces that defines an app navigator.
 *
 * Provided below a host (typically an `Activity`} that can display fragments and knows how to respond to
 * navigation events.
 */
interface AppNavigator {

    /**
     * Trigger a navigation to the specified fragment, optionally adding a transaction to the back
     * stack to make this navigation reversible.
     *
     * Navigator implementation used to Navigate to the given fragment.
     *
     * @param fragment       Fragment to navigate to.
     * @param addToBackstack Whether or not the current fragment should be added to the backstack.
     */
    fun navigateTo(fragment: Fragment, addToBackstack: Boolean, replacementOfFragment: Int)

    fun openActivity(activityClass: Class<*>)

    fun openActivity(activityClass: Class<*>, options: Bundle)

    /**
     * Use like `intent.putExtra("value", options)` to provide arguments for [activityClass]
     * */
    fun openActivity(activityClass: Class<*>, argument: AppAcitivityArgumentsProvider)

    fun openActivity(activityClass: Class<*>, argument: Parcelable)

    fun close(activity: android.app.Activity)

    fun getOptions(activity: android.app.Activity): Bundle?

    fun getParcel(activity: android.app.Activity, key: String): Parcelable?
}
