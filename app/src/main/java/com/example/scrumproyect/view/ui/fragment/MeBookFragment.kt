package com.example.scrumproyect.view.ui.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.scrumproyect.R
import com.example.scrumproyect.view.ui.base.ScrumBaseFragment

class MeBookFragment : ScrumBaseFragment() {

    override fun getFragmentView() = R.layout.fragment_me_book

    override fun onCreate() {
        setTitle(getString(R.string.menu_me))
    }
}
