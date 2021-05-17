package kr.valor.bal.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class WorkoutDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var workoutDao: WorkoutDao

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        workoutDao = appDatabase.workoutDao()
    }

    @After
    fun cleanUp() = appDatabase.close()

    @Test
    fun insertWorkoutInfo_andGetById_equals() = runBlockingTest {
        val today = WorkoutOverview()
        val chestWorkout = WorkoutDetail(
            containerId = today.overviewId,
            workoutName = "Bench press",
            setsDetail = listOf(
                WorkoutSet(140, 5),
                WorkoutSet(140, 5),
                WorkoutSet(140, 5)
            )
        )
        workoutDao.initWorkoutOverview(today)
        workoutDao.insertWorkoutDetails(chestWorkout)

        val loaded =
            workoutDao.getWorkoutOverviewAndDetails(today.overviewId)

        assertThat(loaded.workoutOverview, `is`(today))
        assertThat(loaded.workoutDetails, `is`(listOf(chestWorkout)))
    }

}