package com.example.scrumproyect.view.ui.fragment

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.scrumproyect.R
import com.example.scrumproyect.view.ui.activity.MainActivity
import com.example.scrumproyect.view.ui.activity.NewUserActivity
import com.example.scrumproyect.view.ui.base.ScrumBaseFragment
import com.example.scrumproyect.view.ui.extensions.getString
import com.example.scrumproyect.view.ui.extensions.isEmpty
import com.example.scrumproyect.view.ui.extensions.showError
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.*
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.*

class LoginFragment : ScrumBaseFragment() {

    private var callbackManager: CallbackManager? = null

    @Suppress("PrivatePropertyName")
    private val RC_SIGN_IN: Int = 8767
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mGoogleSignInOptions: GoogleSignInOptions

    override fun getFragmentView() = R.layout.fragment_login

    override fun onCreate() {
        setTitle(getString(R.string.menu_login))
        callbackManager = CallbackManager.Factory.create()
        loginButton!!.setReadPermissions(Arrays.asList("email"))
        loginButton.fragment = this

        singInFaceBook()

        loginButtonFB.setOnClickListener {
            loginButton.performClick()
        }

        loginButtonEmail.setOnClickListener {
            loginUser()
        }

        newUser.setOnClickListener {
            startActivity(NewUserActivity::class.java)
        }

        configureGoogleSignIn()
        setupUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let { fireBaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                Toast.makeText(context, "Google sign in failed: onActivityResult ", Toast.LENGTH_LONG).show()
            }
        } else {
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun validateEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun loginUser() {
        hideAllWrappers()
        if (TextUtils.isEmpty(emailOld.text)){
            emailOld.showError("Se debe ingresar un correo")
        }

        if (TextUtils.isEmpty(passwordOld.text)){
            passwordOld.showError("Falta ingresar la contraseña")
        }

        if (emailOld.isEmpty() || passwordOld.isEmpty()) {
            return
        }

        if (!validateEmail(emailOld.getString())) {
            emailOld.showError("Correo inválido")
        }

        if (!validateEmail(emailOld.getString())) {
            return
        }
        (activity as MainActivity).login(emailOld.getString(), passwordOld.getString())
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(googleToken)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(context, mGoogleSignInOptions)
    }

    private fun setupUI() {
        google_button.setOnClickListener {
            signIn()
        }
        login_button_gmail.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun singInFaceBook() {
        callbackManager = CallbackManager.Factory.create()

        loginButton!!.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                (activity as MainActivity).login(loginResult.accessToken)
            }

            override fun onCancel() {
                Toast.makeText(context, R.string.cancel_login, Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                Log.d("facebook error", error.message)
                Toast.makeText(context, R.string.error_login, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fireBaseAuthWithGoogle(acct: GoogleSignInAccount) {
        (activity as MainActivity).login(acct)
    }
}
