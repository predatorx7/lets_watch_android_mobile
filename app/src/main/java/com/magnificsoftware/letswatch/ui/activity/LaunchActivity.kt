package com.magnificsoftware.letswatch.ui.activity

import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import com.magnificsoftware.letswatch.manager.authentication.AppAuthentication
import javax.inject.Inject

@AndroidEntryPoint
class LaunchActivity : AppCompatActivity() {

    private val splashDelay = 1800

    private val mHandler = Handler(Looper.getMainLooper())

    private val mLauncher: Runnable = Runnable {
        launch()
    }

    override fun onStart() {
        super.onStart()
        mHandler.postDelayed(mLauncher, splashDelay.toLong())
    }

    override fun onStop() {
        mHandler.removeCallbacks(mLauncher)
        super.onStop()
    }

    @Inject
    lateinit var authentication: AppAuthentication

    private fun launch() {
        authentication.start(this)
    }
}