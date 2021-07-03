package kr.valor.bal.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.data.entities.WorkoutSet
import kr.valor.bal.getOrAwaitValue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

//@ExperimentalCoroutinesApi
//@RunWith(AndroidJUnit4::class)
//@SmallTest
//class WorkoutDaoTest {
//
//    private lateinit var appDatabase: AppDatabase
//    private lateinit var workoutDao: WorkoutDao
//
//    @get:Rule
//    var instantExecutorRule = InstantTaskExecutorRule()
//
//    @Before
//    fun setup() {
//        appDatabase = Room.inMemoryDatabaseBuilder(
//            ApplicationProvider.getApplicationContext(),
//            AppDatabase::class.java
//        ).build()
//        workoutDao = appDatabase.workoutDao()
//
//        initData()
//    }
//
//    private fun initData() = runBlocking {
//        val workoutOverview = WorkoutOverview()
//        val workoutOverviewId = workoutDao.insert(workoutOverview)
//        val workoutDetails = listOf(
//            WorkoutDetail(containerId = workoutOverviewId, workoutName = "Bench press"),
//            WorkoutDetail(containerId = workoutOverviewId, workoutName = "Overhead press"),
//            WorkoutDetail(containerId = workoutOverviewId, workoutName = "Squat")
//        )
//        val workoutDetailIds = mutableListOf<Long>()
//        workoutDetails.forEach {
//            val workoutDetailId = workoutDao.insert(it)
//            workoutDetailIds.add(workoutDetailId)
//        }
//        workoutDetailIds.forEach { id ->
//            val workoutSets = listOf(
//                WorkoutSet(containerId = id, reps = 5, weights = 120.0),
//                WorkoutSet(containerId = id, reps = 5, weights = 120.0),
//                WorkoutSet(containerId = id, reps = 5, weights = 120.0)
//            )
//            workoutSets.forEach { workoutSet ->
//                workoutDao.insert(workoutSet)
//            }
//        }
//    }
//
//    @After
//    fun cleanUp() = appDatabase.close()
//
//
//    @Test
//    fun workoutScheduleTransaction_works_asExpected() = runBlockingTest {
//        val loaded = workoutDao.getLatestWorkoutOverview()!!
//
//        val entireSchedule = workoutDao.getWorkoutSchedule(loaded.overviewId)
//
//        assertThat(entireSchedule.getOrAwaitValue().workoutOverview, `is`(loaded))
//        val workoutDetails = entireSchedule.getOrAwaitValue().workoutDetails
//        assertThat(workoutDetails.size, `is`(3))
//        assertThat(workoutDetails[0].workoutDetail.workoutName, `is`("Bench press"))
//        assertThat(workoutDetails[0].workoutSets[0].reps, `is`(5))
//        assertThat(workoutDetails[0].workoutSets[0].weights, `is`(120))
//        assertThat(workoutDetails[0].workoutSets[1].reps, `is`(5))
//        assertThat(workoutDetails[0].workoutSets[1].weights, `is`(120))
//        assertThat(workoutDetails[0].workoutSets[2].reps, `is`(5))
//        assertThat(workoutDetails[0].workoutSets[2].weights, `is`(120))
//
//
//        assertThat(workoutDetails[1].workoutDetail.workoutName, `is`("Overhead press"))
//        assertThat(workoutDetails[1].workoutSets[0].reps, `is`(5))
//        assertThat(workoutDetails[1].workoutSets[0].weights, `is`(120))
//        assertThat(workoutDetails[1].workoutSets[1].reps, `is`(5))
//        assertThat(workoutDetails[1].workoutSets[1].weights, `is`(120))
//        assertThat(workoutDetails[1].workoutSets[2].reps, `is`(5))
//        assertThat(workoutDetails[1].workoutSets[2].weights, `is`(120))
//
//        assertThat(workoutDetails[2].workoutDetail.workoutName, `is`("Squat"))
//        assertThat(workoutDetails[2].workoutSets[0].reps, `is`(5))
//        assertThat(workoutDetails[2].workoutSets[0].weights, `is`(120))
//        assertThat(workoutDetails[2].workoutSets[1].reps, `is`(5))
//        assertThat(workoutDetails[2].workoutSets[1].weights, `is`(120))
//        assertThat(workoutDetails[2].workoutSets[2].reps, `is`(5))
//        assertThat(workoutDetails[2].workoutSets[2].weights, `is`(120))
//
//
//    }
//
//    @Test
//    fun workoutSet_contains_properSetsInfo() = runBlockingTest {
//
//        val platesStack = mutableListOf(25.0, 25.0)
//        val workoutOverview = WorkoutOverview()
//        val workoutOverviewId = workoutDao.insert(workoutOverview)
//        val workoutDetail = WorkoutDetail(containerId = workoutOverviewId, workoutName = "Bench press")
//        val workoutDetailId = workoutDao.insert(workoutDetail)
//
//        val workoutSet = WorkoutSet(containerId = workoutDetailId, reps = 10, weights = 120.0, platesStack = platesStack)
//
//        val id = workoutDao.insert(workoutSet)
//
//        val loaded = workoutDao.getWorkoutSet(id)
//
//        assertThat(loaded.containerId, `is`(workoutSet.containerId))
//        assertThat(loaded.setId, `is`(id))
//        assertThat(loaded.weights, `is`(workoutSet.weights))
//        assertThat(loaded.reps, `is`(workoutSet.reps))
//        assertThat(loaded.platesStack, `is`(workoutSet.platesStack))
//    }
//
//}