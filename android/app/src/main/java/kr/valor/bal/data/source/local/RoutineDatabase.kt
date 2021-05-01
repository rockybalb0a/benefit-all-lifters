package kr.valor.bal.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kr.valor.bal.data.LocalDateConverter
import kr.valor.bal.data.Routine

@Database(entities = [Routine::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateConverter::class)
abstract class RoutineDatabase: RoomDatabase() {
    abstract fun routineDao(): RoutineDao
}
