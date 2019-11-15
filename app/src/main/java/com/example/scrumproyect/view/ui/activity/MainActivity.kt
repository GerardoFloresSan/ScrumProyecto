package com.example.scrumproyect.view.ui.activity

import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.example.scrumproyect.R
import com.example.scrumproyect.data.entity.ArticleEntity
import com.example.scrumproyect.view.presenter.MasterPresenter
import com.example.scrumproyect.view.presenter.UserPresenter
import com.example.scrumproyect.view.ui.base.ScrumBaseActivity
import com.example.scrumproyect.view.ui.fragment.*
import com.example.scrumproyect.view.ui.utils.FacebookHelper
import com.example.scrumproyect.view.ui.utils.PapersManager
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main2.*
import java.io.Serializable


class MainActivity : ScrumBaseActivity(), UserPresenter.View, MasterPresenter.View{

    private var current = 0
    private var fragments = ArrayList<Fragment>()
    private val presenterUser = UserPresenter()
    private val presenterMaster = MasterPresenter()
    private lateinit var menuG : Menu
    private var openMenuS = false

    override fun getView() = R.layout.activity_main

    override fun onCreate() {
        super.onCreate()

        setSupportActionBar("Agilidad sin humo")

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
                }
                if(current != 0) openMenuSearch (false)
                PapersManager.openAddArticle = false
                replaceFragment(fragments[current])
            }
        })

        nav_close.setOnClickListener {
            logout()
        }

        configurationNavigation()


        if(!PapersManager.openLoginWithDetail) {
            replaceFragment(fragments[current])
        } else {
            PapersManager.openLoginWithDetail = false
            configResultIfHome(false)
        }
        presenterMaster.attachView(this)
        presenterMaster.syncMaster()

    }

    fun openMenuSearch(b : Boolean){
        menuG.clear()
        openMenuS = b
        onCreateOptionsMenu(menuG)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.menu_search, menu)
        val actionMenu: MenuItem = menu!!.findItem(R.id.action_search)
        menu?.findItem(R.id.action_search)?.isVisible = openMenuS
        val searchView = actionMenu.actionView as SearchView
        searchView.findViewById<EditText>(R.id.search_src_text)
            .setHintTextColor(resources.getColor(R.color.md_white_1000))

        val white = ContextCompat.getColor(this, R.color.md_white_1000)
        toolbar!!.setTitleTextColor(white)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!searchView.isIconified) {
                    searchView.setIconifiedByDefault(true)
                }
                actionMenu.collapseActionView()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filterModeList: List<ArticleEntity> = filter((fragments[0] as HomeFragment).getList(), newText!!)
                (fragments[0] as HomeFragment).search(filterModeList)
                return true
            }

        })
        menuG = menu
        return super.onCreateOptionsMenu(menu)
    }

    private fun filter(pl: List<ArticleEntity>, query: String): List<ArticleEntity> {
        var query = query
        query = query.toLowerCase()
        val filteredModelList = arrayListOf<ArticleEntity>()
        for (model in pl) {
            val text = model.descriptionM.toLowerCase() + model.metadata.description.toLowerCase() + model.urlM.toLowerCase()
            if (text.contains(query)) {
                filteredModelList.add(model)
            }
        }
        return filteredModelList
    }

    override fun onResume() {
        super.onResume()
        presenterUser.attachView(this)
        presenterMaster.attachView(this)
        if (PapersManager.masters.forbiddenWords.isEmpty()) presenterMaster.syncMaster()

        //welcome_user.text = "Bienvenido " + PapersManager.userEntity.name
    }

    private fun configurationNavigation() {
        navigationView?.menu?.apply {
            findItem(R.id.nav_user).title = getString(if (PapersManager.session) R.string.menu_profile else R.string.menu_login)
            findItem(R.id.nav_me).isVisible = PapersManager.session
            nav_close.visibility = if (PapersManager.session) View.VISIBLE else View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        presenterUser.detachView()
        presenterMaster.detachView()
    }

    fun openLoginConfig() {
        MaterialDialog.Builder(this)
            .title("¿Desea agregar nuevos articulo a la lista?")
            .content("Debe iniciar sesion ó registrese con su cuenta para poder continuar")
            .positiveText("Si")
            .positiveColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .onPositive { _, _ -> configResultIfHome(true)}
            .negativeText("No")
            .negativeColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            .show()
    }
    
    fun openLoginConfigCalif() {
        MaterialDialog.Builder(this)
            .title("¿Desea calificar un articulo de la lista?")
            .content("Debe iniciar sesion ó registrese con su cuenta para poder continuar")
            .positiveText("Si")
            .positiveColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .onPositive { _, _ -> configResultIfHome(false)}
            .negativeText("No")
            .negativeColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            .show()
    }

    private fun configResultIfHome(type : Boolean) {
        PapersManager.openAddArticle = type
        replaceFragment(fragments[1])
    }

    fun login(emailN : String, passwordN : String) = presenterUser.login(emailN, passwordN)

    fun login(accessToken: AccessToken) = presenterUser.loginFaceBook(accessToken)

    fun login(acct: GoogleSignInAccount) = presenterUser.loginGoogle(acct)

    private fun logout() = presenterUser.logout(googleToken)

    override fun successUser(flag: Int, vararg args: Serializable) {
        when(flag) {
            0, 1, 2 -> {
                configurationNavigation()
                replaceFragment(fragments[0])
            }
            3, 4, 5 -> {
                configurationNavigation()
                when(current) {
                    0 -> (fragments[0] as HomeFragment).config(false)
                    2, 3 -> replaceFragment(fragments[0])
                }
            }
        }
    }
    override fun successMasters(flag: Int, vararg args: Serializable) {
        if (flag == 1) Handler().postDelayed({ presenterMaster.syncMaster() }, 2000)
    }


}
