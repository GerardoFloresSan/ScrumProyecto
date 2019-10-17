package com.example.scrumproyect.view.ui.activity

import android.text.TextUtils
import android.util.Patterns
import com.example.scrumproyect.R
import com.example.scrumproyect.view.presenter.UserPresenter
import com.example.scrumproyect.view.ui.application.ScrumApplication
import com.example.scrumproyect.view.ui.base.ScrumBaseActivity
import com.example.scrumproyect.view.ui.extensions.getString
import com.example.scrumproyect.view.ui.extensions.isEmpty
import com.example.scrumproyect.view.ui.extensions.showError
import com.example.scrumproyect.view.ui.extensions.startActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_new_user.*
import java.io.Serializable

class NewUserActivity : ScrumBaseActivity() , UserPresenter.View{

    private var firebaseAuth: FirebaseAuth? = null

    private val presenter = UserPresenter()

    override fun getView() = R.layout.activity_new_user

    override fun onCreate() {
        super.onCreate()
        register_user.setOnClickListener {
            registerUser()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }
    private fun validateEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun registerUser() {
        hideAllWrappers()
        if (TextUtils.isEmpty(emailNew.text)){
            emailNew.showError("Se debe ingresar un correo")
        }

        if (TextUtils.isEmpty(passwordNew.text)){
            passwordNew.showError("Falta ingresar la contraseña")
        }

        if (emailNew.isEmpty() || passwordNew.isEmpty()) {
            return
        }

        if (!validateEmail(emailNew.getString())) {
            emailNew.showError("Correo inválido")
        }

        if (!validateEmail(emailNew.getString())) {
            return
        }


        presenter.newUser(emailNew.getString(), passwordNew.getString())

    }
    override fun successUser(flag: Int, vararg args: Serializable) {
        ScrumApplication.closeAll()
        startActivity(MainActivity::class.java)
    }

}
