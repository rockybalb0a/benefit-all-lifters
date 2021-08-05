package kr.valor.bal.adapters.home

import android.view.ViewGroup
import kr.valor.bal.R
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.adapters.ViewHolderFactory
import kr.valor.bal.data.local.youtube.DatabaseVideo
import kr.valor.bal.databinding.ItemHomeVideoBinding
import kr.valor.bal.utilities.binding.VideoBindingParameterCreator

class VideoViewHolder private constructor(private val binding: ItemHomeVideoBinding): ViewHolder(binding) {
    fun bind(item: DatabaseVideo) {
        with(binding) {
            bindingCreator = VideoBindingParameterCreator
            video = item
            executePendingBindings()
        }
    }
    companion object: ViewHolderFactory() {
        override fun create(parent: ViewGroup): ViewHolder {
            val binding =
                inflate<ItemHomeVideoBinding>(parent, R.layout.item_home_video)
            return VideoViewHolder(binding)
        }
    }
}