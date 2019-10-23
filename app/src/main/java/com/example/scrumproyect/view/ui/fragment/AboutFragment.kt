package com.example.scrumproyect.view.ui.fragment

import android.content.Intent
import android.net.Uri
import com.example.scrumproyect.R
import com.example.scrumproyect.view.ui.base.ScrumBaseFragment
import com.example.scrumproyect.view.ui.utils.PapersManager
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : ScrumBaseFragment() {

    override fun getFragmentView() = R.layout.fragment_about

    override fun onCreate() {
        setTitle(getString(R.string.menu_about))

        /**
        detail.text = PapersManager.masters.about

        about_link_video.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(PapersManager.masters.link)
            startActivity(openURL)
        }

        */
    }
}
