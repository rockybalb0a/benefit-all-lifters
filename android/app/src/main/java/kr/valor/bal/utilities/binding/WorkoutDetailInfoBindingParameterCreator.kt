package kr.valor.bal.utilities.binding

import android.content.Context
import kr.valor.bal.R
import kr.valor.bal.data.local.workout.WorkoutDetailAndSets
import kr.valor.bal.data.local.workout.entities.WorkoutSet

object WorkoutDetailInfoBindingParameterCreator {

    private const val EMPTY_WEIGHTS = "20"

    private const val EMPTY_REPS = "0"

    fun getWorkoutNameString(item: WorkoutDetailAndSets): String =
        item.workoutDetail.workoutName

    fun getWeightsWithWeightUnitString(item: WorkoutSet?, context: Context): String {
        val weights = item?.weights?.toInt() ?: 20
        return context.resources.getString(R.string.weights_text, weights)
    }

    fun getWeightsWithoutWeightUnit(item: WorkoutSet?): String =
        item?.weights?.toInt()?.toString() ?: EMPTY_WEIGHTS

    fun getWeightsUnit(context: Context): String =
        context.resources.getString(R.string.default_weights_unit)

    fun getCurrentSetsNumberString(idx: Int, context: Context): String =
        context.resources.getString(R.string.sets_text_full_template, idx + 1)

    fun getCurrentSetsNumberSimplifiedString(idx: Int, context: Context): String =
        context.resources.getString(R.string.sets_text_simplified_template, idx + 1)

    fun getRepsWithUnitString(item: WorkoutSet?, context: Context): String {
        val res = context.resources
        val reps = item?.reps ?: 0
        return if (reps > 1) {
            res.getString(R.string.reps_text_with, reps)
        } else {
            res.getString(R.string.rep_text_with, reps)
        }
    }

    fun getRepsWithoutUnitString(item: WorkoutSet?): String =
        item?.reps?.toString() ?: EMPTY_REPS

    fun getRepsUnit(item: WorkoutSet?, context: Context): String {
        val res = context.resources
        return item?.let {
            if (it.reps > 1) res.getString(R.string.reps_text) else res.getString(R.string.rep_text)
        } ?: res.getString(R.string.rep_text)
    }

}