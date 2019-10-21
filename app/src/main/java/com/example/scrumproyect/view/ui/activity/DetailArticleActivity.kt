package com.example.scrumproyect.view.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scrumproyect.R
import com.example.scrumproyect.data.entity.ArticleEntity
import com.example.scrumproyect.data.entity.CommentEntity
import com.example.scrumproyect.view.presenter.ArticlePresenter
import com.example.scrumproyect.view.presenter.CommentPresenter
import com.example.scrumproyect.view.ui.adapter.CommentAdapter
import com.example.scrumproyect.view.ui.base.ScrumBaseActivity
import com.example.scrumproyect.view.ui.extensions.clean
import com.example.scrumproyect.view.ui.extensions.getString
import kotlinx.android.synthetic.main.activity_dertail_product.*
import java.io.Serializable

class DetailArticleActivity : ScrumBaseActivity() , CommentPresenter.View, ArticlePresenter.View {

    private val presenterComment = CommentPresenter()
    private val presenterArticle = ArticlePresenter()
    private var list = ArrayList<CommentEntity>()
    private lateinit var adapter: CommentAdapter

    private lateinit var entity: ArticleEntity

    override fun getView() = R.layout.activity_dertail_product

    override fun onCreate() {
        super.onCreate()
        setSupportActionBar("")
        adapter = CommentAdapter {}

        entity = intent.getSerializableExtra("extra0") as ArticleEntity

        Glide.with(this@DetailArticleActivity).load(entity.urlImageM).into(image)

        title_detail.text = entity.titleM
        description_detail.text = entity.descriptionM

        if (description_detail.text.isNullOrEmpty()) {
            description_detail.visibility = View.GONE
        } else {
            description_detail.visibility = View.VISIBLE
        }
        send.setOnClickListener { verify() }

        sad_number.text = entity.sad.size.toString()
        neutral_number.text = entity.neutral.size.toString()
        happy_number.text = entity.happy.size.toString()

        title_detail.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(entity.titleM)
            startActivity(openURL)
        }

        sad_button.setOnClickListener {
            presenterArticle.removeLike(0, entity.idM)
        }

        neutral_button.setOnClickListener {
            presenterArticle.removeLike(1, entity.idM)
        }

        happy_button.setOnClickListener {
            presenterArticle.removeLike(2, entity.idM)
        }
    }

    private fun verify() {
        if (input.getString().isNotEmpty()) {
            presenterComment.addComment(entity.idM, CommentEntity().apply {
                id = "1"
                comment = input.getString()
            })
            input.clean()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(input.windowToken, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        presenterComment.attachView(this)
        presenterArticle.attachView(this)
        presenterComment.syncComment(entity.idM)
    }

    override fun onPause() {
        super.onPause()
        presenterComment.detachView()
        presenterArticle.detachView()
    }

    @Suppress("UNCHECKED_CAST", "USELESS_CAST")
    override fun successSchedule(flag: Int, vararg args: Serializable) {
        if (flag == 0) {
            list = args[0] as ArrayList<CommentEntity>
            recycler.layoutManager = GridLayoutManager(this, 1) as RecyclerView.LayoutManager?
            recycler.adapter = adapter
            adapter.data = (list as List<CommentEntity>)
        } else {
            list.add(args[0] as CommentEntity)
            adapter.data = (list as List<CommentEntity>)
            recycler.adapter?.notifyDataSetChanged()
        }
    }

    override fun successArticle(flag: Int, vararg args: Serializable) {

    }
}
