package kr.valor.bal.data

import androidx.annotation.VisibleForTesting
import androidx.room.*
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview
import java.time.LocalDate

@Dao
interface WorkoutDao {
    @Insert
    suspend fun initWorkoutOverview(workoutOverview: WorkoutOverview)

    @Insert
    suspend fun insertWorkoutDetails(vararg workoutDetails: WorkoutDetail)

    @VisibleForTesting
    @Transaction
    @Query("SELECT * FROM workout_overview")
    suspend fun getAllWorkoutOverviewAndDetails(): List<WorkoutOverviewAndDetails>

    @VisibleForTesting
    @Transaction
    @Query("SELECT * FROM workout_overview WHERE overview_id is :targetId")
    suspend fun getWorkoutOverviewAndDetails(targetId: String): WorkoutOverviewAndDetails
}