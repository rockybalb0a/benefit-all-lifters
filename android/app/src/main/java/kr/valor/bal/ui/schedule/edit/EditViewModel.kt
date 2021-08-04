package kr.valor.bal.ui.schedule.edit

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kr.valor.bal.data.DefaultRepository
import kr.valor.bal.data.local.WorkoutSchedule
import kr.valor.bal.data.local.entities.WorkoutDetail
import kr.valor.bal.ui.schedule.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val workoutRepo: DefaultRepository
): BaseViewModel(workoutRepo) {

    sealed class Event {
        object ShowAddNewWorkoutDialog: Event()
        object ShowTimerSettingDialog: Event()
        object EditDetectedEvent: Event()
        data class EditDoneAndBackToDetailDest(val overviewId: Long): Event()
        data class EditRejectAndBackToDetailDest(val overviewId: Long): Event()
    }

    private val overviewId: Long = savedStateHandle["overviewId"]!!

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private val _currentWorkoutOverview = workoutRepo.getWorkoutOverviewCachedById(overviewId)

    private var _currentWorkoutSchedule = _currentWorkoutOverview.switchMap {
        workoutRepo.getWorkoutScheduleCachedByWorkoutOverviewId(it.overviewId)
    }

    val currentWorkoutSchedule: LiveData<WorkoutSchedule>
        get() = _currentWorkoutSchedule

    private val _recyclerViewVisibility = MutableLiveData(true)
    val recyclerViewVisibility: LiveData<Boolean>
        get() = _recyclerViewVisibility

    fun onBackButtonClicked() {
        viewModelScope.launch {
            if (workoutRepo.isChanged(overviewId, this)) {
                eventChannel.send(Event.EditDetectedEvent)
            } else {
                eventChannel.send(Event.EditRejectAndBackToDetailDest(overviewId))
            }
        }
    }

    fun onEditWorkoutTimerSettingButtonClicked() {
        viewModelScope.launch {
            eventChannel.send(Event.ShowTimerSettingDialog)
        }
    }

    fun onEditTimerButtonClicked(minutes: Long) {
        _currentWorkoutOverview.value!!.let {
            it.elapsedTimeMilli += minutes * 60000L
            updateWorkoutOverview(it)
        }
    }

    fun onEditFinishButtonClicked() {
        viewModelScope.launch {
            eventChannel.send(Event.EditDoneAndBackToDetailDest(overviewId))
        }
    }

    fun onEditRejectButtonClicked() {
        viewModelScope.launch {
            _recyclerViewVisibility.value = false
            if(workoutRepo.restore(overviewId, viewModelScope)) {
                eventChannel.send(Event.EditRejectAndBackToDetailDest(overviewId))
            }
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

}