package kr.valor.bal.data.source.local

import androidx.room.*
import kr.valor.bal.data.Routine

@Dao
interface RoutineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: Routine)

    @Update
    suspend fun updateRoutine(routine: Routine): Int

    @Query("SELECT * FROM routines")
    suspend fun getAllRoutines(): List<Routine>

    @Query("SELECT * FROM routines WHERE entryid = :routineId ")
    suspend fun getRoutine(routineId: String): Routine
}