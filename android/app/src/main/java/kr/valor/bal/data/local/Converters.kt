package kr.valor.bal.data.local

import androidx.room.TypeConverter
import kr.valor.bal.utilities.TrackingStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {

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
            var result = String()

            it.forEachIndexed { index, item ->
                result += if (index == it.size - 1) {
                    item.toString()
                } else {
                    item.toString() + DELIMITER
                }
            }
            result
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
    }


}

