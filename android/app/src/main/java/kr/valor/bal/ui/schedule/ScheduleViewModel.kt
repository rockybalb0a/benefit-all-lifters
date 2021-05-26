package kr.valor.bal.ui.schedule

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.valor.bal.data.WorkoutDao
import kr.valor.bal.data.WorkoutDetailAndSets
import kr.valor.bal.data.WorkoutOverviewAndDetails
import kr.valor.bal.data.WorkoutSchedule
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val workoutDao: WorkoutDao
) : ViewModel() {

    private val _currentWorkoutOverview = liveData {
        val loadedWorkoutOverview = workoutDao.getLatestWorkoutOverview()
        if (loadedWorkoutOverview == null) {
            val newWorkoutOverview = WorkoutOverview()
            workoutDao.insert(newWorkoutOverview)
        } else {
            if (loadedWorkoutOverview.date != LocalDate.now()) {
                val newWorkoutOverview = WorkoutOverview()
                workoutDao.insert(newWorkoutOverview)
            }
        }
        emit(workoutDao.getLatestWorkoutOverview()!!)
    }

    private val _currentWorkoutSchedule = _currentWorkoutOverview.switchMap {
        workoutDao.getWorkoutSchedule(it.overviewId)
    }

    val currentWorkoutSchedule: LiveData<WorkoutSchedule>
        get() = _currentWorkoutSchedule


    fun onDialogItemSelected(newWorkoutName: String) {
        _currentWorkoutOverview.value?.let {
            val newWorkoutDetail = WorkoutDetail(
                containerId = it.overviewId,
                workoutName = newWorkoutName
            )
            viewModelScope.launch {
                workoutDao.insert(newWorkoutDetail)
            }
        }
    }

}