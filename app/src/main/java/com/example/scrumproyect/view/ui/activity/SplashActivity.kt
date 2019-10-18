package com.example.scrumproyect.view.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.scrumproyect.R
import com.example.scrumproyect.view.ui.extensions.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startActivity(MainActivity::class.java)
    }
}
