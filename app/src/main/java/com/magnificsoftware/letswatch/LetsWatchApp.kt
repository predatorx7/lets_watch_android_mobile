package com.magnificsoftware.letswatch

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class LetsWatchApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Note: ignore "Unresolved reference: BuildConfig" errors
        if (com.magnificsoftware.letswatch.BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
