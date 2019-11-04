package com.example.scrumproyect.view.ui.fragment


import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.scrumproyect.R
import com.example.scrumproyect.data.entity.ArticleEntity
import com.example.scrumproyect.view.presenter.ArticlePresenter
import com.example.scrumproyect.view.ui.activity.DetailArticleActivity
import com.example.scrumproyect.view.ui.activity.MainActivity
import com.example.scrumproyect.view.ui.adapter.ArticleAdapter
import com.example.scrumproyect.view.ui.base.ScrumBaseFragment
import com.example.scrumproyect.view.ui.utils.PapersManager
import kotlinx.android.synthetic.main.fragment_me_book.*
import java.io.Serializable

class MeBookFragment : ScrumBaseFragment(), ArticlePresenter.View{

    private val presenter = ArticlePresenter()
    private var openDetail : Boolean = false
    private lateinit var adapter: ArticleAdapter
    var listArticle : List<ArticleEntity> = arrayListOf()

    override fun getFragmentView() = R.layout.fragment_me_book

    @Suppress("USELESS_CAST")
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
                2 -> {
                    share(article.urlM)
                }
                3 -> {
                    //sad
                    presenter.addUpdateLike(0, article.idM)
                }
                4 -> {
                    //neutral
                    presenter.addUpdateLike(1, article.idM)
                }
                5 -> {
                    //happy
                    presenter.addUpdateLike(2, article.idM)
                }
                11 -> {
                    (activity as MainActivity).openLoginConfigCalif()
                }
            }
        }
        recycler.layoutManager = GridLayoutManager(context, 1) as RecyclerView.LayoutManager?
        adapter.data = listArticle
        recycler.adapter = adapter

        presenter.attachView(this)
        presenter.syncMeArticles()
    }

    private fun share(text : String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Hola mira lo que encontre en ....\n $text")
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
        when (flag) {
            0 -> {
                refresh.isRefreshing = false
                listArticle = args[0] as List<ArticleEntity>
                linear_loading.visibility = View.GONE
                linear_error.visibility = View.GONE
                refresh.visibility = View.GONE

                @Suppress("UseExpressionBody")
                if (listArticle.isEmpty()) {
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

                    openDetail = false
                    adapter.data = listArticle
                    adapter.notifyDataSetChanged()
                }
            }
            1 -> {
                openDetail = false
                /*(activity as MainActivity).openMenuSearch(true)*/
                for (item in listArticle) {
                    if (item.idM == args[1] as String) {
                        (item.sad as ArrayList).remove(PapersManager.userEntity.uidUser)
                        (item.neutral as ArrayList).remove(PapersManager.userEntity.uidUser)
                        (item.happy as ArrayList).remove(PapersManager.userEntity.uidUser)
                        when(args[0]) {
                            0 -> {
                                (item.sad as ArrayList).add(PapersManager.userEntity.uidUser)
                            }
                            1 -> {
                                (item.neutral as ArrayList).add(PapersManager.userEntity.uidUser)
                            }
                            2 -> {
                                (item.happy as ArrayList).add(PapersManager.userEntity.uidUser)
                            }
                        }
                    }
                }

                adapter.data = listArticle
                adapter.notifyDataSetChanged()
            }
            2 -> {}
        }
    }
}
