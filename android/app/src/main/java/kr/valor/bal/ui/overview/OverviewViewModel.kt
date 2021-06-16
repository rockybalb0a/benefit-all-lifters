package kr.valor.bal.ui.overview

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.WorkoutDao
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    workoutDao: WorkoutDao
): ViewModel() {

    val workoutSchedules = workoutDao.getAllWorkoutSchedule()

}