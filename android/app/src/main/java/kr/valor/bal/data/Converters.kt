package kr.valor.bal.data

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
    fun workoutSetsToColumn(setsList: List<WorkoutSet>?): String? {
        return setsList?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun columnToWorkoutSets(json: String?): List<WorkoutSet>? {
        return json?.let { Json.decodeFromString(it) }
    }


}

