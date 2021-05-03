package kr.valor.bal.data.source

import androidx.annotation.VisibleForTesting
import androidx.room.*
import kr.valor.bal.data.source.entities.DailyRoutine
import kr.valor.bal.data.source.entities.WorkoutDetail
import kr.valor.bal.data.source.entities.WorkoutOverview
import java.time.LocalDate

@ExperimentalUnsignedTypes
@Dao
interface RoutineDao {
    @Insert
    suspend fun initDailyRoutine(workoutOverview: WorkoutOverview)

    @Insert
    suspend fun insertWorkoutDetails(vararg workoutDetails: WorkoutDetail)

    @VisibleForTesting
    @Transaction
    @Query("SELECT * FROM workout_overview")
    suspend fun getAllDailyRoutine(): List<DailyRoutine>

    @VisibleForTesting
    @Transaction
    @Query("SELECT * FROM workout_overview WHERE date is :targetDate")
    suspend fun getDailyRoutineByDate(targetDate: LocalDate): DailyRoutine

    @VisibleForTesting
    @Query("SELECT * FROM workout_overview WHERE overview_id is :targetId")
    suspend fun getWorkoutOverviewById(targetId: String): WorkoutOverview

    @VisibleForTesting
    @Query("SELECT * FROM workout_detail WHERE detail_id is :targetId")
    suspend fun getWorkoutDetailById(targetId: String): WorkoutDetail

}