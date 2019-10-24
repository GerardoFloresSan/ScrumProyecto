package com.example.scrumproyect.view.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.scrumproyect.R
import com.example.scrumproyect.view.ui.application.ScrumApplication
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment

class YoutubeViewerActivity : AppCompatActivity(), YouTubePlayer.OnInitializedListener {

    private lateinit var id: String

    private lateinit var player: YouTubePlayer

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_viewer)
        ScrumApplication.addActivity(this)

        id = intent.getStringExtra("extra0")

        val fragment = supportFragmentManager.findFragmentById(R.id.youtube) as YouTubePlayerSupportFragment
        fragment.initialize("AIzaSyCmOrjDGuN9PDyT8PSJOEVjjZEEOpE5jdo", this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ScrumApplication.removeActivity(this)
    }


    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
        player!!.setPlayerStateChangeListener(playerStateChangeListener)
        player.setPlaybackEventListener(playbackEventListener)

        if (!wasRestored) {
            player.cueVideo(id)
        }

        this.player = player

    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {

    }

    private val playbackEventListener = object : YouTubePlayer.PlaybackEventListener {

        override fun onBuffering(arg0: Boolean) {}

        override fun onPaused() {}

        override fun onPlaying() {}

        override fun onSeekTo(arg0: Int) {}

        override fun onStopped() {}

    }

    @Suppress("SENSELESS_COMPARISON")
    private val playerStateChangeListener = object : YouTubePlayer.PlayerStateChangeListener {

        override fun onAdStarted() {}

        override fun onError(arg0: YouTubePlayer.ErrorReason) {}

        override fun onLoaded(arg0: String) {
            if (player != null) {
                player.play()
            }
        }

        override fun onLoading() {}

        override fun onVideoEnded() {
            /*AmaApplication.closeAll()
            startActivity(Intent(this@YouTubeActivity, EndActivity::class.java))*/
            this@YoutubeViewerActivity.finish()
        }

        override fun onVideoStarted() {}
    }
}
