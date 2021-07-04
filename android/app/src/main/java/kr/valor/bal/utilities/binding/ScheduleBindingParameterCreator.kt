package kr.valor.bal.utilities.binding

import android.content.Context
import kr.valor.bal.R
import kr.valor.bal.utilities.elapsedTimeFormatter

object ScheduleBindingParameterCreator {

    fun getElapsedTimeOnTrackingString(elapsedTime: Long?, context: Context): String {
        val res = context.resources
        return elapsedTime?.run {
            elapsedTime.elapsedTimeFormatter()
        } ?: res.getString(R.string.empty_elapsed_time)
    }

}