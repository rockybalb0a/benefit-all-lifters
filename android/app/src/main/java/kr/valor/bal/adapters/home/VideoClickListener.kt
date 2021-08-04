package kr.valor.bal.adapters.home

import kr.valor.bal.data.local.youtube.entity.DatabaseVideo

class VideoClickListener(val clickListener: (DatabaseVideo) -> Unit) {
    fun onClick(video: DatabaseVideo) = clickListener(video)
}