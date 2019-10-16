package com.example.scrumproyect.view.ui.fragment

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Switch
import android.widget.Toast
import com.example.scrumproyect.R
import com.example.scrumproyect.view.presenter.UserPresenter
import com.example.scrumproyect.view.ui.activity.MainActivity
import com.example.scrumproyect.view.ui.activity.NewUserActivity
import com.example.scrumproyect.view.ui.base.ScrumBaseFragment
import com.example.scrumproyect.view.ui.extensions.getString
import com.example.scrumproyect.view.ui.extensions.isEmpty
import com.example.scrumproyect.view.ui.extensions.showError
import com.example.scrumproyect.view.ui.utils.FacebookHelper
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*
import java.io.Serializable
import java.util.*

class LoginFragment : ScrumBaseFragment(), UserPresenter.View {

    private val presenter = UserPresenter()
    private var callbackManager: CallbackManager? = null

    @Suppress("PrivatePropertyName")
    private val RC_SIGN_IN: Int = 8767
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    override fun getFragmentView() = R.layout.fragment_login

    override fun onCreate() {
        setTitle("Login")
        callbackManager = CallbackManager.Factory.create()
        loginButton!!.setReadPermissions(Arrays.asList("email"))
        loginButton.fragment = this

        FacebookHelper.init(context)
        singInFacebook()
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

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
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

        presenter.login(emailOld.getString(), passwordOld.getString())
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
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

    fun singInFacebook() {
        callbackManager = CallbackManager.Factory.create()

        loginButton!!.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                presenter.loginFaceBook(loginResult.accessToken)
                /*handleFacebookAccessToken(loginResult.accessToken)*/
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
        presenter.loginGoogle(acct)
    }

    override fun successSchedule(flag: Int, vararg args: Serializable) {
        val msg : String = when(flag) {
            0 -> "Login normal"
            1 -> "Facebook"
            2 -> "Google"
            else -> "error"
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}
