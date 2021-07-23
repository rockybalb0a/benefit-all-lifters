package kr.valor.bal.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.data.entities.WorkoutSet
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRepository @Inject constructor(private val workoutDao: WorkoutDao) {

    fun getAllWorkoutSchedule(): LiveData<List<WorkoutSchedule>> = workoutDao.getAllWorkoutSchedule()

    fun getExistWorkoutOverviewById(overviewId: Long) = liveData<WorkoutOverview> {
        emit(workoutDao.getNoneNullWorkoutOverviewById(overviewId))
    }

    fun getWorkoutOverviewOfToday(init: (WorkoutOverview) -> Unit) = liveData {
        val currentDate = LocalDate.now()
        val currentWorkoutOverview = workoutDao.getWorkoutOverviewByDate(currentDate)
            ?: run {
                createWorkoutOverviewIfNotExist()
            }
        init(currentWorkoutOverview)
        emit(currentWorkoutOverview)
    }

    fun getWorkoutScheduleByWorkoutOverviewId(id: Long): LiveData<WorkoutSchedule> {
        return workoutDao.getWorkoutSchedule(id)
    }

    fun getWorkoutSetById(id: Long): LiveData<WorkoutSet> {
        return workoutDao.getWorkoutSet(id)
    }

    suspend fun insertWorkoutSet(workoutSet: WorkoutSet): Long {
        return workoutDao.insert(workoutSet)
    }

    suspend fun updateWorkoutSet(workoutSet: WorkoutSet) {
        workoutDao.update(workoutSet)
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

    private suspend fun createWorkoutOverviewIfNotExist(): WorkoutOverview {
        val newWorkoutOverview = WorkoutOverview()
        workoutDao.insert(newWorkoutOverview)
        return workoutDao.getLatestWorkoutOverview()
    }
}