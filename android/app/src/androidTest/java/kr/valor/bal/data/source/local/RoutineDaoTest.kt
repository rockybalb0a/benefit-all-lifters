package kr.valor.bal.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kr.valor.bal.data.source.RoutineDao
import kr.valor.bal.data.source.RoutineDatabase
import kr.valor.bal.data.source.entities.WorkoutDetail
import kr.valor.bal.data.source.entities.WorkoutOverview
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
@SmallTest
@ExperimentalCoroutinesApi
@ExperimentalUnsignedTypes
class RoutineDaoTest {

    private lateinit var database: RoutineDatabase
    private lateinit var dao: RoutineDao

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RoutineDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.routineDao()


    }


    @After
    fun close() = database.close()

    @Test
    fun insertDailyRoutine_getAllDailyRoutines() = runBlockingTest {

        // GIVEN - Today's workout & Yesterday's workout
        val todayDate = LocalDate.now()
        val yesterdayDate = todayDate.minusDays(1)

        val yesterday = WorkoutOverview(date = yesterdayDate, elapsedTime = 3600_000L)
        val squat = WorkoutDetail(superId = yesterday.overviewId, workoutName = "Squat")
        val deadLift = WorkoutDetail(superId = yesterday.overviewId, workoutName = "Dead lift")
        dao.initDailyRoutine(yesterday)
        dao.insertWorkoutDetails(squat, deadLift)

        val today = WorkoutOverview(date = todayDate, elapsedTime = 6200_000L)
        val benchPress = WorkoutDetail(superId = today.overviewId, workoutName = "Bench Press")
        val overHeadPress = WorkoutDetail(superId = today.overviewId, workoutName = "Over Head Press")
        dao.initDailyRoutine(today)
        dao.insertWorkoutDetails(benchPress, overHeadPress)

        // WHEN
        val dailyRoutine = dao.getAllDailyRoutine()

        // THEN
        assertThat(dailyRoutine.size, `is`(2))

        assertThat(dailyRoutine[0].workoutOverview.date, `is`(yesterdayDate))
        assertThat(dailyRoutine[0].workoutOverview.overviewId, `is`(yesterday.overviewId))
        assertThat(dailyRoutine[0].workoutOverview.elapsedTime, `is`(yesterday.elapsedTime))

        assertThat(dailyRoutine[0].workoutDetails.size, `is`(2))
        assertThat(dailyRoutine[0].workoutDetails[0].workoutName, `is`(squat.workoutName))
        assertThat(dailyRoutine[0].workoutDetails[1].workoutName, `is`(deadLift.workoutName))

        assertThat(dailyRoutine[1].workoutOverview.date, `is`(todayDate))
        assertThat(dailyRoutine[1].workoutOverview.overviewId, `is`(today.overviewId))
        assertThat(dailyRoutine[1].workoutOverview.elapsedTime, `is`(today.elapsedTime))

        assertThat(dailyRoutine[1].workoutDetails.size, `is`(2))
        assertThat(dailyRoutine[1].workoutDetails[0].workoutName, `is`(benchPress.workoutName))
        assertThat(dailyRoutine[1].workoutDetails[1].workoutName, `is`(overHeadPress.workoutName))
    }

    @Test
    fun insertRoutines_getByDate() = runBlockingTest {
        // GIVEN - Today's workout & Yesterday's workout
        val todayDate = LocalDate.now()
        val yesterdayDate = todayDate.minusDays(1)

        val yesterday = WorkoutOverview(date = yesterdayDate, elapsedTime = 3600_000L)
        val squat = WorkoutDetail(superId = yesterday.overviewId, workoutName = "Squat")
        val deadLift = WorkoutDetail(superId = yesterday.overviewId, workoutName = "Dead lift")
        dao.initDailyRoutine(yesterday)
        dao.insertWorkoutDetails(squat, deadLift)

        val today = WorkoutOverview(date = todayDate, elapsedTime = 6200_000L)
        val benchPress = WorkoutDetail(superId = today.overviewId, workoutName = "Bench Press")
        val overHeadPress = WorkoutDetail(superId = today.overviewId, workoutName = "Over Head Press")
        dao.initDailyRoutine(today)
        dao.insertWorkoutDetails(benchPress, overHeadPress)

        // WHEN
        val todayRoutine = dao.getDailyRoutineByDate(todayDate)
        val yesterdayRoutine = dao.getDailyRoutineByDate(yesterdayDate)

        // THEN
        assertThat(todayRoutine.workoutOverview, `is`(today))
        assertThat(todayRoutine.workoutDetails.size, `is`(2))
        assertThat(todayRoutine.workoutDetails, `is`(listOf(benchPress, overHeadPress)))

        assertThat(yesterdayRoutine.workoutOverview, `is`(yesterday))
        assertThat(yesterdayRoutine.workoutDetails.size, `is`(2))
        assertThat(yesterdayRoutine.workoutDetails, `is`(listOf(squat, deadLift)))
    }

}