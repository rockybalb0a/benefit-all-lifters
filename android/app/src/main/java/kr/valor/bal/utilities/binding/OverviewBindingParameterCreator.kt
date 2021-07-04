package kr.valor.bal.utilities.binding

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import kr.valor.bal.R
import kr.valor.bal.data.WorkoutSchedule
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.utilities.TrackingStatus
import kr.valor.bal.utilities.elapsedTimeFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.*


object OverviewBindingParameterCreator {

    private const val SIMPLIFY_TO_TODAY = 0L

    private const val SIMPLIFY_TO_YESTERDAY = 1L

    fun getLocalDateFormattedString(localDate: LocalDate, context: Context): String =
        localDate.localDateFormatter(context)

    fun getElapsedTimeFormattedString(item: WorkoutSchedule?, context: Context): String {
        val res = context.resources
        return item?.run {
            when(workoutOverview.trackingStatus) {
                TrackingStatus.DONE -> workoutOverview.elapsedTimeFormatter()
                else -> res.getString(R.string.empty_elapsed_time)
            }
        } ?: res.getString(R.string.empty_elapsed_time)
    }


    fun getMainLiftingCategoryText(item: WorkoutSchedule, context: Context): String {

        val workouts = item.workoutDetails
        val res = context.resources

        return if (workouts.size >= 2) {
            res.getString(
                R.string.main_lifting_more,
                workouts.component1().workoutDetail.workoutName,
                workouts.component2().workoutDetail.workoutName
            )
        } else {
            try {
                res.getString(
                    R.string.main_lifting_one,
                    workouts.component1().workoutDetail.workoutName
                )
            } catch (e: IndexOutOfBoundsException) {
                res.getString(R.string.main_lifting_empty)
            }
        }
    }

    private fun LocalDate.localDateFormatter(context: Context): String = when {
        isSimplifiable(SIMPLIFY_TO_TODAY) ->
            context.resources.getString(R.string.today_workout_overview_title)
        isSimplifiable(SIMPLIFY_TO_YESTERDAY) ->
            context.resources.getString(R.string.yesterday_workout_overview_title)
        else -> getFullDateString()
    }


    private fun WorkoutOverview.elapsedTimeFormatter(): String {
        val durationTimeMilli = with(this) {
            endTimeMilli - startTimeMilli
        }
        return durationTimeMilli.elapsedTimeFormatter()
    }

    private fun LocalDate.isSimplifiable(targetDayOfWeek: Long): Boolean =
        (dayOfYear == LocalDate.now().minusDays(targetDayOfWeek).dayOfYear) &&
                (dayOfMonth == LocalDate.now().minusDays(targetDayOfWeek).dayOfMonth)


    private fun LocalDate.getFullDateString(): String =
        dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.US) +
                ", " + format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))

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