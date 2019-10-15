package com.example.scrumproyect

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.scrumproyect.view.presenter.UserPresenter
import com.example.scrumproyect.view.ui.base.ScrumBaseActivity
import com.example.scrumproyect.view.ui.extensions.startActivity
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
import kotlinx.android.synthetic.main.activity_login.*
import java.io.Serializable
import java.util.*

class LoginActivity : ScrumBaseActivity(), UserPresenter.View {

    private val presenter = UserPresenter()
    private var callbackManager: CallbackManager? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseAuthListener: FirebaseAuth.AuthStateListener? = null
    private val googleApiClient: GoogleApiClient? = null

    val RC_SIGN_IN: Int = 8767
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions


    override fun getView() = R.layout.activity_login

    override fun onCreate() {
        super.onCreate()
        callbackManager = CallbackManager.Factory.create()
        loginButton!!.setReadPermissions(Arrays.asList("email"))
        login_test.setOnClickListener {
            presenter.login()
        }

        FacebookHelper.init(this)
        loginButton!!.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                presenter.loginFaceBook(loginResult.accessToken)
                /*handleFacebookAccessToken(loginResult.accessToken)*/
            }

            override fun onCancel() {
                Toast.makeText(applicationContext, R.string.cancel_login, Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                Log.d("facebook error", error.message)
                Toast.makeText(applicationContext, R.string.error_login, Toast.LENGTH_SHORT).show()
            }
        })

        firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuthListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if (user != null) {
                goMainScreen()
            }
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

    private fun goMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: onActivityResult ", Toast.LENGTH_LONG).show()
            }
        } else {
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }

    }

    override fun onStart() {
        super.onStart()
        firebaseAuth!!.addAuthStateListener(firebaseAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth!!.removeAuthStateListener(firebaseAuthListener!!)
    }

    override fun successSchedule(flag: Int, vararg args: Serializable) {
        startActivity(MainActivity::class.java)
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    private fun setupUI() {
        google_button.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth!!.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                startActivity(MainActivity::class.java)
            } else {
                Toast.makeText(this, "Google sign in failed firebaseAuthWithGoogle", Toast.LENGTH_LONG).show()
            }
        }
    }


}
