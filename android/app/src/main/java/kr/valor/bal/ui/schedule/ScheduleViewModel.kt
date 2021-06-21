package kr.valor.bal.ui.schedule

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.valor.bal.data.WorkoutDao
import kr.valor.bal.data.WorkoutSchedule
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.data.entities.WorkoutSet
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val workoutDao: WorkoutDao
) : ViewModel() {

    private val _currentWorkoutOverview = liveData {
        val currentDate = LocalDate.now()
        val currentWorkoutOverview = workoutDao.getWorkoutOverviewByDate(currentDate)
            ?: run {
                val newWorkoutOverview = WorkoutOverview()
                workoutDao.insert(newWorkoutOverview)
                workoutDao.getLatestWorkoutOverview()
            }
        emit(currentWorkoutOverview)
    }

    private val _currentWorkoutSchedule = _currentWorkoutOverview.switchMap {
        workoutDao.getWorkoutSchedule(it.overviewId)
    }

    val currentWorkoutSchedule: LiveData<WorkoutSchedule>
        get() = _currentWorkoutSchedule


    fun onAddNewSetButtonClicked(detailId: Long) {
        val newWorkoutSet = WorkoutSet(containerId = detailId)
        insertWorkoutSet(newWorkoutSet)
    }

    fun onDeleteSetButtonClicked(detailId: Long) {
        deleteWorkoutSet(detailId)
    }

    fun onCloseButtonClicked(workoutDetail: WorkoutDetail) {
        deleteWorkoutDetail(workoutDetail)
    }

    fun onDialogItemSelected(newWorkoutName: String) {
        _currentWorkoutOverview.value?.let {
            val newWorkoutDetail = WorkoutDetail(
                containerId = it.overviewId,
                workoutName = newWorkoutName
            )
            insertWorkoutDetail(newWorkoutDetail)
        }
    }

    private fun insertWorkoutSet(workoutSet: WorkoutSet) {
        viewModelScope.launch {
            val latestWorkoutSet = workoutDao.getLatestWorkoutSetByWorkoutDetailId(workoutSet.containerId)
            latestWorkoutSet?.let {
                workoutSet.weights = it.weights
                workoutSet.reps = it.reps
                workoutSet.platesStack = it.platesStack
            }
            workoutDao.insert(workoutSet)
        }
    }


    private fun deleteWorkoutSet(detailId: Long) {
        viewModelScope.launch {
            workoutDao.deleteWorkoutSetAssociatedWithWorkoutDetail(detailId)
        }
    }

    private fun insertWorkoutDetail(workoutDetail: WorkoutDetail) {
        viewModelScope.launch {
            workoutDao.insert(workoutDetail)
        }
    }

    private fun deleteWorkoutDetail(workoutDetail: WorkoutDetail) {
        viewModelScope.launch {
            workoutDao.delete(workoutDetail)
        }
    }

}