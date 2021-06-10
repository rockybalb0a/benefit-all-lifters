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

    @Delete
    suspend fun delete(workoutDetail: WorkoutDetail)

    @Insert
    suspend fun insert(workoutSet: WorkoutSet): Long

    @Transaction
    @Query("SELECT * FROM workout_overview WHERE overview_id is :workoutOverviewId")
    fun getWorkoutSchedule(workoutOverviewId: Long): LiveData<WorkoutSchedule>

    @Query("SELECT * FROM workout_overview ORDER BY overview_id DESC")
    fun getAllWorkouts(): LiveData<List<WorkoutOverview>>

    @Query("SELECT * FROM workout_overview ORDER BY overview_id DESC LIMIT 1")
    suspend fun getLatestWorkoutOverview(): WorkoutOverview?

    @Query("SELECT * FROM workout_overview WHERE date is :date")
    suspend fun getWorkoutOverviewByDate(date: LocalDate): WorkoutOverview?

    @Query("SELECT * FROM workout_set WHERE container_id is :detailId ORDER BY container_id DESC LIMIT 1")
    suspend fun getWorkoutSetAssociatedWithWorkoutDetail(detailId: Long): WorkoutSet

    @Delete
    suspend fun deleteWorkoutSet(workoutSet: WorkoutSet)

    @Transaction
    suspend fun deleteWorkoutSetAssociatedWithWorkoutDetail(detailId: Long) {
        val target = getWorkoutSetAssociatedWithWorkoutDetail(detailId)
        deleteWorkoutSet(target)
    }

    @VisibleForTesting
    @Query("SELECT * FROM workout_set WHERE set_id is :workoutSetId")
    suspend fun getWorkoutSet(workoutSetId: Long): WorkoutSet

}