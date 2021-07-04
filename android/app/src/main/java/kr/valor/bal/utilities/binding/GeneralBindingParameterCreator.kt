package kr.valor.bal.utilities.binding

import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.data.entities.WorkoutSet

object GeneralBindingParameterCreator {

    fun getWorkoutNameString(item: WorkoutDetailAndSets): String =
        item.workoutDetail.workoutName

    fun getWeightsWithoutWeightUnit(item: WorkoutSet?): String {
        val weights = item?.weights?.toInt() ?: 20
        return weights.toString()
    }

}