package kr.valor.bal.data.local

import androidx.room.TypeConverter
import kr.valor.bal.data.local.user.UserPersonalRecording
import kr.valor.bal.utilities.TrackingStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AppDatabaseConverters {

    @TypeConverter
    fun columnToLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it, DateTimeFormatter.ISO_DATE) }
    }

    @TypeConverter
    fun localDateToColumn(date: LocalDate?): String? {
        return date?.format(DateTimeFormatter.ISO_DATE)
    }

    @TypeConverter
    fun platesStackToColumn(value: MutableList<Double>) : String {
        return value.let {
            val result = StringBuilder()

            it.forEachIndexed { index, item ->
                result.append(if (index == it.size - 1) {
                    item.toString()
                } else {
                    item.toString() + DELIMITER
                })
            }
            result.toString()
        }
    }

    @TypeConverter
    fun columnToPlatesStack(data: String): MutableList<Double> {
        return if (data.isEmpty()) mutableListOf() else {
            data.let {
                val platesWeightsList = mutableListOf<Double>()

                it.split(DELIMITER).forEach { weights ->
                    platesWeightsList.add(weights.toDouble())
                }

                platesWeightsList
            }
        }
    }

    @TypeConverter
    fun userPrRecordingListToColumn(value: MutableList<UserPersonalRecording>): String {
        return value.let {
            val result = StringBuilder()

            it.forEachIndexed { index, item ->
                result.append(if (index == it.size - 1) {
                    item.workoutName + PR_ITEM_DELIMITER + item.weights + PR_ITEM_DELIMITER + item.reps
                } else {
                    item.workoutName + PR_ITEM_DELIMITER + item.weights + PR_ITEM_DELIMITER + item.reps  + PR_DELIMITER
                })
            }

            result.toString()
        }
    }

    @TypeConverter
    fun columnToUserPrRecordingList(data: String): MutableList<UserPersonalRecording> {
        return if (data.isEmpty()) mutableListOf() else {
            data.let {
                val returnList = mutableListOf<UserPersonalRecording>()
                it.split(PR_DELIMITER).forEach { eachItem ->
                    val item = eachItem.split(PR_ITEM_DELIMITER)
                    returnList.add(UserPersonalRecording(item[0], item[1].toDouble(), item[2].toInt()))
                }
                returnList
            }
        }
    }

    @TypeConverter
    fun columnToTrackingStatus(data: Int): TrackingStatus {
        return when(data) {
            1 -> TrackingStatus.TRACKING
            2 -> TrackingStatus.PAUSE
            3 -> TrackingStatus.DONE
            else -> TrackingStatus.NONE
        }
    }

    @TypeConverter
    fun trackingStatusToColumn(status: TrackingStatus): Int {
        return when (status) {
            TrackingStatus.NONE -> 0
            TrackingStatus.TRACKING -> 1
            TrackingStatus.PAUSE -> 2
            TrackingStatus.DONE -> 3
        }
    }

    companion object {
        private const val DELIMITER = " "
        private const val PR_ITEM_DELIMITER = "-"
        private const val PR_DELIMITER = "/"
    }


}

