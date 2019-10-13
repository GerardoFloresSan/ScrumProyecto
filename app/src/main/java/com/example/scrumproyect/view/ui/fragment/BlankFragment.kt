package com.example.scrumproyect.view.ui.fragment

import com.example.scrumproyect.R
import com.example.scrumproyect.view.ui.base.BaseFragment

class BlankFragment : BaseFragment() {

    override fun getFragmentView() = R.layout.fragment_blank

    override fun onCreate() {
        setTitle("Example")
    }
}