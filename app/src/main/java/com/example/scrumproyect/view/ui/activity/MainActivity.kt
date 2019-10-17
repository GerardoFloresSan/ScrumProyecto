package com.example.scrumproyect.view.ui.activity

import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.scrumproyect.R
import com.example.scrumproyect.view.presenter.UserPresenter
import com.example.scrumproyect.view.ui.base.ScrumBaseActivity
import com.example.scrumproyect.view.ui.fragment.HomeFragment
import com.example.scrumproyect.view.ui.fragment.LoginFragment
import com.example.scrumproyect.view.ui.fragment.ProfileFragment
import com.example.scrumproyect.view.ui.utils.Methods
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import java.io.Serializable


class MainActivity : ScrumBaseActivity(), UserPresenter.View{

    private var current = 0
    private var fragments = ArrayList<Fragment>()
    private val presenterUser = UserPresenter()

    override fun getView() = R.layout.activity_main

    override fun onCreate() {
        super.onCreate()

        setSupportActionBar("Productos")
        setupDrawer()
        fragments.add(HomeFragment())
        fragments.add(LoginFragment())
        fragments.add(ProfileFragment())

        setupDrawerContent(object : SimpleNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem) {
                when (item.itemId) {
                    R.id.nav_home -> current = 0
                    R.id.nav_gallery -> current = 1
                    R.id.nav_slideshow -> current = 2
                    R.id.nav_tools -> current = 0
                    R.id.nav_share -> Methods.isInternetConnected()
                    R.id.nav_send -> logout()
                }
                replaceFragment(fragments[current])
            }
        })

        navigationView?.menu?.apply {
            findItem(R.id.nav_tools)
        }

        replaceFragment(fragments[current])
    }

    override fun onResume() {
        super.onResume()
        presenterUser.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        presenterUser.detachView()
    }

    fun _login(emailN : String, passwordN : String) {
        presenterUser.login(emailN, passwordN)
    }

    fun _login(accessToken: AccessToken) {
        presenterUser.loginFaceBook(accessToken)
    }

    fun _login(acct: GoogleSignInAccount) {
        presenterUser.loginGoogle(acct)
    }

    fun logout() {
        presenterUser.logout(googleToken)
    }

    override fun successUser(flag: Int, vararg args: Serializable) {
        when(flag) {
            0, 1, 2 -> {
                replaceFragment(fragments[1])
            }
            3, 4, 5 -> {
                replaceFragment(fragments[2])
            }
            else -> {

            }
        }
    }
}
