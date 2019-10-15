package com.example.scrumproyect.view.ui.base

import android.content.Intent
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.lang.RuntimeException

abstract class FacebookBaseActivity: ScrumBaseActivity() {

    private lateinit var callbackManager: CallbackManager

    @Suppress("DEPRECATION")
    protected fun initFacebookView(view: View) {
        FacebookSdk.setApplicationId("540850742950843")
        FacebookSdk.sdkInitialize(this)
        callbackManager = CallbackManager.Factory.create()
        FacebookPresenter.setCallback(callbackManager, view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (callbackManager.onActivityResult(requestCode, resultCode, data)) return
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))
    }

    class FacebookPresenter {

        companion object {
            fun setCallback(callbackManager: CallbackManager, view: View) {

                LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    override fun onError(error: FacebookException?) {
                        if (error != null) {
                            view.onFailedFacebookLogin(error)
                        }
                        else {
                            view.onFailedFacebookLogin(RuntimeException("Empty Error"))
                        }
                    }

                    override fun onSuccess(result: LoginResult?) {
                        if (result != null) {
                            view.onSuccessFacebookLogin(result)
                        }
                        else {
                            view.onFailedFacebookLogin(RuntimeException("Empty Result"))
                        }
                    }

                    override fun onCancel() {
                        AccessToken.setCurrentAccessToken(null)
                        /*view.onCancelFacebookLogin(RuntimeException("Cancel Task"))*/
                    }
                })
            }
        }
    }

    interface View {
        fun onSuccessFacebookLogin(loginResult: LoginResult)

        fun onFailedFacebookLogin(exception: RuntimeException)
    }

}