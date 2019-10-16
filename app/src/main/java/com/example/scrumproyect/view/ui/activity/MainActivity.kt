package com.example.scrumproyect.view.ui.activity

import android.content.Intent
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scrumproyect.R
import com.example.scrumproyect.data.entity.ProductEntity
import com.example.scrumproyect.view.presenter.ProductPresenter
import com.example.scrumproyect.view.ui.adapter.ProductAdapter
import com.example.scrumproyect.view.ui.base.ScrumBaseActivity
import com.example.scrumproyect.view.ui.extensions.startActivity
import com.example.scrumproyect.view.ui.fragment.HomeFragment
import com.example.scrumproyect.view.ui.fragment.LoginFragment
import com.example.scrumproyect.view.ui.fragment.ProfileFragment
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable


class MainActivity : ScrumBaseActivity()/*, ProductPresenter.View */{
    private var current = 0
    private var fragments = ArrayList<Fragment>()

    /*private val presenter = ProductPresenter()*/

    /*@Inject
    lateinit var presenter: ProductRxPresenter*/

    override fun getView() = R.layout.activity_main

    override fun onCreate() {
        super.onCreate()

        /*component.inject(this)*/
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
                    R.id.nav_share -> current = 1
                    R.id.nav_send -> current = 2
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
        /*presenter.attachView(this)
        presenter.syncProduct()
        fab.setOnClickListener {
            startActivity(AddProductActivity::class.java)
        }
        logout.setOnClickListener {
            logoutSesion()
        }*/
    }

    override fun onPause() {
        super.onPause()
        /*presenter.detachView()*/
    }

    /*@Suppress("UNCHECKED_CAST")
    override fun successSchedule(flag: Int, vararg args: Serializable) {
        if (flag == 0) {
            val list = args[0] as ArrayList<ProductEntity>
            recycler.layoutManager = GridLayoutManager(this, 1) as RecyclerView.LayoutManager?
            recycler.adapter = ProductAdapter(list as List<ProductEntity>) {
                startActivity(DetailProductActivity::class.java, it)
            }
        }
    }

    private fun goLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun logoutSesion() {
        FirebaseAuth.getInstance().signOut()
        LoginManager.getInstance().logOut()
        goLoginScreen()
    }*/
}
