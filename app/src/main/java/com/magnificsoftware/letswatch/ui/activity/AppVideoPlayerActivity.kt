package com.magnificsoftware.letswatch.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import com.magnificsoftware.letswatch.databinding.ActivityAppvideoplayerBinding
import com.magnificsoftware.letswatch.manager.navigation.AppNavigator
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class AppVideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppvideoplayerBinding

    private var baseVideoView: PlayerView? = null

    private var player: SimpleExoPlayer? = null

    private var videoSourceUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // When extending the BrightcovePlayer, we must assign the brightcoveVideoView before
        // entering the superclass. This allows for some stock video player lifecycle
        // management.  Establish the video object and use it's event emitter to get important
        // notifications and to control logging.
        binding = ActivityAppvideoplayerBinding.inflate(layoutInflater)
        baseVideoView = binding.root
        setContentView(baseVideoView!!)


        super.onCreate(savedInstanceState)

        videoSourceUri = getVideoSourceUri()

        // Hide support action bar
        if (supportActionBar != null)
            supportActionBar?.hide()

        // Set UI view as secure
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }


    // private fun showDescription() {
    //    // TODO: THIS WILL NOT WORK
    //    val fragment = VideoDetailsFragment()
    //
    //    supportFragmentManager.beginTransaction()
    //        .replace(R.id.video_details_container, fragment)
    //        .commit()
    // }

    @Inject
    lateinit var appNavigator: AppNavigator

    private fun getCreationParams(): Bundle? {
        val option = appNavigator.getOptions(this)

        if (option == null) {
            Timber.e("Options are null")
        }
        return option
    }

    private fun getVideoSourceUri(): String {
        val options = getCreationParams()
        return options?.getString("video_src_uri") ?: ""
    }

    private fun initializePlayer() {
        if (player == null) {
            val trackSelector = DefaultTrackSelector(this)
            trackSelector.setParameters(
                trackSelector.buildUponParameters().setMaxVideoSizeSd()
            )
            player = SimpleExoPlayer.Builder(this)
                .setTrackSelector(trackSelector)
                .build()
            player!!.prepare()
        }
        baseVideoView!!.player = player
        val mediaItem: MediaItem = MediaItem.fromUri(videoSourceUri ?: "")
        player!!.setMediaItem(mediaItem)
        // TODO(mushaheedx): Create a data class for video description
        // showDescription()

        player!!.playWhenReady = playWhenReady
        player!!.seekTo(currentWindow, playbackPosition)
        player!!.prepare()
    }

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0


    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        baseVideoView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }


}
