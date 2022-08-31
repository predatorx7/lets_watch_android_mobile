package com.magnificsoftware.letswatch.manager.navigation

import android.os.Bundle
import android.os.Parcelable
import com.magnificsoftware.letswatch.ui.activity.AppVideoPlayerActivity
import com.magnificsoftware.letswatch.ui.activity.DetailsActivity
import javax.inject.Inject


class LetsWatchNavigator @Inject constructor(val appNavigator: AppNavigator) {

    /**
     * Open brightcove video player
     */
    fun openVideoPlayer(videoSourceUri: String) {
        val options = Bundle()
        options.putString("video_src_uri", videoSourceUri)

        appNavigator.openActivity(AppVideoPlayerActivity::class.java, options)
    }

    /**
     * Open details page
     */
    fun openDetailsPage(argument: Parcelable) {
        appNavigator.openActivity(DetailsActivity::class.java, argument)
    }
}