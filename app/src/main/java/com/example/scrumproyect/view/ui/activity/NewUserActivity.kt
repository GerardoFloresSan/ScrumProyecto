package com.example.scrumproyect.view.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.example.scrumproyect.R
import com.example.scrumproyect.view.ui.extensions.showError
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_new_user.*

class NewUserActivity : AppCompatActivity() {

    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
    }


    private fun registrarUsuario() {

        if (TextUtils.isEmpty(emailNew.text)){
            emailNew.showError("Se debe ingresar un correo")
        }

        if (TextUtils.isEmpty(passwordNew.text)){
            passwordNew.showError("Falta ingresar la contraseña")
        }

        firebaseAuth!!.createUserWithEmailAndPassword(emailNew.text.toString(), passwordNew.text.toString()).addOnCanceledListener {

        }

    }

}
