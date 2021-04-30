package kr.valor.bal.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "routines")
data class Routine (
    @PrimaryKey
    @ColumnInfo(name = "entryid")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "date")
    var date: String = ""
)
