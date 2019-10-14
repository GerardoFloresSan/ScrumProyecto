package com.example.scrumproyect.view.ui.activity

import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.scrumproyect.LoginActivity
import com.example.scrumproyect.R
import com.example.scrumproyect.view.ui.application.ScrumActivity
import com.example.scrumproyect.view.ui.base.BaseActivity
import com.example.scrumproyect.view.ui.base.ScrumBaseActivity
import com.example.scrumproyect.view.ui.fragment.BlankFragment
import com.example.scrumproyect.view.ui.fragment.BlankFragment2
import kotlinx.android.synthetic.main.app_bar_main2.*
import com.example.scrumproyect.view.ui.extensions.startActivity

class Main2Activity : ScrumBaseActivity() {
    private var current = 0
    private var fragments = ArrayList<Fragment>()
    override fun getView() = R.layout.activity_main2

    override fun onCreate() {
        super.onCreate()
        setSupportActionBar("pruebas 2")
        setupDrawer()
        fragments.add(BlankFragment())
        fragments.add(BlankFragment2())

        setupDrawerContent(object : SimpleNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem) {
                when (item.itemId) {
                    R.id.nav_home -> current = 0
                    R.id.nav_gallery -> current = 1
                    R.id.nav_slideshow -> current = 0
                    R.id.nav_tools -> current = 1
                    R.id.nav_share -> current = 0
                    R.id.nav_send -> current = 1
                }
                replaceFragment(fragments[current])
            }
        })

        navigationView?.menu?.apply {
            findItem(R.id.nav_tools)
        }
        replaceFragment(fragments[current])

        fab.setOnClickListener {
            ScrumActivity.closeAll()
            startActivity(LoginActivity::class.java)
        }
    }
}
