package kr.valor.bal.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.data.entities.WorkoutSet
import javax.inject.Singleton

@Database(entities = [WorkoutOverview::class, WorkoutDetail::class, WorkoutSet::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}

val MIGRATION_1_2 = object : Migration(1,2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // TODO
    }
}
