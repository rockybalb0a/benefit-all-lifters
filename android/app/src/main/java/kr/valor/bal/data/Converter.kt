package kr.valor.bal.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kr.valor.bal.data.source.entities.WorkoutSet
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converter {

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

