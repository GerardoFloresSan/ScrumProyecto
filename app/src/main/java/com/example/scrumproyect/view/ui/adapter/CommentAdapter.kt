package com.example.scrumproyect.view.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scrumproyect.R
import com.example.scrumproyect.data.entity.CommentEntity
import com.example.scrumproyect.view.ui.extensions.inflate
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentAdapter(private val listener: (CommentEntity) -> Unit) : RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    var data: List<CommentEntity> = arrayListOf()

    override fun onBindViewHolder(holder: CommentHolder, position: Int) = holder.bind(data[position], listener)

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CommentHolder.init(parent, viewType)

    override fun getItemViewType(position: Int) = if (position == 0) 0 else 1

    class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: CommentEntity, listener: (CommentEntity) -> Unit) = with(itemView) {
            detail.text = item.comment
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