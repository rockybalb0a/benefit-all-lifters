package kr.valor.bal.data

import androidx.room.TypeConverter
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

    companion object {
        private const val DELIMITER = " "
    }



//    @TypeConverter
//    fun workoutSetsToColumn(setsList: List<WorkoutSet>?): String? {
//        return setsList?.let { Json.encodeToString(it) }
//    }
//
//    @TypeConverter
//    fun columnToWorkoutSets(json: String?): List<WorkoutSet>? {
//        return json?.let { Json.decodeFromString(it) }
//    }

}

