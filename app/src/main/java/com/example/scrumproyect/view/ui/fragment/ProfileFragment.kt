package com.example.scrumproyect.view.ui.fragment

import com.bumptech.glide.Glide
import com.example.scrumproyect.R
import com.example.scrumproyect.view.ui.base.ScrumBaseFragment
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

    fun getData() {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            if (name!!.isEmpty()){
                nameTextView.text = "Prueba" //COMO SE LEE EL PAPER PARA OBTENER EL NOMBRE QUE SE GRABO
            }else{
                nameTextView.text = name
            }

            emailTextView.text = email
            Glide.with(context).load(photoUrl).error(R.drawable.ic_person_black_36dp).into(profile_image)


        } else {

            //TODO
        }
    }
}
