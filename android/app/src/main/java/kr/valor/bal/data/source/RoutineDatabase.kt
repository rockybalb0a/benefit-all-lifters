package kr.valor.bal.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kr.valor.bal.data.Converter
import kr.valor.bal.data.source.entities.WorkoutDetail
import kr.valor.bal.data.source.entities.WorkoutOverview

@ExperimentalUnsignedTypes
@Database(entities = [WorkoutOverview::class, WorkoutDetail::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class RoutineDatabase: RoomDatabase() {
    abstract fun routineDao(): RoutineDao
}
