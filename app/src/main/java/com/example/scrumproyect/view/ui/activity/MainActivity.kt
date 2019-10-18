package com.example.scrumproyect.view.ui.activity

import android.os.Handler
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.scrumproyect.R
import com.example.scrumproyect.view.presenter.MasterPresenter
import com.example.scrumproyect.view.presenter.UserPresenter
import com.example.scrumproyect.view.ui.base.ScrumBaseActivity
import com.example.scrumproyect.view.ui.fragment.*
import com.example.scrumproyect.view.ui.utils.PapersManager
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import java.io.Serializable


class MainActivity : ScrumBaseActivity(), UserPresenter.View, MasterPresenter.View{

    private var current = 0
    private var fragments = ArrayList<Fragment>()
    private val presenterUser = UserPresenter()
    private val presenterMaster = MasterPresenter()

    override fun getView() = R.layout.activity_main

    override fun onCreate() {
        super.onCreate()

        setSupportActionBar("Productos")
        setupDrawer()
        fragments.add(HomeFragment())

        fragments.add(LoginFragment())
        fragments.add(ProfileFragment())

        fragments.add(MeBookFragment())
        fragments.add(AboutFragment())
        fragments.add(TermsFragment())


        setupDrawerContent(object : SimpleNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem) {
                when (item.itemId) {
                    R.id.nav_home -> current = 0
                    R.id.nav_user -> current = if(PapersManager.session) 2 else 1
                    R.id.nav_me -> current = 3
                    R.id.nav_about -> current = 4
                    R.id.nav_term -> current = 5
                    R.id.nav_close -> logout()
                }
                replaceFragment(fragments[current])
            }
        })

        configurationNavigation()

        replaceFragment(fragments[current])
        presenterMaster.attachView(this)
        presenterMaster.syncMaster()
    }

    override fun onResume() {
        super.onResume()
        presenterUser.attachView(this)
        presenterMaster.attachView(this)
        if (PapersManager.masters.forbiddenWords.isEmpty()) presenterMaster.syncMaster()
    }

    private fun configurationNavigation() {
        navigationView?.menu?.apply {
            findItem(R.id.nav_user).title = getString(if (PapersManager.session) R.string.menu_profile else R.string.menu_login)
            findItem(R.id.nav_close).isVisible = PapersManager.session
        }
    }

    override fun onPause() {
        super.onPause()
        presenterUser.detachView()
        presenterMaster.detachView()
    }

    fun login(emailN : String, passwordN : String) = presenterUser.login(emailN, passwordN)

    fun login(accessToken: AccessToken) = presenterUser.loginFaceBook(accessToken)

    fun login(acct: GoogleSignInAccount) = presenterUser.loginGoogle(acct)

    fun logout() = presenterUser.logout(googleToken)

    override fun successUser(flag: Int, vararg args: Serializable) {
        when(flag) {
            0, 1, 2 -> {
                configurationNavigation()
                replaceFragment(fragments[2])
            }
            3, 4, 5 -> {
                configurationNavigation()
                replaceFragment(fragments[1])
            }
        }
    }
    override fun successMasters(flag: Int, vararg args: Serializable) {
        if (flag == 1) Handler().postDelayed({ presenterMaster.syncMaster() }, 2000)
    }
}
