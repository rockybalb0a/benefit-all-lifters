package kr.valor.bal.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview
import javax.inject.Singleton

@Database(entities = [WorkoutOverview::class, WorkoutDetail::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}
