package kr.valor.bal.ui.schedule

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.valor.bal.data.WorkoutDao
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val workoutDao: WorkoutDao
) : ViewModel() {

    private val currentWorkoutOverview = MutableLiveData<WorkoutOverview>()

    private val _workoutDetails = workoutDao.getAllWorkoutDetails()
    val workoutDetails: LiveData<List<WorkoutDetail>>
        get() = _workoutDetails


    init {
        setupCurrentWorkoutOverview()
    }


    fun onDialogItemSelected(newWorkoutName: String) {
        currentWorkoutOverview.value?.let {
            val newWorkoutDetail = WorkoutDetail(
                containerId = it.overviewId,
                workoutName = newWorkoutName
            )
            insertWorkoutDetailToCurrentWorkoutOverview(newWorkoutDetail)
        }
    }

    private fun insertWorkoutDetailToCurrentWorkoutOverview(workoutDetail: WorkoutDetail) {
        viewModelScope.launch {
            workoutDao.insertWorkoutDetail(workoutDetail)
        }
    }


    private fun setupCurrentWorkoutOverview() {
        viewModelScope.launch {
            val loadedWorkoutOverview = getCurrentWorkoutOverviewFromLocalStorage()
            if (loadedWorkoutOverview == null) {
                val newWorkoutOverview = WorkoutOverview()
                workoutDao.insertWorkoutOverview(newWorkoutOverview)
            }
            currentWorkoutOverview.value = getCurrentWorkoutOverviewFromLocalStorage()
        }
    }

    private suspend fun getCurrentWorkoutOverviewFromLocalStorage(): WorkoutOverview? {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            var loadedWorkoutOverview = workoutDao.getLatestWorkoutOverview()
            if (loadedWorkoutOverview?.date != LocalDate.now()) {
                loadedWorkoutOverview = null
            }
            loadedWorkoutOverview
        }
    }

}