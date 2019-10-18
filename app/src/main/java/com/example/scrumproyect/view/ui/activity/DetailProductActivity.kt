package com.example.scrumproyect.view.ui.activity

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.scrumproyect.R
import com.example.scrumproyect.data.entity.CommentEntity
import com.example.scrumproyect.data.entity.ArticleEntity
import com.example.scrumproyect.view.presenter.CommentPresenter
import com.example.scrumproyect.view.ui.adapter.CommentAdapter
import com.example.scrumproyect.view.ui.base.ScrumBaseActivity
import com.example.scrumproyect.view.ui.extensions.clean
import com.example.scrumproyect.view.ui.extensions.getString
import kotlinx.android.synthetic.main.activity_dertail_product.*
import java.io.Serializable

class DetailProductActivity : ScrumBaseActivity() , CommentPresenter.View{

    private val presenter = CommentPresenter()
    private var list = ArrayList<CommentEntity>()
    private lateinit var adapter: CommentAdapter

    private lateinit var entity: ArticleEntity

    override fun getView() = R.layout.activity_dertail_product

    override fun onCreate() {
        super.onCreate()
        setSupportActionBar("")
        adapter = CommentAdapter {}

        entity = intent.getSerializableExtra("extra0") as ArticleEntity

        Glide.with(this@DetailProductActivity).load(entity.urlImageM).into(image)

        title_detail.text = entity.titleM
        description_detail.text = entity.descriptionM
        send.setOnClickListener { verify() }
    }

    private fun verify() {
        if (input.getString().isNotEmpty()) {
            presenter.addComment(entity.idM, CommentEntity().apply {
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
        presenter.attachView(this)
        presenter.syncComment(entity.idM)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    @Suppress("UNCHECKED_CAST", "USELESS_CAST")
    override fun successSchedule(flag: Int, vararg args: Serializable) {
        if (flag == 0) {
            list = args[0] as ArrayList<CommentEntity>
            recycler.layoutManager = GridLayoutManager(this, 1)
            recycler.adapter = adapter
            adapter.data = (list as List<CommentEntity>)
        } else {
            list.add(args[0] as CommentEntity)
            adapter.data = (list as List<CommentEntity>)
            recycler.adapter?.notifyDataSetChanged()
        }
    }
}
