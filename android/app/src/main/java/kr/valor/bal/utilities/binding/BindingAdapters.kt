package kr.valor.bal.utilities.binding

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import kr.valor.bal.R
import kr.valor.bal.adapters.home.HomeAdapter
import kr.valor.bal.adapters.home.VideoAdapter
import kr.valor.bal.adapters.overview.OverviewAdapter
import kr.valor.bal.data.local.user.UserPersonalRecording
import kr.valor.bal.data.local.workout.WorkoutSchedule
import kr.valor.bal.data.local.workout.entities.WorkoutOverview
import kr.valor.bal.data.local.youtube.DatabaseVideo

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
            workoutList[2] -> R.drawable.background_image_bench_press
            workoutList[3] -> R.drawable.background_image_dead_lift
            workoutList[4] -> R.drawable.background_image_press
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

@JvmName("overviewBinding")
@BindingAdapter("overviewBinding")
fun RecyclerView.bindRecyclerView(data: List<WorkoutSchedule>?) {
    val adapter = adapter as OverviewAdapter

    adapter.submitList(data)
}

@JvmName("userRecordBinding")
@BindingAdapter("userRecordBinding")
fun RecyclerView.bindRecyclerView(data: List<UserPersonalRecording>?) {
    val adapter = adapter as HomeAdapter
    adapter.submitList(data)
}

@JvmName("videoBinding")
@BindingAdapter("videoBinding")
fun RecyclerView.bindRecyclerView(data: List<DatabaseVideo>?) {
    val adapter = adapter as VideoAdapter
    adapter.submitList(data)
}

@BindingAdapter("thumbnailUrl")
fun ImageView.bindImage(imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(this.context)
            .load(imgUri)
            .into(this)
    }
}