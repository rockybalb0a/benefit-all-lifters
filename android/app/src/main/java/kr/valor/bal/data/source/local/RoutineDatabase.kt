package kr.valor.bal.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.valor.bal.data.Routine

@Database(entities = [Routine::class], version = 1, exportSchema = false)
abstract class RoutineDatabase: RoomDatabase() {
    abstract fun routineDao(): RoutineDao
}
