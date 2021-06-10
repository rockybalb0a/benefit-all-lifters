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

@Database(entities = [WorkoutOverview::class, WorkoutDetail::class, WorkoutSet::class], version = 2, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}

val MIGRATION_1_2 = object : Migration(1,2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE workout_set_new (" +
                    "set_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "container_id INTEGER NOT NULL, " +
                    "reps INTEGER NOT NULL, " +
                    "weights REAL NOT NULL, " +
                    "FOREIGN KEY(container_id) REFERENCES " +
                    "workout_detail(detail_id) ON UPDATE NO ACTION ON DELETE CASCADE)"
        )

        database.execSQL(
            "INSERT INTO workout_set_new (set_id, container_id, reps, weights) SELECT * FROM workout_set"
        )

        database.execSQL("DROP TABLE workout_set")

        database.execSQL("ALTER TABLE workout_set_new RENAME TO workout_set")

        database.execSQL("CREATE INDEX index_workout_set_container_id ON workout_set (container_id)")
    }
}
