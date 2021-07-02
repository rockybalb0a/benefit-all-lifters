package kr.valor.bal.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.flow.Flow
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.data.entities.WorkoutSet
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRepository @Inject constructor(private val workoutDao: WorkoutDao) {

    fun getAllWorkouts(): LiveData<List<WorkoutOverview>> = workoutDao.getAllWorkouts()

    fun getWorkoutOverviewOfToday(init: (WorkoutOverview) -> Unit) = liveData<WorkoutOverview> {
        val currentDate = LocalDate.now()
        val currentWorkoutOverview = workoutDao.getWorkoutOverviewByDate(currentDate)
            ?: run {
                val newWorkoutOverview = WorkoutOverview()
                workoutDao.insert(newWorkoutOverview)
                workoutDao.getLatestWorkoutOverview()
            }
        init(currentWorkoutOverview)
        emit(currentWorkoutOverview)
    }

    fun getWorkoutScheduleByWorkoutOverviewId(id: Long): LiveData<WorkoutSchedule> {
        return workoutDao.getWorkoutSchedule(id)
    }

    suspend fun addWorkoutSet(workoutSet: WorkoutSet) {
        workoutDao.insert(workoutSet)
    }

    suspend fun removeWorkoutSet(detailId: Long) {
        workoutDao.deleteWorkoutSetAssociatedWithWorkoutDetail(detailId)
    }

    suspend fun getLatestWorkoutSetInfo(workoutSet: WorkoutSet): WorkoutSet? {
        return workoutDao.getLatestWorkoutSetByWorkoutDetailId(workoutSet.containerId)
    }

    suspend fun addWorkoutDetail(workoutDetail: WorkoutDetail) {
        workoutDao.insert(workoutDetail)
    }

    suspend fun dropWorkoutDetail(workoutDetail: WorkoutDetail) {
        workoutDao.delete(workoutDetail)
    }

    suspend fun updateWorkoutOverview(workoutOverview: WorkoutOverview) {
        workoutDao.update(workoutOverview)
    }

}