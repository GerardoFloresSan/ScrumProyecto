package com.example.scrumproyect.view.ui.fragment

import com.example.scrumproyect.R
import com.example.scrumproyect.view.ui.base.ScrumBaseFragment

class ProfileFragment : ScrumBaseFragment() {

    override fun getFragmentView() = R.layout.fragment_profile

    override fun onCreate() {
        setTitle("Perfil")
    }
}
