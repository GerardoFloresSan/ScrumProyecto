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
    private var openDetail : Boolean = false
    private lateinit var adapter: ArticleAdapter

    override fun getFragmentView() = R.layout.fragment_me_book

    override fun onCreate() {
        setTitle(getString(R.string.menu_me))

        adapter = ArticleAdapter { flag, article ->
            when (flag) {
                0 -> {
                    val openURL = Intent(Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(article.titleM)
                    startActivity(openURL)
                }
                1 -> {
                    openDetail = true
                    startActivity(DetailArticleActivity::class.java, article)
                }
                2 -> share(article.urlM)
            }
        }
        presenter.attachView(this)
        presenter.syncMeArticles()
    }

    private fun share(text : String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(sendIntent)
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        if(openDetail) {
            presenter.syncMeArticles()
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    private fun refreshList() {
        refresh.isRefreshing = true
        openDetail = false
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

                if (!openDetail) {
                    recycler.layoutManager = GridLayoutManager(context, 1) as RecyclerView.LayoutManager?
                    adapter.data = args[0] as List<ArticleEntity>
                    recycler.adapter = adapter
                }
                else {
                    adapter.data = args[0] as List<ArticleEntity>
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }
}
