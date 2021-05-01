package kr.valor.bal.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kr.valor.bal.data.Routine
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
    fun insertRoutine_getRoutineById() = runBlockingTest {
        val routine = Routine()
        dao.insertRoutine(routine)

        val loaded = dao.getRoutine(routine.id)

        assertThat(loaded.id, `is`(routine.id))
        assertThat(loaded.date, `is`(routine.date))
    }

    @Test
    fun insertRoutine_typeConverter_correctlyWorks() = runBlockingTest {
        val currentTime = LocalDate.now()
        val routine = Routine(date = currentTime)
        dao.insertRoutine(routine)

        val loaded = dao.getRoutine(routine.id)

        assertThat(loaded.date, `is`(currentTime))
    }

}