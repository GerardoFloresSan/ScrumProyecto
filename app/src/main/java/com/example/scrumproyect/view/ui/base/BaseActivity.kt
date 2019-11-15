package com.example.scrumproyect.view.ui.base

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.scrumproyect.R
import android.content.res.Configuration
import com.google.android.material.navigation.NavigationView
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.ColorFilter
import android.os.Build
import android.view.ViewGroup
import com.example.scrumproyect.view.ui.application.ScrumApplication
import com.example.scrumproyect.view.ui.extensions.enableError
import com.google.android.material.textfield.TextInputLayout


abstract class BaseActivity : AppCompatActivity() {

    protected var toolbar: Toolbar? = null
    protected var drawerLayout: DrawerLayout? = null
    private var drawerToggle: ActionBarDrawerToggle? = null
    protected var navigationView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getView())
        onCreate()
        ScrumApplication.addActivity(this)
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroy() {
        super.onDestroy()
        ScrumApplication.removeActivity(this)
    }

    @SuppressLint("PrivateResource")
    protected fun setSupportActionBar(title: String): ActionBar {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val green = ContextCompat.getColor(this, R.color.text_color)

        toolbar?.setTitleTextColor(green)

        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable?.colorFilter = BlendModeColorFilter(green, BlendMode.SRC_ATOP) as ColorFilter?
        } else {
            drawable?.setColorFilter(green, PorterDuff.Mode.SRC_ATOP)
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(drawable)
        supportActionBar!!.title = title

        return supportActionBar!!
    }

    fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity.currentFocus ?: View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected fun setupDrawer() {
        drawerLayout = findViewById(R.id.drawer)

        drawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                hideKeyboard(this@BaseActivity)
            }
        }
        drawerLayout!!.addDrawerListener(drawerToggle as ActionBarDrawerToggle)
    }

    protected fun setupDrawerContent(listener: SimpleNavigationItemSelectedListener) {
        navigationView = findViewById(R.id.nav)

        navigationView!!.setNavigationItemSelectedListener { item ->
            listener.onNavigationItemSelected(item)
            drawerLayout!!.closeDrawers()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            if (drawerToggle != null) {
                return drawerToggle!!.onOptionsItemSelected(item)
            } else onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if (drawerToggle != null)
            drawerToggle!!.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (drawerToggle != null)
            drawerToggle!!.onConfigurationChanged(newConfig)
    }

    @SuppressLint("RtlHardcoded")
    override fun onBackPressed() {
        if (drawerLayout != null) {
            if (drawerLayout!!.isDrawerOpen(Gravity.LEFT)) {
                drawerLayout!!.closeDrawers()
                return
            }
        }
        super.onBackPressed()
    }

    abstract fun getView(): Int

    open fun onCreate() { }


    interface SimpleNavigationItemSelectedListener {
        fun onNavigationItemSelected(item: MenuItem)
    }

    fun activeAllWrappers() {
        activeAllWrappers(findViewById(android.R.id.content))
    }

    private fun activeAllWrappers(parent: ViewGroup) {
        for (i in 0..parent.childCount) {

            val view = parent.getChildAt(i)

            if (view is TextInputLayout) {
                view.enableError()
            }
            else if (view is ViewGroup) {
                activeAllWrappers(parent.getChildAt(i) as ViewGroup)
            }
        }
    }

    fun hideAllWrappers() {
        activeAllWrappers(findViewById(android.R.id.content))
    }
}