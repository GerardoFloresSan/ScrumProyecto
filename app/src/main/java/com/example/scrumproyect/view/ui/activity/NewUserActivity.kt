package com.example.scrumproyect.view.ui.activity

import android.text.TextUtils
import android.util.Patterns
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
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
        if (emailNew.isEmpty()){
            emailNew.showError("Se debe ingresar un correo")
        }

        if (passwordNew.isEmpty()){
            passwordNew.showError("Falta ingresar la contraseña")
        }

        if (nameNew.isEmpty()){
            nameNew.showError("Falta ingresar su nombre")
        }

        if (emailNew.isEmpty() || passwordNew.isEmpty() || nameNew.isEmpty()) {
            return
        }

        if (!validateEmail(emailNew.getString())) {
            emailNew.showError("Correo inválido")
        }

        if (!validateEmail(emailNew.getString())) {
            return
        }


        presenter.newUser(emailNew.getString(), passwordNew.getString(), nameNew.getString())

    }

    private fun dialogCreateUser() {
        MaterialDialog.Builder(this)
            .title("¡Felicitaciones!")
            .cancelable(false)
            .content("Tu registro fue exitoso")
            .neutralText("OK")
            .neutralColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .onPositive { _, _ ->
                run {
                    ScrumApplication.closeAll()
                    startActivity(MainActivity::class.java)
                }
            }
            .show()
    }

    override fun successUser(flag: Int, vararg args: Serializable) {
        dialogCreateUser()
    }

}
