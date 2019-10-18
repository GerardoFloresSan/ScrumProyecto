package com.example.scrumproyect.view.ui.fragment

import com.example.scrumproyect.R
import com.example.scrumproyect.view.ui.base.ScrumBaseFragment

class TermsFragment : ScrumBaseFragment() {

    override fun getFragmentView() = R.layout.fragment_terms

    override fun onCreate() {
        setTitle(getString(R.string.menu_terms))
    }
}
