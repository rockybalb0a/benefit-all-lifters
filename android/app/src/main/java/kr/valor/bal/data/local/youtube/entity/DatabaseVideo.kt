package kr.valor.bal.data.local.youtube.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseVideo(
    @PrimaryKey
    val id: String,
    val thumbnailUrl: String,
    val thumbnailWidth: Int,
    val thumbnailHeight: Int,
    val title: String,
    val channelTitle: String
)