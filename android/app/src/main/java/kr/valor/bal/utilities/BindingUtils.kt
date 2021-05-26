package kr.valor.bal.utilities

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import kr.valor.bal.R
import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview
import kotlin.random.Random

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