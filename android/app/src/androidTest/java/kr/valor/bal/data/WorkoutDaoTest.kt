package kr.valor.bal.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kr.valor.bal.data.entities.WorkoutOverview
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

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
    fun insertWorkoutOverview_andGetByLocalDate_isEqual() = runBlockingTest {
        val workoutOverview = WorkoutOverview(startTimeMilli = 100L, endTimeMilli = 300L)

        workoutDao.insertWorkoutOverview(workoutOverview)
        val loaded = workoutDao.getLatestWorkoutOverview() as WorkoutOverview

        assertThat(loaded.startTimeMilli, `is`(100L))
        assertThat(loaded.endTimeMilli, `is`(300L))

    }

}