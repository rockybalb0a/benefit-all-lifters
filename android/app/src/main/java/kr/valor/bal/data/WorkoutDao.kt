package kr.valor.bal.data

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.data.entities.WorkoutSet
import java.time.LocalDate

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insert(workoutOverview: WorkoutOverview): Long

    @Insert
    suspend fun insert(workoutDetail: WorkoutDetail): Long

    @Insert
    suspend fun insert(workoutSet: WorkoutSet): Long

    @Transaction
    @Query("SELECT * FROM workout_overview WHERE overview_id is :workoutOverviewId")
    fun getWorkoutSchedule(workoutOverviewId: Long): LiveData<WorkoutSchedule>

    @Query("SELECT * FROM workout_overview ORDER BY overview_id DESC")
    fun getAllWorkouts(): LiveData<List<WorkoutOverview>>

    @Query("SELECT * FROM workout_overview ORDER BY overview_id DESC LIMIT 1")
    suspend fun getLatestWorkoutOverview(): WorkoutOverview?

}