package kr.valor.bal.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateConverter {

    @TypeConverter
    fun fromColumn(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it, DateTimeFormatter.ISO_DATE) }
    }

    @TypeConverter
    fun toColumn(date: LocalDate?): String? {
        return date?.format(DateTimeFormatter.ISO_DATE)
    }

}

