package kr.valor.bal.utilities.binding

import android.content.Context
import kr.valor.bal.R
import kr.valor.bal.data.local.user.UserPersonalRecording

object UserPrBindingParameterCreator {

    fun getWorkoutName(item: UserPersonalRecording?, context: Context): String {
        return item?.workoutName ?: context.resources.getStringArray(R.array.exercise_list)[0]
    }

    fun getWeights(item: UserPersonalRecording?, context: Context): String? {
        return item?.weights?.getString(context)
    }

    fun getMaxLift(item: UserPersonalRecording?, context: Context): String? {
        return item?.let {
            if (it.weights.isSimplifiable()) {
                context.resources.getString(R.string.max_lift_body_weight_decimal, it.weights.toInt(), it.reps)
            } else {
                context.resources.getString(R.string.max_lift_body_weight_float, it.weights, it.reps)
            }
        }
    }


    // O'Conner formula
    fun getEstimated1RM(item: UserPersonalRecording?, context: Context): String? {
        return item?.let {
            if (it.reps == 1) {
                it.weights.getString(context)
            } else {
                val weights = item.weights
                val reps = item.reps
                val estimated = weights * (1 + 0.025 * reps)
                context.resources.getString(R.string.weights_integer_text, estimated.toInt())
            }
        }
    }

    private fun Double.getString(context: Context): String {
        return if (this.isSimplifiable()) {
            context.resources.getString(R.string.weights_integer_text, this.toInt())
        } else {
            context.resources.getString(R.string.weights_floating_text, this)
        }
    }

    private fun Double.isSimplifiable(): Boolean {
        return this % 1.0 == 0.0
    }
}