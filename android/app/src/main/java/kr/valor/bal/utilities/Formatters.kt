package kr.valor.bal.utilities

import kr.valor.bal.data.entities.WorkoutOverview
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale.US

private const val SIMPLIFY_TO_TODAY = 0L

private const val SIMPLIFY_TO_YESTERDAY = 1L


fun WorkoutOverview.localDateFormatter(): String = when {
    isSimplifiable(SIMPLIFY_TO_TODAY) -> TODAY_WORKOUT_OVERVIEW_TITLE
    isSimplifiable(SIMPLIFY_TO_YESTERDAY) -> YESTERDAY_WORKOUT_OVERVIEW_TITLE
    else -> getFullDateString()
}


fun WorkoutOverview.elapsedTimeFormatter(): String {
    val durationTimeMilli = with(this) {
        endTimeMilli - startTimeMilli
    }
    return durationTimeMilli.elapsedTimeFormatter()
}

fun Long.elapsedTimeFormatter(): String {
    val hour = (this / (1000 * 60 * 60)) % 24
    val min = (this / (1000 * 60)) % 60
    val sec = (this / 1000) % 60

    return String.format("%02d:%02d:%02d", hour, min, sec)
}


private fun WorkoutOverview.isSimplifiable(daysToSubtract: Long): Boolean {
    return date.dayOfYear == LocalDate.now().minusDays(daysToSubtract).dayOfYear &&
            date.dayOfMonth == LocalDate.now().minusDays(daysToSubtract).dayOfMonth
}

private fun WorkoutOverview.getFullDateString(): String =
    date.dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, US) +
            ", " + date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))

