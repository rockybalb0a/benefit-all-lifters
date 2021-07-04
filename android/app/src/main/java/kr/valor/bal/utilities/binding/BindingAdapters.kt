package kr.valor.bal.utilities.binding

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
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
import kr.valor.bal.databinding.SetInfoItemBinding


@BindingAdapter("thumbnailImage")
fun ImageView.setThumbnailImage(item: WorkoutSchedule?) {
    item?.let {
        setImageResource(when (it.workoutOverview.overviewId.toInt() % 7) {
            0 -> R.drawable.thumbnail_background_1
            1 -> R.drawable.thumbnail_background_2
            2 -> R.drawable.thumbnail_background_3
            3 -> R.drawable.thumbnail_background_4
            4 -> R.drawable.thumbnail_background_5
            5 -> R.drawable.thumbnail_background_6
            else -> R.drawable.thumbnail_background_7
        })
    } ?: setImageResource(R.drawable.thumbnail_background_7)
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
