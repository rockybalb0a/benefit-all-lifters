package kr.valor.bal.utilities.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import kr.valor.bal.R
import kr.valor.bal.adapters.ViewHolder
import kr.valor.bal.adapters.home.HomeAdapter
import kr.valor.bal.adapters.overview.OverviewAdapter
import kr.valor.bal.data.WorkoutSchedule
import kr.valor.bal.data.entities.WorkoutOverview

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

@BindingAdapter("timerImage")
fun ImageView.setTimerBackgroundImage(item: WorkoutOverview?) {
    item?.let {
        setImageResource(when (it.overviewId.toInt() % 7) {
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

@BindingAdapter("headerImage")
fun ImageView.setHeaderImage(workoutName: String?) {
    val workoutList = resources.getStringArray(R.array.exercise_list)
    workoutName?.let {
        setImageResource(when (it) {
            workoutList[0] -> R.drawable.background_image_squat
            workoutList[1] -> R.drawable.background_image_front_squat
            workoutList[2] -> R.drawable.background_image_dead_lift
            workoutList[3] -> R.drawable.background_image_press
            workoutList[4] -> R.drawable.background_image_bench_press
            workoutList[5] -> R.drawable.background_image_barbell_row
            else -> R.drawable.background_image_default
        })
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

@BindingAdapter("overviewBinding")
fun RecyclerView.bindRecyclerView(data: List<WorkoutSchedule>?) {
    val adapter = adapter as OverviewAdapter

    adapter.submitList(data)
}