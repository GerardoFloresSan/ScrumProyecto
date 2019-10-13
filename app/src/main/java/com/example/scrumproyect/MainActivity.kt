package com.example.scrumproyect

import com.example.scrumproyect.data.entity.ProductEntity
import com.example.scrumproyect.view.presenter.ProductPresenter
import com.example.scrumproyect.view.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable


class MainActivity : BaseActivity(), ProductPresenter.View  {

    private val presenter = ProductPresenter()

    override fun getView() = R.layout.activity_main

    override fun onCreate() {
        super.onCreate()

        setSupportActionBar("pruebas")
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        add_product.setOnClickListener {
            presenter.addProduct(ProductEntity().apply {
                id = "test"
                title = "1"
            })
        }
        get_product.setOnClickListener {
            presenter.syncProduct()
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun successSchedule(flag: Int, vararg args: Serializable) {
        if (flag == 0) {
            @Suppress("UNCHECKED_CAST")
            numbers.text = (args[0] as ArrayList<ProductEntity>).size.toString()
        } else {
            numbers.text = "guardado"
        }
    }
}
