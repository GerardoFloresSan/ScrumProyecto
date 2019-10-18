package com.example.scrumproyect.view.ui.activity

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.scrumproyect.R
import com.example.scrumproyect.data.entity.ArticleEntity
import com.example.scrumproyect.view.presenter.ArticlePresenter
import com.example.scrumproyect.view.ui.base.ScrumBaseActivity
import com.example.scrumproyect.view.ui.extensions.getString
import com.example.scrumproyect.view.ui.extensions.isEmpty
import com.example.scrumproyect.view.ui.extensions.showError
import kotlinx.android.synthetic.main.activity_add_product.*
import java.io.Serializable

class AddProductActivity : ScrumBaseActivity() , ArticlePresenter.View{

    private val presenter = ArticlePresenter()

    override fun getView() = R.layout.activity_add_product

    override fun onCreate() {
        super.onCreate()
        setSupportActionBar("Agregar producto")
        cardView.setBackgroundResource(R.drawable.card_view_radius)
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_send, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.send) {
            onClickSave(null)
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    fun onClickSave(@Suppress("UNUSED_PARAMETER") view: View?) {
        verify()
    }

    private fun verify() {
        hideAllWrappers()
        if (name.isEmpty()) {
            name.showError("Texto Obligatorio")
        }

        if (description.isEmpty()) {
            description.showError("Texto Obligatorio")
        }


        if (description.isEmpty() || name.isEmpty()) {
            return
        }

        presenter.addArticle(ArticleEntity().apply {
            this.idM = "test"
            this.titleM =  name.getString()
            this.descriptionM = description.getString()
            this.urlImageM = "https://firebasestorage.googleapis.com/v0/b/proyectoscrum-2d2e3.appspot.com/o/background_example.jpg?alt=media&token=db01a943-1352-4382-8299-e96774708537"
        })
    }

    override fun successArticle(flag: Int, vararg args: Serializable) {
        Toast.makeText(this, "Producto agregada con éxito", Toast.LENGTH_SHORT).show()
        finish()
    }
}
