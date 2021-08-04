package kr.valor.bal.adapters.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.data.local.youtube.entity.DatabaseVideo

class VideoAdapter(private val clickListener: VideoClickListener): ListAdapter<DatabaseVideo, ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return VideoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(item)
        }
        (holder as VideoViewHolder).bind(item)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DatabaseVideo>() {
            override fun areItemsTheSame(oldItem: DatabaseVideo, newItem: DatabaseVideo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DatabaseVideo, newItem: DatabaseVideo): Boolean {
                return oldItem == newItem
            }
        }
    }
}