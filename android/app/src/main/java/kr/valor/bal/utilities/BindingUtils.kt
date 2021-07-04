package kr.valor.bal.utilities

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import kr.valor.bal.R
import kr.valor.bal.adapters.RecyclerviewItemClickListener
import kr.valor.bal.adapters.UpdateWorkoutSetListener
import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.data.WorkoutSchedule
import kr.valor.bal.data.entities.WorkoutSet
import kr.valor.bal.databinding.SetInfoItemBinding
import java.lang.IndexOutOfBoundsException

// Data Binding — lessons learnt
// https://medium.com/androiddevelopers/data-binding-lessons-learnt-4fd16576b719

@BindingAdapter("elapsedTimeOnTracking")
fun TextView.setTrackingTimeText(elapsedTime: Long?) {
    text = elapsedTime?.run {
        elapsedTime.elapsedTimeFormatter()
    } ?: resources.getString(R.string.empty_elapsed_time)
}

@BindingAdapter("workoutName")
fun TextView.setWorkoutName(item: WorkoutDetailAndSets) {
    text = item.workoutDetail.workoutName
}

@BindingAdapter("sets")
fun TextView.setCurrentSets(setItemIndex: Int) {
    text = resources.getString(R.string.sets_text_full_template, setItemIndex + 1)
}

@BindingAdapter("weights")
fun TextView.setWeights(item: WorkoutSet?) {
    val weights = item?.weights?.toInt() ?: 20
    text = weights.toString()
}

@BindingAdapter("weightsWithWeightUnit")
fun TextView.setWeightsWithWeightUnit(item: WorkoutSet?) {
    val weights = item?.weights?.toInt() ?: 20
    text = resources.getString(R.string.weights_text, weights)
}

@BindingAdapter("reps")
fun TextView.setReps(item: WorkoutSet?) {
    val reps = item?.reps ?: 0
    text = if (reps > 1) resources.getString(R.string.reps_text_with, reps) else resources.getString(R.string.rep_text_with, reps)
}


@BindingAdapter("workoutSets", "updateListener")
fun LinearLayout.inflateWorkoutSetsView(item: WorkoutDetailAndSets, itemClickListener: RecyclerviewItemClickListener<*>) {

    if (item.workoutSets.isNotEmpty()) {
        val layoutInflater = LayoutInflater.from(context)

        item.workoutSets.forEachIndexed { index, workoutSet ->

            val setsView = SetInfoItemBinding.inflate(layoutInflater)
            with(setsView) {
                set = workoutSet
                workoutSetNumber.text = resources.getString(R.string.sets_text_simplified_template, index + 1)
                workoutSetReps.text = "${workoutSet.reps}"
                workoutSetRepsUnit.text = if (workoutSet.reps > 1) resources.getString(R.string.reps_text) else resources.getString(R.string.rep_text)
                workoutSetWeights.text = "${workoutSet.weights.toInt()}"
                workoutSetWeightsUnit.text = resources.getString(R.string.default_weights_unit)
                clickListener = itemClickListener as UpdateWorkoutSetListener
                addView(this.root)
            }
        }
    }
}

// Two-way binding

@BindingAdapter("checkedToggleBtnIndex")
fun MaterialButtonToggleGroup.setChecked(checkedIndex: Int) {
    getChildAt(checkedIndex)?.let {
        if (checkedButtonId != it.id) {
            (it as MaterialButton).isChecked = true
        }
    }
}

@InverseBindingAdapter(attribute = "checkedToggleBtnIndex")
fun MaterialButtonToggleGroup.getChecked(): Int = indexOfChild(findViewById(checkedButtonId))

@BindingAdapter("checkedToggleBtnIndexAttrChanged")
fun MaterialButtonToggleGroup.setToggleGroupChangedListener(listener: InverseBindingListener) {
    addOnButtonCheckedListener { _, _, _ -> listener.onChange()}
}
