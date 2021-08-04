package kr.valor.bal.utilities.binding

import kr.valor.bal.data.local.youtube.entity.DatabaseVideo

object VideoBindingParameterCreator {

    fun getTitleString(video: DatabaseVideo?): String? {
        return video?.title
    }

    fun getChannelTitleString(video: DatabaseVideo?): String? {
        return video?.channelTitle
    }

}