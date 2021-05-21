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
    suspend fun initWorkoutOverview(workoutOverview: WorkoutOverview)

    @Insert
    suspend fun insertWorkoutDetails(vararg workoutDetails: WorkoutDetail)

    @Insert
    suspend fun insertAll(workoutOverviewList: List<WorkoutOverview>)

    @Query("SELECT * FROM workout_overview")
    fun getAllWorkouts(): LiveData<List<WorkoutOverview>>

    @VisibleForTesting
    @Transaction
    @Query("SELECT * FROM workout_overview")
    suspend fun getAllWorkoutOverviewAndDetails(): List<WorkoutOverviewAndDetails>

    @VisibleForTesting
    @Transaction
    @Query("SELECT * FROM workout_overview WHERE overview_id is :targetId")
    suspend fun getWorkoutOverviewAndDetails(targetId: String): WorkoutOverviewAndDetails
}