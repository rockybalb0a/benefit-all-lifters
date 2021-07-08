package kr.valor.bal.ui.schedule

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
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

    sealed class Event {
        object ShowAddNewWorkoutDialog: Event()
        object ShowTimerStopActionChoiceDialog: Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

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
    val resetButtonEnabled = _currentWorkoutSchedule.map {
        it.workoutOverview.trackingStatus == TrackingStatus.PAUSE
    }

    private val _elapsedTimeMilli = MutableLiveData<Long>(0L)
    val elapsedTimeMilli: LiveData<Long>
        get() = _elapsedTimeMilli

    private val _onTracking = MutableLiveData<Boolean>()
    val onTracking: LiveData<Boolean>
        get() = _onTracking

    fun onTimerToggleButtonClicked() {
        val trackingStatus = _currentWorkoutOverview.value!!.trackingStatus
        if (trackingStatus == TrackingStatus.NONE) {
            onStartTimeTracking()
            return
        }
        if (ScheduleTimeTracker.onTracking) onPauseTimeTracking() else onResumeTimeTracking()
    }

    fun onTimerResetButtonClicked() {
        viewModelScope.launch {
            eventChannel.send(Event.ShowTimerStopActionChoiceDialog)
        }
    }

    fun onTimerResetActionSelected() {
        _currentWorkoutOverview.value?.let {
            with(it) {
                startTimeMilli = 0L
                elapsedTimeMilli = 0L
                endTimeMilli = 0L
                trackingTimeMilli = 0L
                trackingStatus = TrackingStatus.NONE
                updateWorkoutOverview(it)
            }
        }
        _elapsedTimeMilli.value = 0L
    }

    fun onWorkoutFinishButtonClicked() {
        onStopTimeTracking()
        // TODO : Navigate to ScheduleRecordFragment(temporary name)
    }

    fun onAddNewWorkoutButtonClicked() {
        viewModelScope.launch {
            eventChannel.send(Event.ShowAddNewWorkoutDialog)
        }
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


    private fun onStartTimeTracking() {
        _currentWorkoutOverview.value?.let {
            it.trackingStatus = TrackingStatus.TRACKING
            it.startTimeMilli = System.currentTimeMillis()
            it.trackingTimeMilli = it.startTimeMilli
            updateWorkoutOverview(it)
        }
        activateTimeTracking()
    }

    private fun onPauseTimeTracking() {
        deactivateTimeTracking()
        _currentWorkoutOverview.value?.let {
            it.trackingStatus = TrackingStatus.PAUSE
            updateWorkoutOverview(it)
        }
    }

    private fun onResumeTimeTracking() {
        _currentWorkoutOverview.value?.let {
            if (it.trackingStatus == TrackingStatus.PAUSE)  {
                it.trackingStatus = TrackingStatus.TRACKING
                updateWorkoutOverview(it)
            }
        }
        activateTimeTracking()
    }


    private fun onStopTimeTracking() {
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

    private fun syncElapsedTimeWithDatabase(currentWorkoutOverview: WorkoutOverview) {
        if (currentWorkoutOverview.trackingStatus == TrackingStatus.TRACKING) {
            with(currentWorkoutOverview) {
                elapsedTimeMilli += (System.currentTimeMillis() - trackingTimeMilli)
            }
            activateTimeTracking()
        }
        _elapsedTimeMilli.value = currentWorkoutOverview.elapsedTimeMilli
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

