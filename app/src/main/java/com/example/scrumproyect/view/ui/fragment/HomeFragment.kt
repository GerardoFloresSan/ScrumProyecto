package com.example.scrumproyect.view.ui.fragment

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scrumproyect.R
import com.example.scrumproyect.data.entity.ArticleEntity
import com.example.scrumproyect.view.presenter.ArticlePresenter
import com.example.scrumproyect.view.ui.activity.DetailArticleActivity
import com.example.scrumproyect.view.ui.activity.MainActivity
import com.example.scrumproyect.view.ui.adapter.ArticleAdapter
import com.example.scrumproyect.view.ui.base.ScrumBaseFragment
import com.example.scrumproyect.view.ui.extensions.clean
import com.example.scrumproyect.view.ui.extensions.getString
import com.example.scrumproyect.view.ui.extensions.isEmpty
import com.example.scrumproyect.view.ui.extensions.showError
import com.example.scrumproyect.view.ui.utils.PapersManager
import com.example.scrumproyect.view.ui.utils.linkpewview.MetaDataKotlin
import com.example.scrumproyect.view.ui.utils.linkpewview.ProcessUrl
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.Serializable
import java.util.*

class HomeFragment : ScrumBaseFragment(), ArticlePresenter.View {

    private val presenter = ArticlePresenter()
    private lateinit var listButtons: List<AppCompatImageButton>
    var selectItem = 10
    private var openDetail : Boolean = false
    private lateinit var adapter: ArticleAdapter
    var listArticle : List<ArticleEntity> = arrayListOf()

    override fun getFragmentView() = R.layout.fragment_home

    override fun onCreate() {
        setTitle(getString(R.string.menu_home))

        pasteData.setOnClickListener {
            pasteDataInEditText()
        }
        cardView.setBackgroundResource(R.drawable.card_view_radius)

        add_public_url.setOnClickListener {
            validationUrlMetaData()
        }

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

        listButtons = listOf(sad_button, neutral_button, happy_button)
        selectItem = 10
        configButtons()
        presenter.attachView(this)
        presenter.syncArticles()

        linear_loading.visibility = View.VISIBLE
        linear_error.visibility = View.GONE
        refresh.visibility = View.GONE

        open_public_url.setOnClickListener {
            config(true)
        }

        cancel_public_url.setOnClickListener {
            config(false)
        }
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

        add_new_article.visibility = if (PapersManager.session) View.VISIBLE else View.GONE

        when (selectItem) {
            0 -> sad_button.setColorFilter(
                ContextCompat.getColor(context, R.color.md_red_700),
                PorterDuff.Mode.SRC_ATOP
            )
            1 -> neutral_button.setColorFilter(
                ContextCompat.getColor(context, R.color.md_yellow_700),
                PorterDuff.Mode.SRC_ATOP
            )
            2 -> happy_button.setColorFilter(
                ContextCompat.getColor(context, R.color.md_green_700),
                PorterDuff.Mode.SRC_ATOP
            )
        }

        if(openDetail) {
            presenter.syncArticles()
        }
    }

    private fun config(type : Boolean) {
        add_new_article.visibility = if(type) View.GONE else View.VISIBLE
        linear_add_post.visibility = if(type) View.VISIBLE else View.GONE
        linear_data.visibility = if(type) View.GONE else View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    private fun validationUrlMetaData() {
        var contains = false
        hideAllWrappers()

        if (url_text.isEmpty()) {
            url_text.showError("Texto obligatorio")
            return
        }

        for (forbidden in PapersManager.masters.forbiddenWords) {
            if (url_text.getString().contains(forbidden)) {
                contains = true
                break
            }
        }

        if (contains) {
            url_text.showError("url no permititda")
            return
        }

        if (selectItem == 10) {
            Toast.makeText(context, "Califica la Url", Toast.LENGTH_LONG).show()
            return
        }

        showLoading()
        ProcessUrl(object :
            ProcessUrl.DoStuff {
            override fun getContext() = context
            override fun done(result: MetaDataKotlin) {
                hideLoading()
                when (result.typeError) {
                    0 -> saveDataInServer(result)
                    1 -> {
                        url_text.showError("No hay respuesta de la url")
                        return
                    }
                    else -> {
                        url_text.showError("La url podría ser no válida")
                        return
                    }
                }
            }

        }).execute(url_text.getString())
    }

    private fun configButtons() {
        sad_button.setOnClickListener {
            resetButtons()
            selectItem = 0
            sad_button.setColorFilter(ContextCompat.getColor(context, R.color.md_red_700), PorterDuff.Mode.SRC_ATOP)
            sad_button.background = ContextCompat.getDrawable(context, R.drawable.circle_border)
        }

        neutral_button.setOnClickListener {
            resetButtons()
            selectItem = 1
            neutral_button.setColorFilter(ContextCompat.getColor(context, R.color.md_yellow_700), PorterDuff.Mode.SRC_ATOP)
            neutral_button.background = ContextCompat.getDrawable(context, R.drawable.circle_border)
        }

        happy_button.setOnClickListener {
            resetButtons()
            selectItem = 2
            happy_button.setColorFilter(ContextCompat.getColor(context, R.color.md_green_700), PorterDuff.Mode.SRC_ATOP)
            happy_button.background = ContextCompat.getDrawable(context, R.drawable.circle_border)
        }
    }

    private fun resetButtons() {
        for (button in listButtons) {
            button.setColorFilter(ContextCompat.getColor(context, R.color.md_grey_500), PorterDuff.Mode.SRC_ATOP)
            button.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
        }
    }

    fun saveDataInServer(metaDataK: MetaDataKotlin) {
        val article = ArticleEntity().apply {
            titleM = url_text.getString()
            descriptionM = ""
            urlImageM = if (metaDataK.imageUrl.isNotEmpty()) metaDataK.imageUrl else PapersManager.masters.urlGeneral

            urlM = url_text.getString()
            timeCreate = Date().time
            descriptionM = description_text.getString()
            metadata = metaDataK
            when (selectItem) {
                0 -> sad = arrayListOf(PapersManager.userEntity.uidUser)
                1 -> neutral = arrayListOf(PapersManager.userEntity.uidUser)
                2 -> happy = arrayListOf(PapersManager.userEntity.uidUser)
            }
        }
        presenter.addArticle(article, selectItem)
    }

    private fun pasteDataInEditText() {
        val clipboard = activity!!.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
        var pasteData = ""
        if (!clipboard!!.hasPrimaryClip()) {
            url_text.setText(pasteData)
        } else if (!clipboard.primaryClipDescription!!.hasMimeType(MIMETYPE_TEXT_PLAIN)) {
            url_text.setText(pasteData)
        } else {
            val item = clipboard.primaryClip?.getItemAt(0)
            pasteData = item?.text.toString()
            url_text.setText(pasteData)
        }
    }

    private fun refreshList() {
        refresh.isRefreshing = true
        openDetail = false
        presenter.syncArticles()
    }

    fun search(list : List<ArticleEntity>) {
        adapter.data = list
        adapter.notifyDataSetChanged()
    }

    fun getList() : List<ArticleEntity> = listArticle


    @Suppress("UNCHECKED_CAST", "USELESS_CAST", "UseExpressionBody")
    override fun successArticle(flag: Int, vararg args: Serializable) {
        if (flag == 0) {
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

                (activity as MainActivity).openMenuSearch(true)
                if (!openDetail) {
                    recycler.layoutManager = GridLayoutManager(context, 1) as RecyclerView.LayoutManager?
                    adapter.data = listArticle
                    recycler.adapter = adapter
                } else {
                    openDetail = false
                    adapter.data = listArticle
                    adapter.notifyDataSetChanged()
                }
            }
        } else {
            resetButtons()
            selectItem = 10
            url_text.clean()
        }
    }
}
