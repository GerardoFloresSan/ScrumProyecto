package com.example.scrumproyect

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.scrumproyect.data.entity.ProductEntity
import com.example.scrumproyect.view.presenter.ProductPresenter
import com.example.scrumproyect.view.ui.activity.AddProductActivity
import com.example.scrumproyect.view.ui.activity.DetailProductActivity
import com.example.scrumproyect.view.ui.adapter.ProductAdapter
import com.example.scrumproyect.view.ui.base.ScrumBaseActivity
import com.example.scrumproyect.view.ui.extensions.startActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable


class MainActivity : ScrumBaseActivity(), ProductPresenter.View  {

    private val presenter = ProductPresenter()

    override fun getView() = R.layout.activity_main

    override fun onCreate() {
        super.onCreate()
        setSupportActionBar("Productos")
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        presenter.syncProduct()
        fab.setOnClickListener {
            startActivity(AddProductActivity::class.java)
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
            recycler.layoutManager = GridLayoutManager(this, 1)
            recycler.adapter = ProductAdapter(list as List<ProductEntity>) {
                startActivity(DetailProductActivity::class.java, it)
            }
        }
    }
}
