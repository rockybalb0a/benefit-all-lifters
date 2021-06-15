package kr.valor.bal.utilities

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.LiveData
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import kr.valor.bal.R
import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.data.entities.WorkoutOverview
import kotlin.random.Random
import kr.valor.bal.adapters.listeners.ScheduleSetListener
import kr.valor.bal.data.entities.WorkoutSet
import kr.valor.bal.databinding.ListItemSetBinding
import kr.valor.bal.ui.schedule.view.Plates
import kr.valor.bal.ui.schedule.view.PlatesView

@BindingAdapter("dateString")
fun TextView.setDateFormatted(item: WorkoutOverview) {
    text = item.localDateFormatter()
}

@BindingAdapter("elapsedTimeString")
fun TextView.setElapsedTimeFormatted(item: WorkoutOverview) {
    text = item.elapsedTimeFormatter()
}

@BindingAdapter("thumbnailImage")
fun ImageView.setThumbnailImage(item: WorkoutOverview) {
    setImageResource(when (Random.nextLong(0L, 6L)) {
        0L -> R.drawable.thumbnail_background_1
        1L -> R.drawable.thumbnail_background_2
        2L -> R.drawable.thumbnail_background_3
        3L -> R.drawable.thumbnail_background_4
        4L -> R.drawable.thumbnail_background_5
        else -> R.drawable.thumbnail_background_6
    })
}

@BindingAdapter("workoutName")
fun TextView.setWorkoutName(item: WorkoutDetailAndSets) {
    text = item.workoutDetail.workoutName
}

@SuppressLint("SetTextI18n")
@BindingAdapter("workoutSets", "setListener")
fun LinearLayout.inflateWorkoutSetsView(item: WorkoutDetailAndSets, clickListener: ScheduleSetListener) {

    if (item.workoutSets.isNotEmpty()) {
        val layoutInflater = LayoutInflater.from(context)

        item.workoutSets.forEachIndexed { index, workoutSet ->

            val setsView = ListItemSetBinding.inflate(layoutInflater)
            setsView.set = workoutSet
            setsView.workoutSetNumber.text = "# ${index+1}"
            setsView.workoutSetReps.text = "${workoutSet.reps}"
            setsView.workoutSetRepsUnit.text = if (workoutSet.reps > 1) "reps" else "rep"
            setsView.workoutSetWeights.text = "${workoutSet.weights.toInt()}"
            setsView.workoutSetWeightsUnit.text = "kg"
            setsView.clickListener = clickListener
            addView(setsView.root)
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