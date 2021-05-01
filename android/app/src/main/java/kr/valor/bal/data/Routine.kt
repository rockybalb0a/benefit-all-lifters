package kr.valor.bal.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "routines")
data class Routine (
    @PrimaryKey
    @ColumnInfo(name = "entryid")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "date")
    var date: LocalDate = LocalDate.now()
)




