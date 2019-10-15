package com.example.scrumproyect

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scrumproyect.data.entity.ProductEntity
import com.example.scrumproyect.view.presenter.ProductPresenter
import com.example.scrumproyect.view.presenter.ProductRxPresenter
import com.example.scrumproyect.view.ui.activity.AddProductActivity
import com.example.scrumproyect.view.ui.activity.DetailProductActivity
import com.example.scrumproyect.view.ui.adapter.ProductAdapter
import com.example.scrumproyect.view.ui.base.ScrumBaseActivity
import com.example.scrumproyect.view.ui.extensions.startActivity
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable
import javax.inject.Inject


class MainActivity : ScrumBaseActivity(), ProductPresenter.View {

    private val presenter = ProductPresenter()

    /*@Inject
    lateinit var presenter: ProductRxPresenter*/

    override fun getView() = R.layout.activity_main

    override fun onCreate() {
        super.onCreate()

        /*component.inject(this)*/
        setSupportActionBar("Productos")
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        presenter.syncProduct()
        fab.setOnClickListener {
            startActivity(AddProductActivity::class.java)
        }
        logout.setOnClickListener {
            logoutSesion()
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    @Suppress("UNCHECKED_CAST")
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
    }
}
