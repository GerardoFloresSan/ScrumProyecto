package com.example.scrumproyect.view.ui.fragment

import android.view.View
import com.example.scrumproyect.R
import com.example.scrumproyect.view.ui.activity.YoutubeViewerActivity
import com.example.scrumproyect.view.ui.base.ScrumBaseFragment
import com.example.scrumproyect.view.ui.utils.PapersManager
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : ScrumBaseFragment() {

    override fun getFragmentView() = R.layout.fragment_about

    override fun onCreate() {
        setTitle(getString(R.string.menu_about))

        about_link_video.visibility = if(PapersManager.masters.link.isEmpty()) View.GONE else View.VISIBLE

        about_link_video.setOnClickListener {
            /*val intent = YouTubeStandalonePlayer.createVideoIntent(activity, "AIzaSyCmOrjDGuN9PDyT8PSJOEVjjZEEOpE5jdo", "SYUKZqLPN88", 0, true, false)
            startActivity(intent)*/
            startActivity(YoutubeViewerActivity::class.java, PapersManager.masters.link)
        }
    }
}
