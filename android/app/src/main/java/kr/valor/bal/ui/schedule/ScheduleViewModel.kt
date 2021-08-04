package kr.valor.bal.ui.schedule

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.local.workout.WorkoutSchedule
import kr.valor.bal.data.local.workout.entities.WorkoutDetail
import kr.valor.bal.data.local.workout.entities.WorkoutOverview
import kr.valor.bal.utilities.TrackingStatus
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: DefaultRepository,
) : BaseViewModel(repository) {

    sealed class Event {
        object ShowAddNewWorkoutDialog: Event()
        object ShowTimerStopActionChoiceDialog: Event()
        object ShowEditActionChoiceDialog: Event()
        object NavigateToScheduleDoneDest: Event()
        object NavigateToScheduleEditDest: Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _currentWorkoutOverview =
        repository.getWorkoutOverviewOfToday { syncElapsedTimeWithDatabase(it) }

    private val _currentWorkoutSchedule = _currentWorkoutOverview.switchMap {
        repository.getWorkoutScheduleByWorkoutOverviewId(it.overviewId)
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
        viewModelScope.launch {
            eventChannel.send(Event.NavigateToScheduleDoneDest)
        }
    }

    fun onEditWorkoutButtonClicked() {
        viewModelScope.launch {
//            _currentWorkoutOverview.value?.let {
//                it.trackingStatus = TrackingStatus.PAUSE
//                workoutRepo.updateWorkoutOverview(it)
//            }
            eventChannel.send(Event.ShowEditActionChoiceDialog)
        }
    }

    fun onEditWorkoutAcceptClicked() {
        viewModelScope.launch {
            _currentWorkoutOverview.value?.let {
                it.trackingStatus = TrackingStatus.PAUSE
                repository.updateWorkoutOverview(it)
            }
            eventChannel.send(Event.NavigateToScheduleEditDest)
        }
    }



    override fun onAddNewWorkoutButtonClicked() {
        viewModelScope.launch {
            eventChannel.send(Event.ShowAddNewWorkoutDialog)
        }
    }

    override fun onDialogItemSelected(newWorkoutName: String) {
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
//            it.elapsedTimeMilli = 0L
//            it.trackingTimeMilli = 0L
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

}

