package com.example.scrumproyect

import android.annotation.SuppressLint
import android.os.Bundle
import org.json.JSONException
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.Toast
import android.content.Intent
import androidx.annotation.Nullable
import com.example.scrumproyect.view.ui.base.BaseActivity
import com.facebook.*
import java.util.*
import com.facebook.CallbackManager

class LoginActivity : BaseActivity() {

    private var callbackManager: CallbackManager? = null

    override fun getView() = R.layout.activity_login

    override fun onCreate() {
        super.onCreate()
        callbackManager = CallbackManager.Factory.create()
        login_button.setReadPermissions(Arrays.asList("email","public_profile"))
        checkLoginStatus()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


    var tokenTracker: AccessTokenTracker = object : AccessTokenTracker() {
        override fun onCurrentAccessTokenChanged(oldAccessToken: AccessToken, currentAccessToken: AccessToken?) {
            if (currentAccessToken == null) {
                profile_name.setText("")
                profile_email.setText("")
                profile_pic.setImageResource(0)
                Toast.makeText(this@LoginActivity, "User Logged out", Toast.LENGTH_LONG).show()
            } else
                loadUserProfile(currentAccessToken)
        }
    }

    @SuppressLint("SetTextI18n", "CheckResult")
    private fun loadUserProfile(newAccessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(
            newAccessToken
        ) { `object`, response ->
            try {
                val first_name = `object`.getString("first_name")
                val last_name = `object`.getString("last_name")
                val email = `object`.getString("email")
                val id = `object`.getString("id")
                val image_url = "https://graph.facebook.com/$id/picture?type=normal"

                profile_email.text = email
                profile_name.text = "$first_name $last_name"
                val requestOptions = RequestOptions()
                requestOptions.dontAnimate()

                Glide.with(this@LoginActivity).load(image_url).into(profile_pic)


            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "first_name,last_name,email,id")
        request.parameters = parameters
        request.executeAsync()

    }

    private fun checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            loadUserProfile(AccessToken.getCurrentAccessToken())
        }
    }
}
