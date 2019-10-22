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
import com.example.scrumproyect.data.entity.CommentEntity
import com.example.scrumproyect.view.ui.extensions.inflate
import com.example.scrumproyect.view.ui.utils.Methods
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentAdapter(private val listener: (CommentEntity) -> Unit) : RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    var data: List<CommentEntity> = arrayListOf()

    override fun onBindViewHolder(holder: CommentHolder, position: Int) = holder.bind(data[position], listener)

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentHolder.init(parent, viewType)

    override fun getItemViewType(position: Int) = if (position == 0) 0 else 1

    class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: CommentEntity, listener: (CommentEntity) -> Unit) = with(itemView) {

            Glide.with(this)
                .asBitmap()
                .load(item.urlUser)
                .apply(RequestOptions()
                    .centerCrop()
                    .skipMemoryCache(true)
                    .placeholder(Methods.drawable(avatar))
                    .error(Methods.drawable(avatar))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .priority(Priority.HIGH))
                .listener(object : RequestListener<Bitmap> {
                    @SuppressLint("CheckResult")
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        avatar.colorFilter = null
                        return false
                    }
                })
                .into(avatar)
            user.text = item.nameUser
            time.text = Methods.getTime(item.time)
            description.text = item.comment
        }

        companion object {
            fun init(parent: ViewGroup, viewType: Int) : CommentAdapter.CommentHolder {
                val view = parent.inflate(R.layout.item_comment)

                /*val params = view.layoutParams as RecyclerView.LayoutParams
                params.setMargins(Methods.toPixels(20f).toInt(), Methods.toPixels(10f).toInt(),
                    Methods.toPixels(20f).toInt(), Methods.toPixels(10f).toInt())
                view.layoutParams = params*/

                return CommentAdapter.CommentHolder(view)
            }
        }
    }
}