package kr.valor.bal.ui.schedule

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.WorkoutSchedule
import kr.valor.bal.data.entities.WorkoutDetail
import kr.valor.bal.data.entities.WorkoutOverview
import kr.valor.bal.data.entities.WorkoutSet
import kr.valor.bal.utilities.TrackingStatus
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val workoutRepo: DefaultRepository
) : ViewModel() {

    private val _currentWorkoutOverview = workoutRepo.getWorkoutOverviewOfToday {
        syncElapsedTimeWithDatabase(it)
    }

    private val _currentWorkoutSchedule = _currentWorkoutOverview.switchMap {
        workoutRepo.getWorkoutScheduleByWorkoutOverviewId(it.overviewId)
    }
    val currentWorkoutSchedule: LiveData<WorkoutSchedule>
        get() = _currentWorkoutSchedule
    val layoutViewVisibility = _currentWorkoutSchedule.map {
        it.workoutDetails.isNotEmpty()
    }

    private val _elapsedTimeMilli = MutableLiveData<Long>(0L)
    val elapsedTimeMilli: LiveData<Long>
        get() = _elapsedTimeMilli

    private val _onTracking = MutableLiveData<Boolean>()
    val onTracking: LiveData<Boolean>
        get() = _onTracking

    private fun activateTimeTracking() {
        _onTracking.value =
            ScheduleTimeTracker.activateTimeTracker(viewModelScope) { tick ->
                _currentWorkoutOverview.value?.let {
                    it.elapsedTimeMilli += tick
                    it.trackingTimeMilli = System.currentTimeMillis()
                    _elapsedTimeMilli.value = it.elapsedTimeMilli
                    updateWorkoutOverview(it)
                }
            }
    }

    private fun deactivateTimeTracking() {
        _onTracking.value = ScheduleTimeTracker.deactivateTimeTracker()
    }


    private fun syncElapsedTimeWithDatabase(currentWorkoutOverview: WorkoutOverview) {
        if (currentWorkoutOverview.trackingStatus == TrackingStatus.TRACKING) {
            with(currentWorkoutOverview) {
                elapsedTimeMilli += (System.currentTimeMillis() - trackingTimeMilli)
            }
            activateTimeTracking()
        }
        _elapsedTimeMilli.value = currentWorkoutOverview.elapsedTimeMilli
    }

    private fun onStart() {
        _currentWorkoutOverview.value?.let {
            it.trackingStatus = TrackingStatus.TRACKING
            it.startTimeMilli = System.currentTimeMillis()
            it.trackingTimeMilli = it.startTimeMilli
            updateWorkoutOverview(it)
        }
        activateTimeTracking()
    }

    private fun onPause() {
        deactivateTimeTracking()
        _currentWorkoutOverview.value?.let {
            it.trackingStatus = TrackingStatus.PAUSE
            updateWorkoutOverview(it)
        }
    }

    private fun onResume() {
        _currentWorkoutOverview.value?.let {
            if (it.trackingStatus == TrackingStatus.PAUSE)  {
                it.trackingStatus = TrackingStatus.TRACKING
                updateWorkoutOverview(it)
            }
        }
        activateTimeTracking()
    }


    private fun onStop() {
        deactivateTimeTracking()
        _elapsedTimeMilli.value = 0L
        _currentWorkoutOverview.value?.let {
            it.trackingStatus = TrackingStatus.DONE
            it.endTimeMilli  = it.startTimeMilli + it.elapsedTimeMilli
            it.elapsedTimeMilli = 0L
            it.trackingTimeMilli = 0L
            updateWorkoutOverview(it)
        }
    }


    fun toggleTimer() {
        val trackingStatus = _currentWorkoutOverview.value!!.trackingStatus
        if (trackingStatus == TrackingStatus.NONE) {
            onStart()
            return
        }
        if (ScheduleTimeTracker.onTracking) onPause() else onResume()
    }

    fun stopTimer() {
        onStop()
    }

    // For testing
    fun clearTimer() {
        _currentWorkoutOverview.value?.let {
            with(it) {
                startTimeMilli = 0L
                elapsedTimeMilli = 0L
                endTimeMilli = 0L
                trackingTimeMilli = 0L
                trackingStatus = TrackingStatus.NONE
            }
            updateWorkoutOverview(it)
        }
    }

    fun onWorkoutFinish() {
        onStop()
    }

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
            val latestWorkoutSet = workoutRepo.getLatestWorkoutSetInfo(workoutSet)
            latestWorkoutSet?.let {
                workoutSet.weights = it.weights
                workoutSet.reps = it.reps
                workoutSet.platesStack = it.platesStack
            }
            addWorkoutSet(workoutSet)
        }
    }

    private fun addWorkoutSet(workoutSet: WorkoutSet) {
        viewModelScope.launch {
            workoutRepo.addWorkoutSet(workoutSet)
        }
    }


    private fun deleteWorkoutSet(detailId: Long) {
        viewModelScope.launch {
            workoutRepo.removeWorkoutSet(detailId)
        }
    }

    private fun insertWorkoutDetail(workoutDetail: WorkoutDetail) {
        viewModelScope.launch {
            workoutRepo.addWorkoutDetail(workoutDetail)
        }
    }

    private fun deleteWorkoutDetail(workoutDetail: WorkoutDetail) {
        viewModelScope.launch {
            workoutRepo.dropWorkoutDetail(workoutDetail)
        }
    }

    private fun updateWorkoutOverview(workoutOverview: WorkoutOverview) {
        viewModelScope.launch {
            workoutRepo.updateWorkoutOverview(workoutOverview)
        }
    }

}

