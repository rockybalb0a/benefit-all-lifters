package kr.valor.bal.data

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kr.valor.bal.data.entities.WorkoutSet
import kr.valor.bal.utilities.DATABASE_NAME
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


//@RunWith(AndroidJUnit4::class)
//@SmallTest
//class MigrationTest {
//
//    private val TEST_DB_NAME = "migration-test"
//
//    private val WEIGHTS_COLUMN_INDEX = 3
//
//    private lateinit var db: SupportSQLiteDatabase
//
//    @get:Rule
//    val helper: MigrationTestHelper = MigrationTestHelper(
//        InstrumentationRegistry.getInstrumentation(),
//        AppDatabase::class.java.canonicalName,
//        FrameworkSQLiteOpenHelperFactory()
//    )
//
//    @After
//    fun closeDb() {
//        helper.closeWhenFinished(db)
//    }
//
//
//    @Test
//    fun beforeMigrating_containsCorrectData() {
//        db = helper.createDatabase(TEST_DB_NAME, 1)
//        val workoutSet = WorkoutSet(containerId = 100L)
//        val values = ContentValues()
//        values.put("container_id", workoutSet.containerId)
//        values.put("reps", workoutSet.reps)
//        values.put("weights", workoutSet.weights)
//
//        val id = db.insert("workout_set", SQLiteDatabase.CONFLICT_REPLACE, values)
//
//        val cursor = db.query("SELECT * FROM workout_set WHERE set_id = ?", arrayOf(id))
//
//        assertThat(cursor, `is`(notNullValue()))
//        assertThat(cursor.moveToFirst(), `is`(true))
//
//        val weightsColumnName = cursor.getColumnName(WEIGHTS_COLUMN_INDEX)
//        assertThat(weightsColumnName, `is`("weights"))
//
//        val weightsColumnType = cursor.getType(WEIGHTS_COLUMN_INDEX)
//        assertThat(weightsColumnType, `is`(Cursor.FIELD_TYPE_INTEGER))
//    }
//
//    // How to properly add indices in database migration for Room?
//    // https://stackoverflow.com/questions/54269102/how-to-properly-add-indices-in-database-migration-for-room
//    @Test
//    fun migrate1To2_containsCorrectData() {
//        db = helper.createDatabase(TEST_DB_NAME, 1)
//        val values = ContentValues()
//        values.put("container_id", 10L)
//        values.put("reps", 10)
//        values.put("weights", 100)
//
//        val id = db.insert("workout_set", SQLiteDatabase.CONFLICT_REPLACE, values)
//
//        db.close()
//
//        db = helper.runMigrationsAndValidate(TEST_DB_NAME, 2, true, MIGRATION_1_2)
//
//        val cursor = db.query("SELECT * FROM workout_set WHERE set_id = ?", arrayOf(id))
//
//        assertThat(cursor, `is`(notNullValue()))
//        assertThat(cursor.moveToFirst(), `is`(true))
//
//        val weightsColumnName = cursor.getColumnName(WEIGHTS_COLUMN_INDEX)
//        assertThat(weightsColumnName, `is`("weights"))
//
//        val weightsColumnType = cursor.getType(WEIGHTS_COLUMN_INDEX)
//        assertThat(weightsColumnType, `is`(Cursor.FIELD_TYPE_FLOAT))
//
//    }
//
//    @Test
//    fun migrate1To2_usingDao() {
//        db = helper.createDatabase(TEST_DB_NAME, 1)
//        val previousWorkoutSet = previousVersion_workoutSetBuilder()
//        var id = 0L
//        helper.createDatabase(TEST_DB_NAME, 1).apply {
//            id = insert("workout_set", SQLiteDatabase.CONFLICT_REPLACE, previousWorkoutSet)
//            close()
//        }
//
//        helper.runMigrationsAndValidate(TEST_DB_NAME, 2, true, MIGRATION_1_2)
//
//        val migratedDatabase = getMigratedRoomDatabase()
//        val migratedWorkoutSet = migratedDatabase.workoutDao().getWorkoutSet(id)
//        assertThat(migratedWorkoutSet.weights, `is`(100.0))
//
//
//    }
//
//
//    private fun getMigratedRoomDatabase(): AppDatabase {
//        return Room.databaseBuilder(
//            ApplicationProvider.getApplicationContext(),
//            AppDatabase::class.java,
//            TEST_DB_NAME
//        )
//            .allowMainThreadQueries()
//            .addMigrations(
//            MIGRATION_1_2
//        ).build().apply {
//            openHelper.writableDatabase
//            close()
//        }
//    }
//
//    private fun previousVersion_workoutSetBuilder(): ContentValues {
//        val workoutSet = ContentValues()
//        workoutSet.put("container_id", 10L)
//        workoutSet.put("reps", 10)
//        workoutSet.put("weights", 100)
//        return workoutSet
//    }
//
//}