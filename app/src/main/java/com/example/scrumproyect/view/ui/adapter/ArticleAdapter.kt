package com.example.scrumproyect.view.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.scrumproyect.R
import com.example.scrumproyect.data.entity.ArticleEntity
import com.example.scrumproyect.view.ui.extensions.inflate
import com.example.scrumproyect.view.ui.utils.PapersManager
import kotlinx.android.synthetic.main.item_article.view.*

class ArticleAdapter(private val titles: List<ArticleEntity>, private val listener: (Int, ArticleEntity) -> Unit) : RecyclerView.Adapter<ArticleAdapter.ArticleHolder>() {

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) = holder.bind(titles[position], listener)

    override fun getItemCount() = titles.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ArticleHolder.init(parent, viewType)

    override fun getItemViewType(position: Int) = if (position == 0) 0 else 1

    class ArticleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ArticleEntity, listener: (Int, ArticleEntity) -> Unit) = with(itemView) {
            title.text = item.titleM
            Glide.with(this@with)
                .load(item.urlImageM)
                .apply(RequestOptions()
                    .centerCrop()
                    /*.skipMemoryCache(true)*/
                    .error(ContextCompat.getDrawable(context, R.drawable.background_example))
                    /*.diskCacheStrategy(DiskCacheStrategy.NONE)*/
                    .priority(Priority.HIGH))
                .listener(object : RequestListener<Drawable> {
                @SuppressLint("CheckResult")
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    return false
                }
            }).into(image)

            title.setOnClickListener {
                listener(0, item)
            }

            sad_number.text = item.sad.size.toString()
            neutral_number.text = item.neutral.size.toString()
            happy_number.text = item.happy.size.toString()

            more_info.setOnClickListener { listener(1, item) }
        }

        companion object {
            @Suppress("RemoveRedundantQualifierName", "UNUSED_PARAMETER")
            fun init(parent: ViewGroup, viewType: Int) : ArticleAdapter.ArticleHolder {
                val view = parent.inflate(R.layout.item_article)

                /*val params = view.layoutParams as RecyclerView.LayoutParams
                params.setMargins(Methods.toPixels(20f).toInt(), Methods.toPixels(10f).toInt(),
                    Methods.toPixels(20f).toInt(), Methods.toPixels(10f).toInt())
                view.layoutParams = params*/

                return ArticleAdapter.ArticleHolder(view)
            }
        }
    }
}