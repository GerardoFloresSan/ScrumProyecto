package com.example.scrumproyect.view.ui.fragment

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scrumproyect.R
import com.example.scrumproyect.data.entity.ArticleEntity
import com.example.scrumproyect.view.presenter.ArticlePresenter
import com.example.scrumproyect.view.ui.activity.DetailArticleActivity
import com.example.scrumproyect.view.ui.adapter.ArticleAdapter
import com.example.scrumproyect.view.ui.base.ScrumBaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.Serializable
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.Context.CLIPBOARD_SERVICE
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.LightingColorFilter
import android.graphics.PorterDuff
import android.net.Uri
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import com.example.scrumproyect.view.ui.extensions.getString
import com.example.scrumproyect.view.ui.extensions.isEmpty
import com.example.scrumproyect.view.ui.extensions.showError
import com.example.scrumproyect.view.ui.utils.PapersManager
import com.example.scrumproyect.view.ui.utils.linkpewview.MetaDataKotlin
import com.example.scrumproyect.view.ui.utils.linkpewview.ProcessUrl
import java.util.*


class HomeFragment : ScrumBaseFragment(), ArticlePresenter.View{

    private val presenter = ArticlePresenter()
    private lateinit var listButtons : List<AppCompatImageButton>

    override fun getFragmentView() = R.layout.fragment_home

    override fun onCreate() {
        setTitle(getString(R.string.menu_home))
        pasteData.setOnClickListener {
            pasteDataInEditText()
        }
        cardView.setBackgroundResource(R.drawable.card_view_radius)
        public_url.setOnClickListener {
            validationUrlMetaData()
        }
        configButtons()
        listButtons = listOf(sad_button, neutral_button, happy_button)
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        presenter.syncArticles()
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    private fun validationUrlMetaData() {
        showLoading()
        var contains = false
        hideAllWrappers()

        if (url_text.isEmpty()) {
            url_text.showError("Texto obligatorio")
            return
        }

        for(forbidden in PapersManager.masters.forbiddenWords) {
            if (url_text.getString().contains(forbidden)) {
                contains = true
                break
            }
        }

        if (contains) {
            url_text.showError("url no permititda")
            return
        }

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
            sad_button.setColorFilter(ContextCompat.getColor(context, R.color.md_red_700), PorterDuff.Mode.SRC_ATOP)
        }

        neutral_button.setOnClickListener {
            resetButtons()
            neutral_button.setColorFilter(ContextCompat.getColor(context, R.color.md_yellow_700), PorterDuff.Mode.SRC_ATOP)
        }

        happy_button.setOnClickListener {
            resetButtons()
            happy_button.setColorFilter(ContextCompat.getColor(context, R.color.md_green_700), PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun resetButtons() {
        for(button in listButtons) {
            button.setColorFilter(ContextCompat.getColor(context, R.color.md_grey_500), PorterDuff.Mode.SRC_ATOP)
        }
    }

    fun saveDataInServer(metaDataK : MetaDataKotlin) {
        val article = ArticleEntity().apply {
            titleM = url_text.getString()
            descriptionM = ""
            urlImageM = if (metaDataK.imageUrl.isNotEmpty()) metaDataK.imageUrl else PapersManager.masters.urlGeneral

            urlM = url_text.getString()
            timeCreate = Date().time.toInt()
            metadata = metaDataK
        }
        presenter.addArticle(article)
    }

    fun pasteDataInEditText() {
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

    @Suppress("UNCHECKED_CAST", "USELESS_CAST")
    override fun successArticle(flag: Int, vararg args: Serializable) {
        if (flag == 0) {
            recycler.layoutManager = GridLayoutManager(context, 1) as RecyclerView.LayoutManager?
            recycler.adapter = ArticleAdapter(args[0] as List<ArticleEntity>) { flag, product ->
                if(flag == 0) {
                    val openURL = Intent(Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(product.titleM)
                    startActivity(openURL)
                } else {
                    startActivity(DetailArticleActivity::class.java, product)
                }
            }
        } else {
            resetButtons()
            
        }
    }
}
