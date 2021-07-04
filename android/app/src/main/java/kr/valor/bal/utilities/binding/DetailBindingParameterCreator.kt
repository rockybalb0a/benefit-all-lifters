package kr.valor.bal.utilities.binding

import android.content.Context
import kr.valor.bal.R
import kr.valor.bal.data.entities.WorkoutSet

object DetailBindingParameterCreator {

    fun getCurrentSetsNumberString(idx: Int, context: Context): String =
        context.resources.getString(R.string.sets_text_full_template, idx + 1)

    fun getWeightsWithWeightUnitString(item: WorkoutSet?, context: Context): String {
        val weights = item?.weights?.toInt() ?: 20
        return context.resources.getString(R.string.weights_text, weights)
    }

    fun getRepsString(item: WorkoutSet?, context: Context): String {
        val res = context.resources
        val reps = item?.reps ?: 0
        return if (reps > 1) {
            res.getString(R.string.reps_text_with, reps)
        } else {
            res.getString(R.string.rep_text_with, reps)
        }
    }
}