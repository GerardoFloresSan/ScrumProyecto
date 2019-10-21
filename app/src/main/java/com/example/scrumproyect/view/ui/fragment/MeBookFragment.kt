package com.example.scrumproyect.view.ui.fragment


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.scrumproyect.R
import com.example.scrumproyect.data.entity.ArticleEntity
import com.example.scrumproyect.view.presenter.ArticlePresenter
import com.example.scrumproyect.view.ui.activity.DetailArticleActivity
import com.example.scrumproyect.view.ui.adapter.ArticleAdapter
import com.example.scrumproyect.view.ui.base.ScrumBaseFragment
import kotlinx.android.synthetic.main.fragment_me_book.*
import java.io.Serializable

class MeBookFragment : ScrumBaseFragment(), ArticlePresenter.View{

    private val presenter = ArticlePresenter()

    override fun getFragmentView() = R.layout.fragment_me_book

    override fun onCreate() {
        setTitle(getString(R.string.menu_me))
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        presenter.syncMeArticles()
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    private fun refreshList() {
        refresh.isRefreshing = true
        presenter.syncMeArticles()
    }


    @Suppress("UNCHECKED_CAST", "USELESS_CAST", "UseExpressionBody")
    override fun successArticle(flag: Int, vararg args: Serializable) {
        if (flag == 0) {
            refresh.isRefreshing = false
            val list = args[0] as List<ArticleEntity>
            linear_loading.visibility = View.GONE
            linear_error.visibility = View.GONE
            refresh.visibility = View.GONE

            @Suppress("UseExpressionBody")
            if (list.isEmpty()) {
                linear_loading.visibility = View.GONE
                linear_error.visibility = View.VISIBLE
                refresh.visibility = View.GONE

                text_error.text = "No hay articulos"
            } else {
                linear_loading.visibility = View.GONE
                linear_error.visibility = View.GONE
                refresh.visibility = View.VISIBLE

                refresh.setOnRefreshListener {
                    linear_loading.visibility = View.VISIBLE
                    linear_error.visibility = View.GONE
                    refresh.visibility = View.GONE
                    refreshList()
                }

                recycler.layoutManager = GridLayoutManager(context, 1) as RecyclerView.LayoutManager?
                recycler.adapter = ArticleAdapter(args[0] as List<ArticleEntity>) { flag, product ->
                    if (flag == 0) {
                        val openURL = Intent(Intent.ACTION_VIEW)
                        openURL.data = Uri.parse(product.titleM)
                        startActivity(openURL)
                    } else {
                        startActivity(DetailArticleActivity::class.java, product)
                    }
                }
            }
        }
    }
}
