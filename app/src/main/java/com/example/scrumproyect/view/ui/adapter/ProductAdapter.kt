package com.example.scrumproyect.view.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scrumproyect.R
import com.example.scrumproyect.data.entity.ArticleEntity
import com.example.scrumproyect.view.ui.extensions.inflate
import kotlinx.android.synthetic.main.item_product.view.*

class ProductAdapter(private val titles: List<ArticleEntity>, private val listener: (Int, ArticleEntity) -> Unit) : RecyclerView.Adapter<ProductAdapter.ProductHolder>() {

    override fun onBindViewHolder(holder: ProductHolder, position: Int) = holder.bind(titles[position], listener)

    override fun getItemCount() = titles.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductHolder.init(parent, viewType)

    override fun getItemViewType(position: Int) = if (position == 0) 0 else 1

    class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ArticleEntity, listener: (Int, ArticleEntity) -> Unit) = with(itemView) {
            title.text = item.titleM
            Glide.with(this@with).load(item.urlImageM).into(image)
            title.setOnClickListener {
                listener(0, item)
            }

            more_info.setOnClickListener { listener(1, item) }
        }

        companion object {
            @Suppress("RemoveRedundantQualifierName", "UNUSED_PARAMETER")
            fun init(parent: ViewGroup, viewType: Int) : ProductAdapter.ProductHolder {
                val view = parent.inflate(R.layout.item_product)

                /*val params = view.layoutParams as RecyclerView.LayoutParams
                params.setMargins(Methods.toPixels(20f).toInt(), Methods.toPixels(10f).toInt(),
                    Methods.toPixels(20f).toInt(), Methods.toPixels(10f).toInt())
                view.layoutParams = params*/

                return ProductAdapter.ProductHolder(view)
            }
        }
    }
}