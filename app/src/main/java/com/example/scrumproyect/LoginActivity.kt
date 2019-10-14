package com.example.scrumproyect

import android.annotation.SuppressLint
import android.os.Bundle
import org.json.JSONException
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.Toast
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.Nullable
import com.example.scrumproyect.view.presenter.UserPresenter
import com.example.scrumproyect.view.ui.extensions.startActivity
import com.example.scrumproyect.view.ui.base.ScrumBaseActivity
import com.facebook.*
import java.util.*
import com.facebook.CallbackManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import io.paperdb.Paper
import java.io.Serializable

class LoginActivity : ScrumBaseActivity(), UserPresenter.View {

    override fun successSchedule(flag: Int, vararg args: Serializable) {
        startActivity(MainActivity::class.java)
    }

    private val presenter = UserPresenter()
    private var callbackManager: CallbackManager? = null
    private var loginButton: LoginButton? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseAuthListener: FirebaseAuth.AuthStateListener? = null

    private var progressBar: ProgressBar? = null

    override fun getView() = R.layout.activity_login

    override fun onCreate() {
        super.onCreate()
        callbackManager = CallbackManager.Factory.create()
        loginButton!!.setReadPermissions(Arrays.asList("email"))
        login_test.setOnClickListener {
            presenter.login()
        }

        loginButton!!.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Toast.makeText(applicationContext, R.string.cancel_login, Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
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

    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }


    private fun handleFacebookAccessToken(accessToken: AccessToken) {
        progressBar!!.visibility = View.VISIBLE
        loginButton!!.visibility = View.GONE

        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        firebaseAuth!!.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (!task.isSuccessful) {
                Toast.makeText(applicationContext, R.string.firebase_error_login, Toast.LENGTH_LONG).show()
            }
            progressBar!!.visibility = View.GONE
            loginButton!!.visibility = View.VISIBLE
        }
    }

    private fun goMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth!!.addAuthStateListener(firebaseAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth!!.removeAuthStateListener(firebaseAuthListener!!)
    }

}
