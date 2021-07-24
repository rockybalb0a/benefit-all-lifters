package kr.valor.bal.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.data.entities.WorkoutSet
import java.time.LocalDate

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insert(workoutOverview: WorkoutOverview): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(workoutOverview: WorkoutOverview)

    @Insert
    suspend fun insert(workoutDetail: WorkoutDetail): Long

    @Delete
    suspend fun delete(workoutDetail: WorkoutDetail)

    @Insert
    suspend fun insert(workoutSet: WorkoutSet): Long

    @Delete
    suspend fun delete(workoutSet: WorkoutSet)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(workoutSet: WorkoutSet)

    @Query("SELECT * FROM workout_set WHERE set_id is :workoutSetId")
    fun getWorkoutSet(workoutSetId: Long): LiveData<WorkoutSet>

    @Transaction
    @Query("SELECT * FROM workout_overview WHERE overview_id is :workoutOverviewId")
    fun getWorkoutSchedule(workoutOverviewId: Long): LiveData<WorkoutSchedule>

    @Transaction
    @Query("SELECT * FROM workout_overview WHERE overview_id is :workoutOverviewId")
    suspend fun getNoneNullWorkoutSchedule(workoutOverviewId: Long): WorkoutSchedule

    @Transaction
    @Query("SELECT * FROM workout_overview ORDER BY overview_id DESC")
    fun getAllWorkoutSchedule(): LiveData<List<WorkoutSchedule>>

    @Query("SELECT * FROM workout_overview ORDER BY overview_id DESC")
    fun getAllWorkouts(): LiveData<List<WorkoutOverview>>

    @Query("SELECT * FROM workout_overview ORDER BY overview_id DESC LIMIT 1")
    suspend fun getLatestWorkoutOverview(): WorkoutOverview

    @Query("SELECT * FROM workout_overview WHERE date is :date")
    suspend fun getWorkoutOverviewByDate(date: LocalDate): WorkoutOverview?

    @Query("SELECT * FROM workout_overview WHERE overview_id is :overviewId")
    suspend fun getNoneNullWorkoutOverviewById(overviewId: Long): WorkoutOverview

    @Query("SELECT * FROM workout_set WHERE container_id is :detailId ORDER BY set_id DESC LIMIT 1")
    suspend fun getWorkoutSetAssociatedWithWorkoutDetail(detailId: Long): WorkoutSet

    @Query("SELECT * FROM workout_set WHERE container_id is :detailId ORDER BY set_id DESC LIMIT 1")
    suspend fun getLatestWorkoutSetByWorkoutDetailId(detailId: Long): WorkoutSet?

    @Transaction
    suspend fun deleteWorkoutSetAssociatedWithWorkoutDetail(detailId: Long) {
        val target = getWorkoutSetAssociatedWithWorkoutDetail(detailId)
        delete(target)
    }

}