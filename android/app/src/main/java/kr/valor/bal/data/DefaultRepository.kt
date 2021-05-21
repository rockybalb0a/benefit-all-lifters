package kr.valor.bal.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kr.valor.bal.data.entities.WorkoutOverview
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRepository @Inject constructor(private val workoutDao: WorkoutDao) {

    fun getAllWorkouts(): LiveData<List<WorkoutOverview>> = workoutDao.getAllWorkouts()
}