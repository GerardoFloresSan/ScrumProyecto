package com.example.scrumproyect.view.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.scrumproyect.BuildConfig
import com.example.scrumproyect.LoginActivity
import com.example.scrumproyect.MainActivity
import com.example.scrumproyect.R
import com.example.scrumproyect.view.ui.extensions.startActivity
import io.paperdb.Paper

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (Paper.book(BuildConfig.FLAVOR).read("session", false)) {
            startActivity(MainActivity::class.java)
        } else {
            startActivity(LoginActivity::class.java)
        }
    }
}
