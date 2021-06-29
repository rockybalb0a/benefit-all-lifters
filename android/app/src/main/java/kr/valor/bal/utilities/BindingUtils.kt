package kr.valor.bal.utilities

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import kr.valor.bal.R
import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.adapters.listeners.ScheduleSetListener
import kr.valor.bal.data.WorkoutSchedule
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.data.entities.WorkoutSet
import kr.valor.bal.databinding.SetInfoItemBinding
import java.lang.IndexOutOfBoundsException
import kotlin.reflect.KClass

@BindingAdapter("dateString")
fun TextView.setDateFormatted(item: WorkoutSchedule) {
    text = item.workoutOverview.localDateFormatter()
}


@BindingAdapter("elapsedTime")
fun TextView.setElapsedTimeFormatted(item: WorkoutSchedule?) {
    item?.let {
        text = when(it.workoutOverview.trackingStatus) {
            TrackingStatus.DONE -> getElapsedTimeFormatted(resources.getString(R.string.empty_elapsed_time), item)
            else -> resources.getString(R.string.empty_elapsed_time)
        }
    } ?: resources.getString(R.string.empty_elapsed_time)
}

@BindingAdapter("elapsedTimeOnTracking")
fun TextView.setTrackingTimeText(elapsedTime: Long?) {
    text = elapsedTime?.run {
        elapsedTime.elapsedTimeFormatter()
    } ?: resources.getString(R.string.empty_elapsed_time)
}


private fun getElapsedTimeFormatted(default: String ,item: WorkoutSchedule?): String {
    return item?.run {
        this.workoutOverview.elapsedTimeFormatter()
    } ?: default
}


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

@BindingAdapter("mainLifting")
fun TextView.setMainLiftingCategoryText(item: WorkoutSchedule) {
    val workouts = item.workoutDetails
    if (workouts.size >= 2) {
        text = resources.getString(
            R.string.main_lifting_more,
            workouts.component1().workoutDetail.workoutName,
            workouts.component2().workoutDetail.workoutName
        )
    } else {
        text = try {
            resources.getString(
                R.string.main_lifting_one,
                workouts.component1().workoutDetail.workoutName
            )
        } catch (e: IndexOutOfBoundsException) {
            resources.getString(R.string.main_lifting_empty)
        }
    }
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


@BindingAdapter("workoutSets", "setListener")
fun LinearLayout.inflateWorkoutSetsView(item: WorkoutDetailAndSets, itemClickListener: ScheduleSetListener) {

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
                clickListener = itemClickListener
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
