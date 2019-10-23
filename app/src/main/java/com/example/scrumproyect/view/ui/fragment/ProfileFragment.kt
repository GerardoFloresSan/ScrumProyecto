package com.example.scrumproyect.view.ui.fragment

import com.bumptech.glide.Glide
import com.example.scrumproyect.R
import com.example.scrumproyect.data.entity.UserEntity
import com.example.scrumproyect.view.ui.base.ScrumBaseFragment
import com.example.scrumproyect.view.ui.utils.PapersManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : ScrumBaseFragment() {

    override fun getFragmentView() = R.layout.fragment_profile

    override fun onCreate() {
        setTitle(getString(R.string.menu_profile))
    }

    override fun onStart() {
        super.onStart()
        getData()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData() {
        val name = PapersManager.userEntity.name
        val email = PapersManager.userEntity.email
        val photoUrl = PapersManager.userEntity.urlUser
        nameTextView.text = name
        emailTextView.text = email
        Glide.with(context).load(photoUrl).error(R.drawable.ic_person_black_36dp).into(profile_image)
    }
}
