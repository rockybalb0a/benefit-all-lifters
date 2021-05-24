package kr.valor.bal.data

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview
import java.time.LocalDate

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insertWorkoutOverview(workoutOverview: WorkoutOverview)

    @Insert
    suspend fun insertWorkoutDetail(workoutDetail: WorkoutDetail)

    @Query("SELECT * FROM workout_overview")
    fun getAllWorkouts(): LiveData<List<WorkoutOverview>>

    @Query("SELECT * FROM workout_detail")
    fun getAllWorkoutDetails(): LiveData<List<WorkoutDetail>>

    @Query("SELECT * FROM workout_overview ORDER BY overview_id DESC LIMIT 1")
    suspend fun getLatestWorkoutOverview(): WorkoutOverview?











    @VisibleForTesting
    @Transaction
    @Query("SELECT * FROM workout_overview")
    suspend fun getAllWorkoutOverviewAndDetails(): List<WorkoutOverviewAndDetails>

    @VisibleForTesting
    @Transaction
    @Query("SELECT * FROM workout_overview WHERE overview_id is :targetId")
    suspend fun getWorkoutOverviewAndDetails(targetId: String): WorkoutOverviewAndDetails
}